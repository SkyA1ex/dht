package com.jackqack.dht.netty;

import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

/**
 * Created by jackqack on 5/2/15.
 */
public interface INettyServerCallbacks {
    public void seenNode(Node remoteNode);

    public Node[] getClosestNodes(Key key, int limit);

}
