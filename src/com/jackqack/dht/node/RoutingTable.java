package com.jackqack.dht.node;

import com.jackqack.dht.netty.handlers.PingHandler;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/4/15.
 */
public abstract class RoutingTable implements Pingable{
    private static final Logger LOG = Logger.getLogger(RoutingTable.class.toString());
    private final static int BUCKETS = Constants.BIT_LENGTH + 1;

    public final Node myNode;
    /*
     * Bucket i contains nodes on distance [2^i;2^(i+1))
     * It means that in i-th bucket contains nodes with
     * rank of distance = i.
     * For example: d(0001,1001)=1000, rank(1000)=4,
     * this node must be in the last, 4-th bucket.
     */
    Bucket[] buckets = new Bucket[BUCKETS];

    public RoutingTable(Node myNode) {
        this.myNode = myNode;
        for(int i = 0; i < BUCKETS; ++i){
            buckets[i] = new Bucket();
        }
    }

    /*
     * Put the node into bucket or save in cache
     */
    public void seenNode(Node node) {
        if (node.equals(myNode))
            return;
        Bucket b = buckets[node.getKey().dist(myNode.getKey()).rank()];
        b.seenNode(node);
    }

    /*
     * Returns up to 'Constants.K' closest to myNode nodes
     */
    public Node[] getNeighbors() {
        return getClosestNodes(myNode.getKey(), Constants.K);
    }

    /*
     * Return up to 'limit' closest to Key nodes
     */
    public Node[] getClosestNodes(Key key, int limit) {
        if (limit <= 0)
            return new Node[0];
        ArrayList<Node> closestNodes = new ArrayList<Node>(Constants.K);

        int i = myNode.getKey().dist(key).rank();
        int k = 0;

        while(k < BUCKETS) {
            List<Node> list = Arrays.asList(buckets[i].getClosestNodes(key, limit));
            closestNodes.addAll(list);
            limit -= list.size();
            if (limit == 0)
                break;
            i = (i + 1) % BUCKETS;
            ++k;
        }
        return closestNodes.toArray(new Node[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("My node: "+ myNode .toString()+ "\n");
        for(int i = 0; i < BUCKETS; ++i) {
            sb.append("bucket " + String.valueOf(i) + ": ");
            sb.append(buckets[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }


    private class Bucket {

        private LinkedHashMap<Key, Node> nodes = new LinkedHashMap<>(Constants.K, (float) 0.75, true);

        public void seenNode(Node newNode) {
            synchronized (nodes) {
                if (nodes.containsKey(newNode.getKey())) {
                    nodes.remove(newNode.getKey());
                    nodes.put(newNode.getKey(), newNode);
                } else if (nodes.size() < Constants.K) {
                    nodes.put(newNode.getKey(), newNode);
                } else {
                    /**
                     * 1. Send pingTo request to least-recently node
                     * 2. Remove this node if it's failed to respond and insert newNode to tail
                     * 3. Otherwise move LR-node to the tail and discard newNode inserting
                     */
                    Map.Entry<Key, Node> entry = nodes.entrySet().iterator().next();
                    nodes.remove(entry.getKey());
                    Node lruNode = entry.getValue();

                    long delay = ping(lruNode);
                    if (delay != -1) {
                        nodes.put(lruNode.getKey(), lruNode);
                    }
                    else {
                        nodes.put(newNode.getKey(), newNode);
                    }
                }
            }
        }

        public Node[] getClosestNodes(final Key key, int limit) {
            synchronized (nodes) {
                TreeMap<Key, Node> sortedNodes = new TreeMap<>();
                for (Node n : nodes.values()) {
                    sortedNodes.put(n.getKey().dist(key), n);
                }
                return Arrays.copyOf(sortedNodes.values().toArray(new Node[0]), Math.min(nodes.size(), limit));
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Node n : nodes.values()) {
                sb.append(n.toString());
                sb.append("; ");
            }

            return sb.toString();
        }
    }

}
