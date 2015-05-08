package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.FindNodeMessage;
import com.jackqack.dht.netty.protocol.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/8/15.
 */
public class FindNodeInboundHandler extends SimpleChannelInboundHandler<FindNodeMessage> {
    private static final Logger LOG = Logger.getLogger(FindNodeInboundHandler.class.toString());

    private final INettyServerCallbacks mCallbacks;

    public FindNodeInboundHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FindNodeMessage findNodeMessage) throws Exception {
        // attach K closest to 'key' nodes to msg and send packet back to the sender
        LOG.info(String.format("Received findNode request from %s\n", findNodeMessage.getFromNode().toString()));
        findNodeMessage.setAnswer();
        findNodeMessage.setNodes(mCallbacks.getClosestNodes(findNodeMessage.getKey()));
        LOG.info(String.format("Sent findNode answer to %s\n", findNodeMessage.getFromNode().toString()));
        ctx.writeAndFlush(findNodeMessage);
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
