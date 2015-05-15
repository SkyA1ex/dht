package com.jackqack.dht.netty.protocol;

import com.jackqack.dht.file.SimpleData;
import com.jackqack.dht.node.Node;

import java.io.Serializable;

/**
 * Created by jackqack on 5/11/15.
 */
public class StoreMessage implements Serializable {

    private Node fromNode, toNode;
    private SimpleData data;
    private boolean request;
    private boolean stored; // is true if remote node successfully stored the data

    public StoreMessage(Node fromNode, Node toNode, SimpleData data) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        request = true;
        this.data = data;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public SimpleData getData() {
        return data;
    }

    public boolean isRequest() {
        return request;
    }

    public boolean isDataStored() {
        return stored;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }

    public void setData(SimpleData data) {
        this.data = data;
    }

    public void setRequest() {
        request = true;
    }

    public void setAnswer() {
        request = false;
    }

    public void setStored() {
        stored = true;
    }
}
