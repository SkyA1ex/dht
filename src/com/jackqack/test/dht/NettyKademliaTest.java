package com.jackqack.test.dht;

import com.jackqack.strategy.netty.NettyKademliaImpl;
import org.junit.Test;
/**
 * Created by jackqack on 3/8/15.
 */
public class NettyKademliaTest {

    @Test
    public void MainTest(){
        try {
            NettyKademliaImpl kademlia = new NettyKademliaImpl();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
