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
public class PingHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(PingHandler.class.toString());

    private long pingMills;
    private INettyServerCallbacks mCallbacks;


    public PingHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info(String.format("connected %s\n", ctx.channel().remoteAddress()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof PingMessage)) {
            ctx.fireChannelRead(msg);
            return;
        }
        PingMessage pingMessage = (PingMessage) msg;
        // if received pingTo request then send pingTo packet back sender
        if (pingMessage.isRequest()) {
            LOG.info(String.format("Received pingTo request from %s\n", pingMessage.getFromNode().toString()));
            pingMessage.setAnswer();
            LOG.info(String.format("Sent pingTo answer to %s\n", pingMessage.getFromNode().toString()));
            ctx.writeAndFlush(pingMessage);
            mCallbacks.seenNode(pingMessage.getFromNode());
        } // if received pingTo answer then close channel
        else {
            LOG.info(String.format("Received pingTo answer from %s\n", pingMessage.getToNode().toString()));
            pingMills = System.currentTimeMillis() - pingMills;
            mCallbacks.seenNode(pingMessage.getToNode());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        if (!(msg instanceof PingMessage)) {
            return;
        }
        PingMessage pingMessage = (PingMessage) msg;
        LOG.info(String.format("Sent pingTo request to %s\n", pingMessage.getToNode().toString()));
        pingMills = System.currentTimeMillis();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOG.info(String.format("disconnected %s\n", ctx.channel().remoteAddress()));
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
