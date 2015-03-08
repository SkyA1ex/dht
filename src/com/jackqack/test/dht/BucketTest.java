package com.jackqack.test.dht;

import com.jackqack.dht.node.Bucket;
import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import org.junit.Test;


/**
 * Created by jackqack on 3/3/15.
 */
public class BucketTest {

    @Test
    public void bucketTest() {
        Bucket b = new Bucket();
        Node[] data = new Node[Constants.K];
        for(int i = 0; i < Constants.K; ++i) {
            data[i] = new Node(new Key(i), String.valueOf(i), 5500);
            b.seenNode(data[i]);
        }

        Node[] nodes =  b.getClosestNodes(new Key(8),100);
        for (Node node : nodes)
            System.out.println(node);



    }

}
