package com.jackqack.dht;

import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.NettyKademliaServer;
import com.jackqack.dht.node.Constants;
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
    public RoutingTable mTable; // TODO: set private
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

    public void waitClose() {
        try {
            mServer.waitClose();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void store(Node node, Map<Key, Object> o) { }

    /**
     * 1. Scans K closest to 'key' nodes in routing table.
     * 2. For a < K nodes send recursive findNode requests and update
     *    routing table after receiving answer.
     * 3. Decrease 'a' and repeat step two while a > 0.
     * 4. Return K closest to 'key' nodes.
     * @param key
     * @return
     */
    public Node[] findNode(Key key) {
        int a = Constants.K;
        try {
            do {
                // Return up to 'a' closest nodes to key stored in routing table
                Node[] nodes = mTable.getClosestNodes(key, a); // TODO: prevent returning nodes with 'mNode' key value
                for(Node node: nodes) {
                    mServer.findNodes(node, key, a);
                }
                --a;
            } while (a > 0);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return mTable.getClosestNodes(key, Constants.K);
    }

    private Node[] findValue(Key key) {
        return new Node[0];
    }

    private class NettyServerCallbacks implements INettyServerCallbacks {

        @Override
        public void seenNode(Node remoteNode) {
            mTable.seenNode(remoteNode);
        }

        @Override
        public Node[] getClosestNodes(Key key, int limit) {
            return mTable.getClosestNodes(key, limit);
        }
    }

}











