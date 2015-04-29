package com.jackqack.dht.kademlia.netty.protocol;

import com.jackqack.dht.node.Node;
import java.io.Serializable;

/**
 * Created by jackqack on 4/26/15.
 */
public class PingMessage implements Serializable {

    private Node node;
    private boolean request;

    public PingMessage(Node node) {
        this.node = node;
        this.request = true;
    }

    public PingMessage(Node node, boolean request) {
        this.node = node;
        this.request = request;
    }

    public Node getNode() { return node; }
    public boolean isRequest() { return request; }

    public void setNode(Node node) { this.node = node; }

    public void setRequest() { request = true; }
    public void setAnswer() { request = false; }

}
