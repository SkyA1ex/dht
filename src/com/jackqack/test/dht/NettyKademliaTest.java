package com.jackqack.test.dht;

import com.jackqack.dht.NettyKademliaDht;
import com.jackqack.dht.file.SimpleData;
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
        Node node3 = new Node(new Key(0003), "127.0.0.1", 8082, Constants.UDP_PORT);
        Node node4 = new Node(new Key(0004), "127.0.0.1", 8083, Constants.UDP_PORT);
        Node node5 = new Node(new Key(0005), "127.0.0.1", 8084, Constants.UDP_PORT);
        Node node6 = new Node(new Key(0006), "127.0.0.1", 8085, Constants.UDP_PORT);
        Node node7 = new Node(new Key(0007), "127.0.0.1", 8086, Constants.UDP_PORT);
        NettyKademliaDht dht1 = new NettyKademliaDht(node1);
        NettyKademliaDht dht2 = new NettyKademliaDht(node2);
        dht2.mTable.seenNode(node3);
        dht2.mTable.seenNode(node4);
        dht2.mTable.seenNode(node5);
        dht2.mTable.seenNode(node6);
        dht2.mTable.seenNode(node7);
        try {
            dht1.run();
            dht2.run();
            dht1.pingTo(node2);
            dht1.findNode(node2, node5.getKey());
            dht1.findValue(node2, node4.getKey());
            dht1.store(node2, new SimpleData(new Key(0010), "привет"));
            dht1.store(node2, new SimpleData(new Key(0011), "hola"));
            dht2.store(node1, new SimpleData(new Key(0012), "hello"));
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        } finally {
            dht1.waitClose();
            dht2.waitClose();
        }
    }
}
