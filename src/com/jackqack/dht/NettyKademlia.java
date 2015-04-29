package com.jackqack.dht;

import com.jackqack.dht.kademlia.Kademlia;
import com.jackqack.dht.kademlia.netty.NettyKademliaServer;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademlia implements Kademlia, DistributedHashTable {
    private static Logger LOG = Logger.getLogger(NettyKademlia.class.toString());

    private NettyKademliaServer mServer;
    private Node mNode;
    private RoutingTable mTable;
    private Thread mServerThread;

    public NettyKademlia(Node myNode, int ipPort) {
        this.mServer = new NettyKademliaServer(ipPort);
        this.mNode = myNode;
        this.mTable = new RoutingTable(mNode);
    }

    public void run() throws Exception {

        mServerThread = new Thread(()-> {
                try {
                    mServer.run();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });
        mServerThread.start();
//        mServer.run();
        LOG.info("Server (" + mNode.toString() + ") is running.");
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

    public void sendMsg(String msg) {
        mServer.sendMsg(msg);
    }

}
