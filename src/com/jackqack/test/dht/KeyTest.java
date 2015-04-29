package com.jackqack.test.dht;

import com.jackqack.dht.node.Key;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jackqack on 3/3/15.
 */
public class KeyTest {

    @Test
    public void keyStringTest() {
        Key k1 = new Key("Hey");
        Key k2 = new Key("Hey");
        Key k3 = new Key();

        assertEquals(k1, k2);
        assertEquals(k1.getId(), k2.getId());
        assertEquals(k1.toString(), k2.toString());
        assertEquals(k2.dist(k3).getId(), k2.getId());

        System.out.println("KeyTest.keyStringTest was successful!");
    }

    @Test
    public void keyIntTest() {
        Key k1 = new Key(3);
        Key k2 = new Key(7);
        Key k3 = new Key(4);

        assertEquals(k1.dist(k2).getId(), k3.getId());
        assertTrue(k3.compareTo(k1) == 1);
        assertTrue(k3.compareTo(k3) == 0);

        System.out.println("KeyTest.keyIntTest was successful!");
    }

    @Test
    public void keyRankTest() {
        Key k1 = new Key(Integer.parseInt("0000", 2));
        Key k2 = new Key(Integer.parseInt("0001", 2));
        Key k3 = new Key(Integer.parseInt("0011", 2));
        Key k4 = new Key(Integer.parseInt("0100", 2));
        Key k5 = new Key(Integer.parseInt("1001", 2));
        Key k6 = new Key(Integer.parseInt("1010", 2));
        Key k7 = new Key(Integer.parseInt("1111", 2));

        assertEquals(k1.dist(k7).rank(), 4);


        System.out.println("KeyTest.keyRankTest was successful!");
    }


}
