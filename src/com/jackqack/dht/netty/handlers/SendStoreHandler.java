package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.StoreMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/11/15.
 */
public class SendStoreHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(SendStoreHandler.class.toString());

    private INettyServerCallbacks mCallbacks;
    private boolean stored;


    public SendStoreHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
        stored = false;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StoreMessage message = (StoreMessage) msg;
        // set stored flag
        stored = message.isDataStored();
        mCallbacks.seenNode(message.getToNode());
        LOG.info(String.format("Received store answer from %s: %b\n",
                message.getToNode().toString(), message.isDataStored()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        StoreMessage message = (StoreMessage) msg;
        LOG.info(String.format("Sent store request to %s\n", message.getToNode().toString()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    public boolean isDataStored() {
        return stored;
    }

}