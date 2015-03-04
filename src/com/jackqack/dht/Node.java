package com.jackqack.dht;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/3/15.
 */
public class Node implements Serializable, Comparable<Node> {
    private static final Logger LOG = Logger.getLogger(Node.class.toString());

    private Key key;
    private String ipAddress;

    public Node(Key key, String ipAddress) {
        this.key = key;
        this.ipAddress = ipAddress;
    }

    public Key getKey() {
        return key;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Node))
            return false;
        Key k = ((Node) o).getKey();
        return this.key.equals(k);
    }

    @Override
    public String toString() {
        return "id=" + key.toString() + ", address=" + ipAddress;
    }

    @Override
    public int compareTo(Node o) {
        return key.compareTo(o.getKey());
    }
}
