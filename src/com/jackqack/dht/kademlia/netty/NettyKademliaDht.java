package com.jackqack.dht.kademlia.netty;

import com.jackqack.dht.DistributedHashTable;
import com.jackqack.dht.kademlia.Kademlia;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;

import java.util.Map;

/**
 * Created by jackqack on 4/29/15.
 */
public class NettyKademliaDht implements DistributedHashTable, Kademlia {

    private Node mNode;
    private RoutingTable mTable;
    private NettyKademliaServer mServer;

    public NettyKademliaDht(Node node) {
        mNode = node;
        mTable = new RoutingTable(mNode);
        mServer = new NettyKademliaServer(mNode);
    }

    @Override
    public void run() throws Exception {

    }

    @Override
    public void bootstrap() {

    }

    @Override
    public void insert(Map<Key, Object> o) {

    }

    @Override
    public Object lookup(Key key) {
        return null;
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
