package com.jackqack.strategy.netty;

import com.jackqack.dht.Key;
import com.jackqack.dht.Node;
import com.jackqack.strategy.Kademlia;

import java.util.Map;

/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademliaImpl implements Kademlia {



    public NettyKademliaImpl() throws Exception{
        new NettyServer(Kademlia.IP_PORT).run();
    }

    @Override
    public int ping(Node node) {
        return 0;
    }

    @Override
    public void store(Node node, Map<Key, Object> o) {

    }

    @Override
    public Node[] findNode(Key key) {
        return new Node[0];
    }

    @Override
    public Node[] findValue(Key key) {
        return new Node[0];
    }
}
