package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.StoreMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/11/15.
 */
public class StoreInboundHandler extends SimpleChannelInboundHandler<StoreMessage> {
    private static final Logger LOG = Logger.getLogger(StoreMessage.class.toString());

    private final INettyServerCallbacks mCallbacks;

    public StoreInboundHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, StoreMessage storeMessage) throws Exception {
        StringBuilder sb = new StringBuilder();
        // store data and set the stored flag
        sb.append(String.format("Received store request from %s\n", storeMessage.getFromNode().toString()));
        storeMessage.setAnswer();
        mCallbacks.store(storeMessage.getData());
        storeMessage.setStored();
        ctx.writeAndFlush(storeMessage);
        sb.append(String.format("Sent store answer to %s\n", storeMessage.getFromNode().toString()));
        mCallbacks.seenNode(storeMessage.getFromNode());
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
