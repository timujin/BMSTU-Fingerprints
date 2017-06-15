package com.timujin;


import com.timujin.Finger.Finger;

public class FingerprintAlgo {


    public Finger prototype;
    public Finger candidate;

    public FingerprintAlgo() {
        this.prototype = new Finger();
        this.candidate = new Finger();
    }

    public void initialize_prototype(byte[] inBuffer) {
        prototype.initialize(inBuffer);
    }

    public void initialize_candidate(byte[] inBuffer) {
        candidate.initialize(inBuffer);
    }


    public byte match() {



        return (byte)41;
    }
}
