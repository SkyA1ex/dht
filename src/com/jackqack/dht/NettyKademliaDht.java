package com.jackqack.dht;

import com.jackqack.dht.file.SimpleData;
import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.NettyKademliaServer;
import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.net.ConnectException;
import java.util.*;
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
    private HashMap<Key, SimpleData> mData;

    public NettyKademliaDht(Node node) {
        mNode = node;
        mTable = new RoutingTable(mNode) {
            @Override
            public long ping(Node node) {
                return pingTo(node);
            }
        };
        mCallbacks = new NettyServerCallbacks();
        mServer = new NettyKademliaServer(mNode, mCallbacks);
        mData = new HashMap<>();
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

    /**
     * Get set of K closest to 'key' nodes from routing table and send findNode to 'a' of them
     * which not seen yet.
     * Repeat sending findNode request while exist non-seen nodes returning by routing table.
     * @param key
     * @return
     */
    @Override
    public Object lookup(Key key) {
        // Map that save info which node seen
        HashMap<Key, Boolean> seenNodes = new HashMap<>();
        int seen;
        // do until all nodes are seen
        do {
            // get K closest nodes
            Node[] closestNodes = mTable.getClosestNodes(key, Constants.K);
            // pick 'a' nodes from K closest
            ArrayList<Node> requestNodes = new ArrayList<>();
            int i = 0;
            for(Node node: closestNodes) {
                if (!seenNodes.containsKey(node.getKey())) {
                    requestNodes.add(node);
                    if (++i == Constants.a)
                        break;
                }
            }
            // send findNode to nodes chosen before
            seen = 0;
            for(Node node: requestNodes) {
                // send findNode request and add to seenNodes
                findNode(node, key);
                seenNodes.put(node.getKey(), true);
                ++seen;
            }
        } while(seen != 0);
        // TODO: figure out what should be returned (findValue)
        return mTable.getClosestNodes(key, Constants.K);
    }

    public long pingTo(Node node) {
        long delay = -1;
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

    public void store(Node toNode, SimpleData data) {
        try {
            mServer.store(toNode, data);
        } catch (ConnectException e) {
            LOG.info("Can't connect to node " + toNode.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns K closest to 'key' nodes from 'toNode' routing table
     * and updates sender's routing table
     */
    public void findNode(Node toNode, Key key) {
        try {
            mServer.findNode(toNode, key);
        } catch (ConnectException e) {
            LOG.info("Can't connect to node " + toNode.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * If 'toNode' stores value with key 'key' then it return the stored value.
     * Else returns K closest to 'key' nodes from 'toNode' routing table
     * and updates senders's routing table.
     *
     * @param key
     * @return
     */
    public Object findValue(Node toNode, Key key) {
        try {
            mServer.findValue(toNode, key);
        } catch (ConnectException e) {
            LOG.info("Can't connect to node " + toNode.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    private class NettyServerCallbacks implements INettyServerCallbacks {

        @Override
        public void seenNode(Node remoteNode) {
            mTable.seenNode(remoteNode);
        }

        @Override
        public Node[] getClosestNodes(Key key) {
            return mTable.getClosestNodes(key, Constants.K);
        }

        @Override
        public boolean hasValue(Key key) {
            return mData.containsKey(key);
        }

        @Override
        public SimpleData getValue(Key key) {
            return mData.get(key);
        }

        @Override
        public void store(SimpleData data) {
            mData.put(data.getKey(), data);
        }
    }

}











