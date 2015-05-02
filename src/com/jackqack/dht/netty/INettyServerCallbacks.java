package com.jackqack.dht.netty;

import com.jackqack.dht.node.Node;

/**
 * Created by jackqack on 5/2/15.
 */
public interface INettyServerCallbacks {
    public void onPingSuccessful(Node remoteNode);
}
