package com.jackqack.dht;

import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

/**
 * Created by jackqack on 3/8/15.
 */
public interface DistributedHashTable {

    public void run() throws Exception;

    public void bootstrap();

    public void insert(Key key, Object object);

    public Node[] lookup(Key key);
}
