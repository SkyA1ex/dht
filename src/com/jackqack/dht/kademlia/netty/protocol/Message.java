package com.jackqack.dht.kademlia.netty.protocol;

import java.io.Serializable;

/**
 * Created by jackqack on 4/22/15.
 */
public class Message implements Serializable {

    private int id;
    private String msg;

    public Message(int id, String msg) { this.id = id; this.msg = msg; }

    public int getId() { return id; }
    public String getMsg() { return msg; }

}
