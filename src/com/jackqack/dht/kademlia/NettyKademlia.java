package com.jackqack.dht.kademlia;

import com.jackqack.dht.DistributedHashTable;
import com.jackqack.dht.kademlia.netty.NettyKademliaServer;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;
import com.jackqack.dht.kademlia.Kademlia;

import java.util.Map;

/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademlia implements Kademlia, DistributedHashTable {

    private NettyKademliaServer mServer;
    private Node mNode;
    private RoutingTable mTable;

    public NettyKademlia(Node myNode) {
        this.mServer = new NettyKademliaServer(Kademlia.IP_PORT);
        this.mNode = myNode;
        this.mTable = new RoutingTable(mNode);
    }

    public void run() throws Exception {
        mServer.run();
        System.out.println("Server is running");
    }

    public void bootstrap() { }

    public void insert(Map<Key, Object> o) { }

    public Object lookup(Key key) { return null; }

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
