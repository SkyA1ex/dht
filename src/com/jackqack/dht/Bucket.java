package com.jackqack.dht;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/3/15.
 */
public class Bucket {
    private static final Logger LOG = Logger.getLogger(Bucket.class.toString());

    public LinkedHashMap<Key, Node> nodes = new LinkedHashMap<Key, Node>(Constants.K);

    public Node[] getClosestNodes(Node node, int limit) {
        synchronized (nodes) {

        }
        return null;
    }


}
