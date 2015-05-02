package com.jackqack.dht.kademlia.netty.handlers;

import com.jackqack.dht.kademlia.netty.protocol.PingMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Created by jackqack on 4/26/15.
 */
public class PingHandler extends ChannelHandlerAdapter {

    private long pingMills;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.printf("connected %s\n", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PingMessage pingMessage = (PingMessage) msg;
        // if received ping request then send ping packet back sender
        if (pingMessage.isRequest()) {
            System.out.printf("Received ping request from %s\n", pingMessage.getFromNode().toString());
            pingMessage.setAnswer();
            System.out.printf("Sent ping answer to %s\n", pingMessage.getFromNode().toString());
            ctx.writeAndFlush(pingMessage);
        } // if received ping answer then close channel
        else {
            System.out.printf("Received ping answer from %s\n", pingMessage.getToNode().toString());
            pingMills = System.currentTimeMillis() - pingMills;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        PingMessage pingMessage = (PingMessage) msg;
        System.out.printf("Sent ping request to %s\n", pingMessage.getToNode().toString());
        pingMills = System.currentTimeMillis();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.printf("disconnected %s\n", ctx.channel().remoteAddress());
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
