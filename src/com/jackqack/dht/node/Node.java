package com.jackqack.dht.node;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/3/15.
 */
public class Node implements Serializable, Comparable<Node> {
    private static final Logger LOG = Logger.getLogger(Node.class.toString());

    private Key mKey;
    private String mIpAddress;
    private int mTcpPort;
    private int mUdpPort;

    public Node(Key key, String ipAddress, int tcpPort, int udpPort) {
        this.mKey = key;
        this.mIpAddress = ipAddress;
        this.mTcpPort = tcpPort;
        this.mUdpPort = udpPort;
    }

    public Key getKey() {
        return mKey;
    }

    public int getUdpPort() { return mUdpPort; }

    public int getTcpPort() { return mTcpPort; }

    public String getIpAddress() {
        return mIpAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Node))
            return false;
        Key k = ((Node) o).getKey();
        return this.mKey.equals(k);
    }

    @Override
    public String toString() {
        return String.format("id=%s; address=%s; tcpPort=%d; udpPort=%d;",
                mKey.toString(), mIpAddress, mTcpPort, mUdpPort);
    }

    @Override
    public int compareTo(Node o) {
        return mKey.compareTo(o.getKey());
    }
}
