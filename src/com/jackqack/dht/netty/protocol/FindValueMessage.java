package com.jackqack.dht.netty.protocol;

import com.jackqack.dht.file.SimpleData;
import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

/**
 * Created by jackqack on 5/7/15.
 */
public class FindValueMessage extends FindNodeMessage {

    private SimpleData data;

    public FindValueMessage(Node fromNode, Node toNode, Key key) {
        super(fromNode, toNode, key);
    }

    public SimpleData getData() { return data; }

    public void setData(SimpleData data) { this.data = data; }

    public boolean hasData() { return data != null; }


}
