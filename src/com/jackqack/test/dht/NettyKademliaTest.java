package com.jackqack.test.dht;

import com.jackqack.dht.kademlia.netty.NettyKademliaDht;
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
        Node node1 = new Node(new Key(0001), "127.0.0.1", 8080, Constants.UDP_PORT);
        Node node2 = new Node(new Key(0002), "127.0.0.1", 8081, Constants.UDP_PORT);
        NettyKademliaDht dht1 = new NettyKademliaDht(node1);
        NettyKademliaDht dht2 = new NettyKademliaDht(node2);
        try {
            dht1.run();
            dht2.run();
            dht1.ping(node2);

        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            dht1.waitClose();
            dht2.waitClose();
        }
    }
}
