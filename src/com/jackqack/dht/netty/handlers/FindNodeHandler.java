package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.FindNodeMessage;
import com.jackqack.dht.node.Node;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/3/15.
 */
public class FindNodeHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(FindNodeHandler.class.toString());
    
    private INettyServerCallbacks mCallbacks;


    public FindNodeHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info(String.format("connected %s\n", ctx.channel().remoteAddress()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FindNodeMessage))
            return;
        FindNodeMessage message = (FindNodeMessage) msg;
        // attach K closest nodes to msg and send packet back to the sender
        if (message.isRequest()) {
            LOG.info(String.format("Received findNode request from %s\n", message.getFromNode().toString()));
            message.setAnswer();
            message.setNodes(mCallbacks.getClosestNodes(message.getKey(), message.getLimit()));
            LOG.info(String.format("Sent findNode answer to %s\n", message.getFromNode().toString()));
            ctx.writeAndFlush(message);
        } // save all nodes in routing table and close the channel
        else {
            LOG.info(String.format("Received findNode answer from %s\n", message.getToNode().toString()));
            for(Node node: message.getNodes()) {
                mCallbacks.seenNode(node);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        if (!(msg instanceof FindNodeMessage))
            return;
        FindNodeMessage message = (FindNodeMessage) msg;
        LOG.info(String.format("Sent findNode request to %s\n", message.getToNode().toString()));
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

}
