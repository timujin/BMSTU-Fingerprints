package com.timujin.Finger;

public class Minutia {
    public byte mx;
    public byte my;
    public byte dir;
    public Neighbour[] neigh;

    public Minutia() {
        this.mx=0; this.my=0; this.dir=0;
        this.neigh = new Neighbour[5];
        for (int i=0; i<5; i++) {
            neigh[i] = new Neighbour();
        }
    }

    public void set(byte mx, byte my, byte dir) {
        this.mx=mx; this.my=my; this.dir=dir;
    }

    public void setNeigh(int i, byte mx, byte my, byte dir, byte rc) {
        this.neigh[i].set(mx,my,dir,rc);
    }

    public void initialize(byte[] inBuffer, int start, int fin) {
        int i = start;
        this.mx=inBuffer[i]; i++;
        this.my=inBuffer[i]; i++;
        this.dir=inBuffer[i]; i++;
        int k = 0;
        while (i<fin) {
            this.neigh[k].initialize(inBuffer, i, i+Neighbour.NEIGHBOUR_SIZE);
            k+=1; i+=Neighbour.NEIGHBOUR_SIZE;
        }
    }

    public String dump() {
        String res = String.format("M: %d,%d,%d; (", mx+128,my+128,dir+128);
        for (Neighbour n: neigh
             ) {
            res += n.dump() + "; ";
        }
        return res+");";
    }
}
