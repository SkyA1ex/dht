package com.jackqack.dht.node;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by jackqack on 3/3/15.
 */
public final class Key implements Comparable<Key> {
    private final static Logger LOG = Logger.getLogger(Key.class.toString());

    private BitSet id;


    public Key() {
        this.id = new BitSet(Constants.BIT_LENGTH);
    }

    public Key(int id) {
        this.id = new BitSet(Constants.BIT_LENGTH);
        for (int i = 7; i >= 0; i--) {
            if ((id & (1 << i)) != 0)
                this.id.set(i);
        }
    }

    public Key(BitSet id) {
        this.id = id;
    }

    public Key(final String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            id = BitSet.valueOf(md.digest(key.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No such algorithm SHA-1");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding UTF-8");
        }
    }

    public BitSet getId() {
        return this.id;
    }

    public void setId(BitSet id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return bytesToHex(id.toByteArray());
    }

    public Key dist(final Key k) {
        BitSet d = (BitSet) this.id.clone();
        d.xor(k.getId());
        return new Key(d);
    }

    public int rank() {
        return id.length();
    }

    @Override
    public boolean equals(Object k) {
        if (k == null || !(k instanceof Key))
            return false;
        return k == this || (this.getId().equals(((Key) k).getId()));
    }

    @Override
    public int compareTo(Key k) {
        if (this.equals(k)) return 0;
        if (this.rank() > k.rank()) return 1;
        if (this.rank() < k.rank()) return -1;
        int index = this.dist(k).rank()-1;
        return id.get(index)? 1 : -1;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public static Key getRandomKey() {
        Key key = new Key();
        Random rand = new Random();
        for (int i = 0; i < Constants.BIT_LENGTH; ++i)
            if (rand.nextBoolean())
                key.getId().set(i);
        return key;
    }

    //Takes from: http://stackoverflow.com/a/9855338
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //Takes from: http://stackoverflow.com/a/140861
    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + +Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
