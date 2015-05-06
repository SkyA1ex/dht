package com.jackqack.test.dht;

import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import com.jackqack.dht.node.RoutingTable;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by jackqack on 3/4/15.
 */

/*
 * Tests not created because it depends from constants, that
 * may be changed. That is checked manually and works good at this stage.
 */
public class RoutingTableTest {
    private static final Logger LOG = Logger.getLogger(RoutingTable.class.toString());

    RoutingTable table;
    Node[] nodes;


    @Before
    public void beforeTest(){
        nodes = new Node[7];
        nodes[0] = new Node(new Key(Integer.parseInt("0000", 2)), "A", 5511, 5500);
        nodes[1] = new Node(new Key(Integer.parseInt("0001", 2)), "B", 5511, 5500);
        nodes[2] = new Node(new Key(Integer.parseInt("0001", 2)), "B", 5511, 5500);
        nodes[3] = new Node(new Key(Integer.parseInt("0100", 2)), "D", 5511, 5500);
        nodes[4] = new Node(new Key(Integer.parseInt("1001", 2)), "E", 5511, 5500);
        nodes[5] = new Node(new Key(Integer.parseInt("1010", 2)), "F", 5511, 5500);
        nodes[6] = new Node(new Key(Integer.parseInt("1111", 2)), "G", 5511, 5500);

        table = new RoutingTable(nodes[5]) {
            @Override
            public long ping(Node node) {
                return 1;
            }
        };

        for(Node n: nodes) {
            table.seenNode(n);
        }

        LOG.info(table.toString());
    }

    @Test
    public void getClosestNodesTest(){
        StringBuilder sb = new StringBuilder();
        Node find = new Node(new Key(Integer.parseInt("0011", 2)), "C", 5511, 5500);
        Node[] closestNodes = table.getClosestNodes(find.getKey(), Constants.K);
        Node[] neighbors = table.getNeighbors();

        sb.append("\nClosestNodes to " + find.toString()  + " length: " + String.valueOf(closestNodes.length) + "\n");
        for(Node n: closestNodes)
            sb.append(n.toString() + "; ");

        sb.append("\nNeighbors of " + table.myNode.toString() + " length: " + String.valueOf(neighbors.length) + "\n");
        for(Node n: neighbors)
            sb.append(n.toString() + "; ");


        sb.append("\nTest was successfull!");
        LOG.info(sb.toString());
    }


}
