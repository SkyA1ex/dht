package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.PingMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.logging.Logger;

/**
 * Created by jackqack on 4/26/15.
 */
public class SendPingHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(SendPingHandler.class.toString());

    private long pingMills;
    private INettyServerCallbacks mCallbacks;

    public SendPingHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // if received ping answer then close channel
        PingMessage pingMessage = (PingMessage) msg;
        LOG.info(String.format("Received pingTo answer from %s\n", pingMessage.getToNode().toString()));
        pingMills = System.currentTimeMillis() - pingMills;
        mCallbacks.seenNode(pingMessage.getToNode());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        PingMessage pingMessage = (PingMessage) msg;
        LOG.info(String.format("Sent pingTo request to %s\n", pingMessage.getToNode().toString()));
        pingMills = System.currentTimeMillis();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    public long getPing() {
        return pingMills;
    }
}
