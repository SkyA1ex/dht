package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/8/15.
 */
public class PingInboundHandler extends SimpleChannelInboundHandler<PingMessage> {
    private static final Logger LOG = Logger.getLogger(PingInboundHandler.class.toString());

    private final INettyServerCallbacks mCallbacks;

    public PingInboundHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, PingMessage pingMessage) throws Exception {
        StringBuilder sb = new StringBuilder();
        // if received ping request then send ping packet back sender
        sb.append(String.format("Received pingTo request from %s\n", pingMessage.getFromNode().toString()));
        pingMessage.setAnswer();
        sb.append(String.format("Sent pingTo answer to %s\n", pingMessage.getFromNode().toString()));
        ctx.writeAndFlush(pingMessage);
        mCallbacks.seenNode(pingMessage.getFromNode());
        LOG.info(sb.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info(cause.getMessage());
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

}
