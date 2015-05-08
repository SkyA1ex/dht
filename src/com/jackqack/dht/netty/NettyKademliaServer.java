package com.jackqack.dht.netty;

import com.jackqack.dht.netty.handlers.FindNodeInboundHandler;
import com.jackqack.dht.netty.handlers.SendFindNodeHandler;
import com.jackqack.dht.netty.handlers.SendPingHandler;
import com.jackqack.dht.netty.handlers.PingInboundHandler;
import com.jackqack.dht.netty.protocol.FindNodeMessage;
import com.jackqack.dht.netty.protocol.Message;
import com.jackqack.dht.netty.protocol.PingMessage;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademliaServer {

    private Node mNode;
    private INettyServerCallbacks mCallbacks;
    private ServerBootstrap bootstrap;
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ChannelFuture serverFuture;

    public NettyKademliaServer(Node node, INettyServerCallbacks callbacks) {
        mNode = node;
        mCallbacks = callbacks;
    }

    public void run() throws InterruptedException {
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        new PingInboundHandler(mCallbacks),
                        new FindNodeInboundHandler(mCallbacks));
            }
        });

        serverFuture = bootstrap.bind(mNode.getTcpPort()).sync();
    }

    public void sendServerMessage(Message msg) {
        System.out.printf("%d channels connected:\n", channels.size());
        for (Channel ch : channels) {
            System.out.printf("local address: %s, ", ch.localAddress().toString());
            System.out.printf("remote address: %s\n", ch.remoteAddress().toString());
            ch.writeAndFlush(msg);
        }
    }

    public void waitClose() throws InterruptedException {
        serverFuture.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    /**
        Return pingTo to host im ms
     */
    public long pingTo(Node toNode) throws InterruptedException, ConnectException, TimeoutException {
        final SendPingHandler sendPingHandler = new SendPingHandler(mCallbacks);
//        final PingInboundHandler pingHandler = new PingInboundHandler(mCallbacks);
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        sendPingHandler);
            }
        });

        // Start the client.
        ChannelFuture f = b.connect(toNode.getIpAddress(), toNode.getTcpPort()).sync();
        f.channel().writeAndFlush(new PingMessage(mNode, toNode)).sync();
        f.channel().read();
        f.channel().closeFuture().sync();
        return sendPingHandler.getPing();
    }

    /**
        Send request to find and return up to 'limit' closest to 'key' nodes.
        After receiving answer add returned nodes to routing table.
     */
    public void findNode(Node toNode, Key key) throws InterruptedException, ConnectException {
        final SendFindNodeHandler sendFindNodeHandler = new SendFindNodeHandler(mCallbacks);
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        sendFindNodeHandler);
            }
        });

        // Start the client.
        ChannelFuture f = b.connect(toNode.getIpAddress(), toNode.getTcpPort()).sync();
        f.channel().writeAndFlush(new FindNodeMessage(mNode, toNode, key)).sync();
        f.channel().read();
        f.channel().closeFuture().sync();
    }

}