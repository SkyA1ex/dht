package com.jackqack.dht;

import com.jackqack.dht.file.SimpleData;
import com.jackqack.dht.netty.INettyServerCallbacks;
import com.jackqack.dht.netty.NettyKademliaServer;
import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;

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
    private RoutingTable mTable;
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

    /**
     * Manually adds previously known existing Kademlia node to routing table
     *
     * @param node existing Kademlia node
     */
    public void addNode(Node node) {
        if (node != null)
            mTable.seenNode(node);
    }

    /**
     * Performs a node lookup with its own key. So it fills up its routing table
     * based on the responses and inserts itself into the k-buckets of the other
     * nodes in the system.
     */
    @Override
    public void bootstrap() {
        lookup(mNode.getKey());
    }

    /**
     * Insert object in Kademlia.
     * Perform lookup to find k closest nodes to key of object.
     * Then send STORE Kademlia instruction to all of them.
     *
     * @param key    a key of object
     * @param object an object which should be stored in Kademlia
     */
    @Override
    public void insert(Key key, Object object) {
        SimpleData data = new SimpleData(key, object);
        Node[] nodes = lookup(key);
        for (Node node : nodes) {
            store(node, data);
        }
    }

    /**
     * Locate k closest nodes to given key.
     * Gets set of K closest to 'key' nodes from routing table and send
     * findNode to 'a' of them which not seen yet.
     * Repeats sending findNode request while exist non-seen nodes
     * in set of K closest nodes returning by routing table.
     *
     * @param key the key on which will lookup
     * @return Array of k closest nodes to given key
     */
    @Override
    public Node[] lookup(Key key) {
        // Map that save info which node seen
        HashMap<Key, Boolean> seenNodes = new HashMap<>();
        seenNodes.put(mNode.getKey(), true);
        TreeSet<Node> closestNodes = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                Integer d1 = n1.getKey().dist(key).rank();
                Integer d2 = n2.getKey().dist(key).rank();
                if (d1.compareTo(d2) == 0)
                    return n1.getKey().compareTo(n2.getKey());
                return d1.compareTo(d2);
            }
        });
        // get K closest nodes
        closestNodes.addAll(Arrays.asList(mTable.getClosestNodes(key, Constants.K)));

        // do until all nodes are seen
        int seen;
        ArrayList<Node> requestNodes = new ArrayList<>();
        do {
            requestNodes.clear();
            // pick 'a' nodes from K closest
            Iterator<Node> it = closestNodes.iterator();
            int i = 0;
            int passed = 0;
            while (it.hasNext()) {
                Node node = it.next();
                if (!seenNodes.containsKey(node.getKey())) {
                    requestNodes.add(node);
                    if (++i == Constants.a)
                        break;
                }
                if (++passed == Constants.K)
                    break;
            }
            // send findNode to nodes chosen before
            seen = requestNodes.size();
            for (Node node : requestNodes) {
                // send findNode request and add to seenNodes
                Node[] returnedNodes = findNode(node, key);
                closestNodes.addAll(Arrays.asList(returnedNodes));
                seenNodes.put(node.getKey(), true);
            }
        } while (seen != 0);
        return Arrays.asList(closestNodes.toArray(new Node[0])).subList(0, Constants.K).toArray(new Node[0]);
    }

    /**
     * Search value of data in the Kadenlia by given key
     *
     * @param key a key of data which should be returned
     * @return Object if data with this key found and null otherwise.
     */
    public Object find(Key key) {
        Node[] nodes = lookup(key);
        Object result;
        for (Node node : nodes) {
            result = findValue(node, key);
            if (result != null)
                return result;
        }
        return null;
    }

    public void waitClose() {
        try {
            mServer.waitClose();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long pingTo(Node node) {
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

    private void store(Node toNode, SimpleData data) {
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
    private Node[] findNode(Node toNode, Key key) {
        try {
            return mServer.findNode(toNode, key);
        } catch (ConnectException e) {
            LOG.info("Can't connect to node " + toNode.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * If 'toNode' stores value with key 'key' then it return the stored value.
     * Else returns K closest to 'key' nodes from 'toNode' routing table
     * and updates senders's routing table.
     *
     * @param key
     * @return
     */
    private Object findValue(Node toNode, Key key) {
        try {
            return mServer.findValue(toNode, key).getData();
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











