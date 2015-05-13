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

        dhts[1].mTable.seenNode(nodes[3]);
        dhts[1].mTable.seenNode(nodes[6]);
        dhts[1].mTable.seenNode(nodes[8]);
        dhts[1].mTable.seenNode(nodes[11]);

        dhts[2].mTable.seenNode(nodes[3]);
        dhts[2].mTable.seenNode(nodes[1]);
        dhts[2].mTable.seenNode(nodes[5]);
        dhts[2].mTable.seenNode(nodes[8]);

        dhts[3].mTable.seenNode(nodes[2]);
        dhts[3].mTable.seenNode(nodes[5]);
        dhts[3].mTable.seenNode(nodes[11]);
        dhts[3].mTable.seenNode(nodes[14]);

        dhts[4].mTable.seenNode(nodes[5]);
        dhts[4].mTable.seenNode(nodes[6]);
        dhts[4].mTable.seenNode(nodes[3]);
        dhts[4].mTable.seenNode(nodes[15]);

        dhts[5].mTable.seenNode(nodes[4]);
        dhts[5].mTable.seenNode(nodes[6]);
        dhts[5].mTable.seenNode(nodes[1]);
        dhts[5].mTable.seenNode(nodes[11]);

        dhts[6].mTable.seenNode(nodes[1]);
        dhts[6].mTable.seenNode(nodes[5]);
        dhts[6].mTable.seenNode(nodes[10]);
        dhts[6].mTable.seenNode(nodes[13]);

        dhts[8].mTable.seenNode(nodes[11]);
        dhts[8].mTable.seenNode(nodes[14]);
        dhts[8].mTable.seenNode(nodes[6]);

        dhts[10].mTable.seenNode(nodes[11]);
        dhts[10].mTable.seenNode(nodes[8]);
        dhts[10].mTable.seenNode(nodes[13]);
        dhts[10].mTable.seenNode(nodes[3]);

        dhts[11].mTable.seenNode(nodes[10]);
        dhts[11].mTable.seenNode(nodes[8]);
        dhts[11].mTable.seenNode(nodes[13]);
        dhts[11].mTable.seenNode(nodes[2]);
        dhts[11].mTable.seenNode(nodes[3]);

        dhts[12].mTable.seenNode(nodes[13]);
        dhts[12].mTable.seenNode(nodes[14]);
        dhts[12].mTable.seenNode(nodes[10]);
        dhts[12].mTable.seenNode(nodes[5]);

        dhts[13].mTable.seenNode(nodes[12]);
        dhts[13].mTable.seenNode(nodes[15]);
        dhts[13].mTable.seenNode(nodes[8]);
        dhts[13].mTable.seenNode(nodes[1]);

        dhts[14].mTable.seenNode(nodes[15]);
        dhts[14].mTable.seenNode(nodes[12]);
        dhts[14].mTable.seenNode(nodes[10]);
        dhts[14].mTable.seenNode(nodes[4]);

        dhts[15].mTable.seenNode(nodes[14]);
        dhts[15].mTable.seenNode(nodes[12]);
        dhts[15].mTable.seenNode(nodes[10]);
        dhts[15].mTable.seenNode(nodes[6]);
        dhts[15].mTable.seenNode(nodes[5]);

        dhts[0].mTable.seenNode(nodes[1]);
        Node[] closest = new Node[0];
        try {
            for (int i = 0; i < n; ++i)
                if (!notRun[i])
                    dhts[i].run();
//            dhts[0].findNode(nodes[6], nodes[0].getKey());
            closest = (Node[]) dhts[0].lookup(nodes[0].getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(closest.length);
    }

}
