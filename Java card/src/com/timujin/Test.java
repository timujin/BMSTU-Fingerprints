package com.timujin;

import com.licel.jcardsim.base.Simulator;
import javacard.framework.AID;

import javax.smartcardio.ResponseAPDU;

public class Test {

    public static byte compare_fingers(byte[] finger1, byte[] finger2) {
        Simulator simulator = new Simulator();
        byte[] appletAIDBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        AID appletAID = new AID(appletAIDBytes, (short)0, (byte) appletAIDBytes.length );
        simulator.installApplet(appletAID, FingerprintApplet.class, finger1, (short)0, (byte)1);

// 3. Select applet
        simulator.selectApplet(appletAID);

// 4. Send APDU
        //CommandAPDU commandAPDU = new CommandAPDU(apduBuf);
        ResponseAPDU response = new ResponseAPDU(simulator.transmitCommand(finger2));
        // ResponseAPDU response2 = new ResponseAPDU(simulator.transmitCommand(apduBuf));

// 5. Check response status word
        byte[] outbuff = response.getBytes();
        //System.out.print(outbuff.length);
        //for (int i=0; i<outbuff.length; i++) {
            //System.out.print(" ");
            //System.out.print(outbuff[0]);
        //}
        return outbuff[0];
    }
}
