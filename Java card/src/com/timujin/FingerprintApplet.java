package com.timujin;

import com.licel.jcardsim.samples.BaseApplet;
import com.timujin.Finger.Finger;
import javacard.framework.APDU;
import javacard.framework.ISOException;

public class FingerprintApplet extends BaseApplet {

    public FingerprintAlgo algo;

    private FingerprintApplet(byte[] bArray, short bOffset, byte bLength) {
        this.algo = new FingerprintAlgo();
        algo.initialize_prototype(bArray);
        System.out.print(algo.prototype.dump());
        this.register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        new FingerprintApplet(bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        //byte resp = check(buffer[ISO7816.OFFSET_CDATA]);
        byte resp = algo.match();

        buffer[0] = resp;

        // send the 2-balance byte at the offset
        // 0 in the apdu buffer
        apdu.setOutgoingAndSend((short)0,(short)2);
    }
}