package com.jackqack.test.dht;

import com.jackqack.dht.NettyKademlia;
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
        Node myNode1 = new Node(new Key(0001), "192.168.0.108", Constants.UDP_PORT);
        Node myNode2 = new Node(new Key(0002), "192.168.0.110", Constants.UDP_PORT);
        try {
            NettyKademlia dht1 = new NettyKademlia(myNode1, 8089);
            NettyKademlia dht2 = new NettyKademlia(myNode2, 8090);
            dht1.run();
            dht2.run();

            while(true) {
                Thread.sleep(5000);
                Node node = new Node(Key.getRandomKey(), "11.11.11.11", 8089);
                dht1.sendMsg("hey");
                dht2.sendMsg("hey");
//                System.out.printf("%d users connected;\n", dht1.getUsersConnected());

            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
