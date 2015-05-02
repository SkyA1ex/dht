package com.jackqack.dht;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.NettyKademliaServer;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Created by jackqack on 4/29/15.
 */
public class NettyKademliaDht implements DistributedHashTable {
    private static final Logger LOG = Logger.getLogger(NettyKademliaDht.class.toString());

    private Node mNode;
    private RoutingTable mTable;
    private NettyKademliaServer mServer;
    private NettyServerCallbacks mCallbacks;

    public NettyKademliaDht(Node node) {
        mNode = node;
        mTable = new RoutingTable(mNode);
        mCallbacks = new NettyServerCallbacks();
        mServer = new NettyKademliaServer(mNode, mCallbacks);
    }

    @Override
    public void run() throws InterruptedException {
        mServer.run();
    }

    @Override
    public void bootstrap() {

    }

    @Override
    public void insert(Map<Key, Object> o) {

    }

    @Override
    public Object lookup(Key key) {
        return null;
    }

    public void waitClose() {
        try {
            mServer.waitClose();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public long ping(Node node) {
        long delay = 0;
        try {
            delay = mServer.pingTo(node);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ConnectException e) {
            LOG.info("Can't connect to node " + node.toString());
        } catch (TimeoutException e) {
            LOG.info("Timeout exception to node " + node.toString());
        }
        return delay;
    }


    private void store(Node node, Map<Key, Object> o) { }

    private Node[] findNode(Key key) {
        return new Node[0];
    }

    private Node[] findValue(Key key) {
        return new Node[0];
    }

    private class NettyServerCallbacks implements INettyServerCallbacks {

        @Override
        public void onPingSuccessful(Node remoteNode) {
            mTable.seenNode(remoteNode);
        }

    }

}
