package com.timujin.Finger;


public class Finger {
    public static final int MINUTIA_LEN = 23;
    public static final int NUM_MINUTIAE = 10;
    public Minutia[] minutuae;

    public Finger() {
        this.minutuae = new Minutia[NUM_MINUTIAE];
        for (int i=0; i<NUM_MINUTIAE; i++) {
            minutuae[i] = new Minutia();
        }
    }

    public void initialize(byte[] inBuffer) {
        int i=0;
        for (int start=0, fin=0; fin<MINUTIA_LEN*NUM_MINUTIAE;) {
            fin=start+MINUTIA_LEN;
            this.minutuae[i].initialize(inBuffer, start, fin);
            start=fin;
            i+=1;
        }
    }

    public String dump() {
        String res = "";
        for (Minutia m: this.minutuae
             ) {
            res += m.dump() + "\n";
        }
        return res;
    }
}