package com.jackqack.test.dht;

import com.jackqack.dht.NettyKademliaDht;
import com.jackqack.dht.node.Constants;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;
import org.junit.Test;

/**
 * Created by jackqack on 5/13/15.
 */
public class LookupKademliaTest {


    public static void main(String[] args) {
        int n = 16;
        Node[] nodes = new Node[n];
        NettyKademliaDht[] dhts = new NettyKademliaDht[n];
        for(int i = 0; i < n; ++i) {
            nodes[i] = new Node(new Key(i), "127.0.0.1", Constants.TCP_PORT+i, Constants.UDP_PORT);
            dhts[i] = new NettyKademliaDht(nodes[i]);
        }
        boolean[] notRun = new boolean[n];
        notRun[7] = true;
        notRun[9] = true;

        dhts[1].addNode(nodes[3]);
        dhts[1].addNode(nodes[6]);
        dhts[1].addNode(nodes[8]);
        dhts[1].addNode(nodes[11]);

        dhts[2].addNode(nodes[3]);
        dhts[2].addNode(nodes[1]);
        dhts[2].addNode(nodes[5]);
        dhts[2].addNode(nodes[8]);

        dhts[3].addNode(nodes[2]);
        dhts[3].addNode(nodes[5]);
        dhts[3].addNode(nodes[11]);
        dhts[3].addNode(nodes[14]);

        dhts[4].addNode(nodes[5]);
        dhts[4].addNode(nodes[6]);
        dhts[4].addNode(nodes[3]);
        dhts[4].addNode(nodes[15]);

        dhts[5].addNode(nodes[4]);
        dhts[5].addNode(nodes[6]);
        dhts[5].addNode(nodes[1]);
        dhts[5].addNode(nodes[11]);

        dhts[6].addNode(nodes[1]);
        dhts[6].addNode(nodes[5]);
        dhts[6].addNode(nodes[10]);
        dhts[6].addNode(nodes[13]);

        dhts[8].addNode(nodes[11]);
        dhts[8].addNode(nodes[14]);
        dhts[8].addNode(nodes[6]);

        dhts[10].addNode(nodes[11]);
        dhts[10].addNode(nodes[8]);
        dhts[10].addNode(nodes[13]);
        dhts[10].addNode(nodes[3]);

        dhts[11].addNode(nodes[10]);
        dhts[11].addNode(nodes[8]);
        dhts[11].addNode(nodes[13]);
        dhts[11].addNode(nodes[2]);
        dhts[11].addNode(nodes[3]);

        dhts[12].addNode(nodes[13]);
        dhts[12].addNode(nodes[14]);
        dhts[12].addNode(nodes[10]);
        dhts[12].addNode(nodes[5]);

        dhts[13].addNode(nodes[12]);
        dhts[13].addNode(nodes[15]);
        dhts[13].addNode(nodes[8]);
        dhts[13].addNode(nodes[1]);

        dhts[14].addNode(nodes[15]);
        dhts[14].addNode(nodes[12]);
        dhts[14].addNode(nodes[10]);
        dhts[14].addNode(nodes[4]);

        dhts[15].addNode(nodes[14]);
        dhts[15].addNode(nodes[12]);
        dhts[15].addNode(nodes[10]);
        dhts[15].addNode(nodes[6]);
        dhts[15].addNode(nodes[5]);

        dhts[0].addNode(nodes[1]);
        Node[] closest = new Node[0];
        try {
            for (int i = 0; i < n; ++i)
                if (!notRun[i])
                    dhts[i].run();
            dhts[0].bootstrap();

            Key key1 = Key.getRandomKey();
            Key key2 = Key.getRandomKey();
            Key key3 = Key.getRandomKey();

            dhts[0].insert(key1, "HI ALL STRING");
            dhts[1].insert(key2, "SECOND DATA");
            dhts[14].insert(key3, "FINAL DATA");

            String str2 = (String) dhts[4].find(key2);
            String str1 = (String) dhts[13].find(key1);
            String str3 = (String) dhts[11].find(key3);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(closest.length);
    }

}
