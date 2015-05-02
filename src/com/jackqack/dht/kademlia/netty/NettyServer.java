package com.jackqack.dht.kademlia.netty;

import com.jackqack.dht.kademlia.netty.handlers.PingHandler;
import com.jackqack.dht.kademlia.netty.protocol.Message;
import com.jackqack.dht.kademlia.netty.protocol.PingMessage;
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

/**
 * Created by jackqack on 3/8/15.
 */
public class NettyServer {

    private Node mNode;
    private ServerBootstrap bootstrap;
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ChannelFuture serverFuture;

    public NettyServer(Node node) {
        mNode = node;
    }

    public void run() throws Exception {
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        new PingHandler());
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

    // Return ping to host im ms
    public long pingTo(Node toNode) throws InterruptedException {
        final PingHandler pingHandler = new PingHandler();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                        pingHandler);
            }
        });

        // Start the client.
        ChannelFuture f = b.connect(toNode.getIpAddress(), toNode.getTcpPort()).sync();
        f.channel().writeAndFlush(new PingMessage(mNode, toNode)).sync();
        f.channel().read();
        f.channel().closeFuture().sync();
        return pingHandler.getPing();
    }

}
