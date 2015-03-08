package com.jackqack.test.dht;

import com.jackqack.dht.kademlia.NettyKademlia;
import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import org.junit.Test;
/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademliaTest {

    @Test
    public void MainTest(){
        Node myNode = new Node(new Key(0001), "192.168.0.100", Constants.UDP_PORT);
        try {
            NettyKademlia kademlia = new NettyKademlia(myNode);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
