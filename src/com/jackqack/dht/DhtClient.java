package com.jackqack.dht;

import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/8/15.
 */
public class DhtClient {
    private Logger LOG = Logger.getLogger(DhtClient.class.toString());

    private final DistributedHashTable mCallbacks;
    private Node mNode;

    public DhtClient(DistributedHashTable callbacks){
        mCallbacks = callbacks;
    }

    public void run() {
        try {
            mCallbacks.run();
        } catch (Exception e) {
            LOG.severe(e.toString());
        }
    }

    public void bootstrap() { }

    public void insert(Map<Key, Object> o) { }

    public Object lookup(Key key) { return null; }



}
