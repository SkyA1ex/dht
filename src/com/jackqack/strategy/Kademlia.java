package com.jackqack.strategy;

import java.util.Map;

import com.jackqack.dht.Key;
import com.jackqack.dht.Node;

/**
 * Created by jackqack on 3/8/15.
 */
public interface Kademlia {

    public static final int IP_PORT = 8089;

    public int ping(Node node);

    public void store(Node node, Map<Key, Object> o);

    public Node[] findNode(Key key);

    public Node[] findValue(Key key); // can return value

}
