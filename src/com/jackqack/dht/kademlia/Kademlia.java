package com.jackqack.dht.kademlia;

import java.util.Map;

import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

/**
 * Created by jackqack on 3/8/15.
 */
public interface Kademlia {

    public static final int IP_PORT = Constants.TCP_PORT;

    public long ping(Node node);

    public void store(Node node, Map<Key, Object> o);

    public Node[] findNode(Key key);

    public Node[] findValue(Key key); // can return value

}
