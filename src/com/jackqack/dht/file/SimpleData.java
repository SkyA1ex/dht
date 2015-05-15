package com.jackqack.dht.file;

import com.jackqack.dht.node.Key;

import java.io.Serializable;

/**
 * Created by jackqack on 5/7/15.
 */
public class SimpleData implements Serializable {

    private final Key key;
    private Object data;

    public SimpleData(Key key, Object data) {
        this.key = key;
        this.data = data;
    }

    public void setData(Object newData) {
        data = newData;
    }

    public Object getData() {
        return data;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public String toString() {
        return String.format("key - %s; data - %s;", key.toString(), data.toString());
    }
}
