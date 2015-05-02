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
    private NettyServer mServer;

    public NettyKademliaDht(Node node) {
        mNode = node;
        mTable = new RoutingTable(mNode);
        mServer = new NettyServer(mNode);
    }

    @Override
    public void run() throws Exception {
        mServer.run();
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
    public long ping(Node node) {
        long delay = 0;
        try {
            delay = mServer.pingTo(node);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return delay;
    }

    @Override
    public void store(Node node, Map<Key, Object> o) { }

    @Override
    public Node[] findNode(Key key) {
        return new Node[0];
    }

    @Override
    public Node[] findValue(Key key) {
        return new Node[0];
    }

    public void waitClose() {
        try {
            mServer.waitClose();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
