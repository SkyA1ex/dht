package com.jackqack.dht;

import com.jackqack.dht.node.Key;

import java.util.Map;

/**
 * Created by jackqack on 3/8/15.
 */
public interface DistributedHashTable {

    public void run() throws Exception;

    public void bootstrap();

    public void insert(Map<Key, Object> o);

    public Object lookup(Key key);
}
