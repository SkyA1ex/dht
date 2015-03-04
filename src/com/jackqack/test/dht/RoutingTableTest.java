package com.jackqack.test.dht;

import com.jackqack.dht.Key;
import com.jackqack.dht.Node;
import com.jackqack.dht.RoutingTable;
import org.junit.Test;

/**
 * Created by jackqack on 3/4/15.
 */
public class RoutingTableTest {

    @Test
    public void seenNodeTest() {
        Node[] nodes = new Node[7];
        nodes[0] = new Node(new Key(Integer.parseInt("0000", 2)), "A");
        nodes[1] = new Node(new Key(Integer.parseInt("0001", 2)), "B");
        nodes[2] = new Node(new Key(Integer.parseInt("0011", 2)), "C");
        nodes[3] = new Node(new Key(Integer.parseInt("0100", 2)), "D");
        nodes[4] = new Node(new Key(Integer.parseInt("1001", 2)), "E");
        nodes[5] = new Node(new Key(Integer.parseInt("1010", 2)), "F");
        nodes[6] = new Node(new Key(Integer.parseInt("1111", 2)), "G");

        RoutingTable table = new RoutingTable(nodes[6]);

        for(Node n: nodes) {
            table.seenNode(n);
        }

        System.out.println(table.toString());

        System.out.println("RoutingTableTest.seenNodeTest was successfull!");

    }



}
