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
public class SendFindNodeHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(SendFindNodeHandler.class.toString());
    
    private INettyServerCallbacks mCallbacks;
    private Node[] nodes;

    public SendFindNodeHandler(INettyServerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FindNodeMessage message = (FindNodeMessage) msg;
        // save all nodes in routing table and close the channel
        LOG.info(String.format("Received findNode answer from %s\n", message.getToNode().toString()));
        nodes = message.getNodes();
        for(Node node: nodes) {
            mCallbacks.seenNode(node);
        }
        mCallbacks.seenNode(message.getToNode());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        FindNodeMessage message = (FindNodeMessage) msg;
        LOG.info(String.format("Sent findNode request to %s\n", message.getToNode().toString()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    public boolean hasNodes() { return nodes!=null; }

    public Node[] getNodes() { return nodes; }

}
