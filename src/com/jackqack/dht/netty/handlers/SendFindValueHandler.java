package com.jackqack.dht.netty.handlers;

import com.jackqack.dht.file.SimpleData;
import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.protocol.FindValueMessage;
import com.jackqack.dht.node.Node;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.logging.Logger;

/**
 * Created by jackqack on 5/9/15.
 */
public class SendFindValueHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(SendFindValueHandler.class.toString());

    private INettyServerCallbacks mCallbacks;
    private SimpleData mData;


    public SendFindValueHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FindValueMessage message = (FindValueMessage) msg;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Received findValue answer from %s\n", message.getToNode().toString()));
        // get the value if received message contains mData
        if (message.hasData()) {
            mData = message.getData();
            sb.append(String.format("Message contains data with key %s\n", mData.getKey().toString()));
        } else {
            // save all nodes in routing table
            for (Node node : message.getNodes()) {
                mCallbacks.seenNode(node);
            }
            sb.append(String.format("Message contains %d nodes\n", message.getNodes().length));
        }
        mCallbacks.seenNode(message.getToNode());
        LOG.info(sb.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        FindValueMessage message = (FindValueMessage) msg;
        LOG.info(String.format("Sent findValue request to %s\n", message.getToNode().toString()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    public boolean hasData() {
        return mData != null;
    }

    public SimpleData getData() {
        return mData;
    }

}