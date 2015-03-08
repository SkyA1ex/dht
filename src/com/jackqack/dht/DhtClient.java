package com.jackqack.dht;

import com.jackqack.strategy.Kademlia;

import java.util.Map;

/**
 * Created by jackqack on 3/8/15.
 */
public class DhtClient {

    private final Node myNode;
    private final Kademlia mKademlia;

    public DhtClient(Kademlia implementation, Node myNode){
        mKademlia = implementation;
        this.myNode = myNode;
    }

    public void bootstrap() { }

    public void insert(Map<Key, Object> o) { }

    public Object lookup(Key key) { return null; }



}
