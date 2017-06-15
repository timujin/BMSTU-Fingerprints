package com.timujin.Finger;

/**
 * Created by Artem on 15.06.2017.
 */
public class Neighbour {
    public static final int NEIGHBOUR_SIZE = 4;

    public byte mx;
    public byte my;
    public byte dir;
    public byte rc;

    public Neighbour(){
        this.mx=0; this.my=0;this.dir=0;this.rc=0;
    }

    public void set(byte mx, byte my, byte dir, byte rc) {
        this.mx=mx; this.my=my;this.dir=dir;this.rc=rc;
    }

    public void initialize(byte[] inBuffer, int start, int fin) {
        this.set(
                inBuffer[start],
                inBuffer[start+1],
                inBuffer[start+2],
                inBuffer[start+3]
        );
    }

    public String dump() {
        return String.format("N: %d, %d, %d, %d", mx+128,my+128,dir+128,rc+128);
    }
}
