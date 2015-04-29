package com.jackqack.test.dht;

import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jackqack on 3/3/15.
 */
public class NodeTest {

    @Test
    public void nodeTest() {
        Node n1 = new Node(new Key(12), "localhost", 5500);
        Node n2 = new Node(new Key(10), "localhost", 5500);

        assertNotEquals(n1, n2);
        assertTrue(n1.compareTo(n2) == 1);

        System.out.println("NodeTest.nodeTest was successful!");
    }
}
