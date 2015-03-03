package com.jackqack.test.dht;

import com.jackqack.dht.Bucket;
import com.jackqack.dht.Key;
import com.jackqack.dht.Node;
import org.junit.Test;

import java.util.Iterator;

/**
 * Created by jackqack on 3/3/15.
 */
public class BucketTest {

    @Test
    public void bucketTest() {
        Bucket b = new Bucket();
        Node n1 = new Node(new Key(12), "localhost");
        Node n2 = new Node(new Key(13), "localhost");
        Node n3 = new Node(new Key(14), "localhost");
        Node n4 = new Node(new Key(15), "localhost");

        b.nodes.put(n1.getKey(), n1);
        b.nodes.put(n2.getKey(), n2);
        b.nodes.put(n3.getKey(), n3);
        b.nodes.put(n4.getKey(), n4);

        for (Iterator<Node> it = b.nodes.values().iterator(); it.hasNext(); ) {
            System.out.println(it.next().toString());
        }


    }

}
