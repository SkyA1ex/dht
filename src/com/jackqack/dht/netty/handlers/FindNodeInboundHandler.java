package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.FindNodeMessage;
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
        StringBuilder sb = new StringBuilder();
        // attach K closest to 'key' nodes to msg and send packet back to the sender
        sb.append(String.format("Received findNode request from %s\n", findNodeMessage.getFromNode().toString()));
        findNodeMessage.setAnswer();
        findNodeMessage.setNodes(mCallbacks.getClosestNodes(findNodeMessage.getKey()));
        sb.append(String.format("Attached %d nodes\n", findNodeMessage.getNodes().length));
        ctx.writeAndFlush(findNodeMessage);
        sb.append(String.format("Sent findNode answer to %s\n", findNodeMessage.getFromNode().toString()));
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
