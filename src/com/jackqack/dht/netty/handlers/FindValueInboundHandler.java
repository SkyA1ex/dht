package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.FindNodeMessage;
import com.jackqack.dht.netty.protocol.FindValueMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/9/15.
 */
public class FindValueInboundHandler extends SimpleChannelInboundHandler<FindValueMessage> {
    private static final Logger LOG = Logger.getLogger(FindNodeInboundHandler.class.toString());

    private final INettyServerCallbacks mCallbacks;

    public FindValueInboundHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FindValueMessage message) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Received findValue request from %s\n", message.getFromNode().toString()));
        if (mCallbacks.hasValue(message.getKey())) {
            // attach SimpleData to msg
            message.setData(mCallbacks.getValue(message.getKey()));
            sb.append(String.format("Attached data with key %s\n", message.getData().getKey().toString()));
        }
        else {
            // attach K closest to 'key' nodes to msg
            message.setAnswer();
            message.setNodes(mCallbacks.getClosestNodes(message.getKey()));
            sb.append(String.format("Attached %d nodes\n", message.getNodes().length));
        }
        // send packet back to the sender
        ctx.writeAndFlush(message);
        sb.append(String.format("Sent findValue answer to %s\n", message.getFromNode().toString()));
        mCallbacks.seenNode(message.getFromNode());
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
