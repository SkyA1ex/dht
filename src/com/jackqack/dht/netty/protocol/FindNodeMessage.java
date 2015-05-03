package com.jackqack.dht.netty.protocol;

import com.jackqack.dht.node.Key;
import com.jackqack.dht.node.Node;

import java.io.Serializable;

/**
 * Created by jackqack on 5/3/15.
 */

/*
    Recipient should return K closest to key nodes it know about key
 */
public class FindNodeMessage implements Serializable {

    private Node fromNode;
    private Node toNode;
    private boolean request;
    private Key key;
    private Node[] nodes;

    public FindNodeMessage(Node fromNode, Node toNode, Key key) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.request = true;
        this.key = key;
        nodes = new Node[]{};
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public Key getKey() { return key; }

    public Node[] getNodes() {return nodes; }

    public void setNodes(Node[] nodes) { this.nodes = nodes.clone(); }

    public boolean isRequest() {
        return request;
    }

    public void setRequest() {
        request = true;
    }

    public void setAnswer() {
        request = false;
    }

}
