package com.jackqack.test.dht;


import com.jackqack.dht.Constants;
import com.jackqack.dht.Key;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jackqack on 3/3/15.
 */
public class KeyTest {

    @Test
    public void keyStringTest() {
        Key k1 = new Key("Hey");
        Key k2 = new Key("Hey");
        Key k3 = new Key();

        assertEquals(k1,k2);
        assertEquals(k1.getId(),k2.getId());
        assertEquals(k1.toString(), k2.toString());
        assertEquals(k2.dist(k3).getId(),k2.getId());

        System.out.println("KeyTest.keyStringTest ended successfully!");
    }

    @Test
    public void keyIntTest() {
        Key k1 = new Key(3);
        Key k2 = new Key(7);
        Key k3 = new Key(4);

        assertEquals(k1.dist(k2).getId(), k3.getId());
        assertTrue(k3.compareTo(k1) == 1);
        assertTrue(k3.compareTo(k3) == 0);

        System.out.println("KeyTest.keyIntTest ended successfully!");
    }


}
