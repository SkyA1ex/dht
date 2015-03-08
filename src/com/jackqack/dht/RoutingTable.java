package com.jackqack.dht;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/4/15.
 */
public class RoutingTable {
    private static final Logger LOG = Logger.getLogger(RoutingTable.class.toString());
    private final static int BUCKETS = Constants.BIT_LENGTH + 1;

    public final Node myNode;
    /*
     * Bucket i contains nodes on distance [2^i;2^(i+1))
     * It means that in i-th bucket contains nodes with
     * rank of distance = i.
     * For example: d(0001,1001)=1000, rank(1000)=4,
     * this node must be in the last, 4-th bucket.
     */
    Bucket[] buckets = new Bucket[BUCKETS];

    public RoutingTable(Node myNode) {
        this.myNode = myNode;
        for(int i = 0; i < BUCKETS; ++i){
            buckets[i] = new Bucket();
        }
    }

    /*
     * Put the node into bucket or save in cache
     */
    public void seenNode(Node node) {
        Bucket b = buckets[node.getKey().dist(myNode.getKey()).rank()];
        b.seenNode(node);
    }

    /*
     * Returns up to Constants.K closest to myNode nodes
     */
    public Node[] getNeighbors(){
        return getClosestNodes(myNode.getKey());
    }


    /*
     * Return up to Constants.K closest to Key nodes
     */
    public Node[] getClosestNodes(Key key) {
        ArrayList<Node> closestNodes = new ArrayList<Node>(Constants.K);
        int limit = Constants.K;

        int i = myNode.getKey().dist(key).rank();
        for(; i < BUCKETS; ++i) {
            List<Node> list = Arrays.asList(buckets[i].getClosestNodes(key, limit));
            closestNodes.addAll(list);
            limit -= list.size();
            if(limit == 0)
                break;
        }
        return closestNodes.toArray(new Node[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("My node: "+ myNode .toString()+ "\n");
        for(int i = 0; i < BUCKETS; ++i) {
            sb.append("bucket " + String.valueOf(i) + ": ");
            sb.append(buckets[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
