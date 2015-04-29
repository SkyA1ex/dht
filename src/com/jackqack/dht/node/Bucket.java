package com.jackqack.dht.node;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/3/15.
 */
public class Bucket {
    private static final Logger LOG = Logger.getLogger(Bucket.class.toString());

    private LinkedHashMap<Key, Node> nodes = new LinkedHashMap<>(Constants.K, (float) 0.75, true);

    public void seenNode(Node node) {
        synchronized (nodes) {
            if (nodes.containsKey(node.getKey())) {
                nodes.remove(node.getKey());
                nodes.put(node.getKey(), node);
            } else if (nodes.size() < Constants.K) {
                nodes.put(node.getKey(), node);
            } else {
                // TODO: figure out what to do
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