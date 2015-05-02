package com.jackqack.dht.kademlia.netty.protocol;

import com.jackqack.dht.node.Node;
import java.io.Serializable;

/**
 * Created by jackqack on 4/26/15.
 */
public class PingMessage implements Serializable {

    private Node fromNode;
    private Node toNode;
    private boolean request;

    public PingMessage(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.request = true;
    }

    public Node getFromNode() { return fromNode; }
    public Node getToNode() { return toNode; }
    public boolean isRequest() { return request; }

    public void setFromNode(Node fromNode) { this.fromNode = fromNode; }
    public void setToNode(Node toNode) { this.toNode = toNode; }

    public void setRequest() { request = true; }
    public void setAnswer() { request = false; }

}
