package com.timujin;

import com.licel.jcardsim.base.Simulator;
import com.licel.jcardsim.samples.HelloWorldApplet;
import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.ISO7816;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // 1. Create simulator
        Simulator simulator = new Simulator();

        byte[] finger1 = new byte[]{62, -93, -100, 68, -118, -118, -125, 80, -58, -115, -127, 75, -56, -99, -127, 71, -64, -115, -127, 67, -54, -98, -126, 97, 75, -104, 110, 5, -98, -119, 106, 33, -103, -122, 125, -58, -99, -112, 121, 28, -102, -121, 108, 73, -120, -127, 108, 73, -120, 112, -73, -115, -114, 110, 5, -98, -120, 125, -58, -99, -113, 121, 28, -102, -122, 127, 127, 127, 127, 106, 33, -103, 112, -73, -115, -118, 110, 5, -98, -125, 125, -58, -99, -118, 121, 28, -102, -127, 108, 73, -120, -122, 58, 84, -120, 60, 58, -120, -124, 68, 53, -104, -123, 75, 71, -120, -126, 82, 94, -120, -127, 83, 103, -104, -126, 75, 71, -120, 106, 33, -103, -123, 108, 73, -120, -127, 97, 75, -104, -128, 82, 94, -120, -125, 83, 103, -104, -124, -42, 60, -119, -28, 40, -103, -125, -33, 56, -103, -128, -30, 74, -104, -127, -33, 101, -104, -123, -36, 95, -104, -123, 60, 58, -120, 63, 13, -122, -121, 68, 53, -104, -127, 97, 75, -104, -126, 75, 71, -120, -126, 82, 94, -120, -123, 52, 52, -104, 55, 33, -120, -125, 68, 53, -104, -128, 60, 58, -120, -127, 75, 71, -120, -125, 58, 84, -120, -124, -47, 53, -104, -47, 40, -119, -126, -28, 40, -103, -126, -33, 56, -103, -128, -30, 74, -104, -126, -42, 60, -119, -127};
        byte[] finger2 = new byte[]{33, 54, -102, 49, 25, -101, -123, 71, 38, -101, -123, 67, 53, -102, -126, 68, 67, -102, -128, 65, 84, -118, -124, 71, 38, -101, 98, 10, -100, -122, 100, 31, -101, -125, 88, 35, -101, -127, 96, 112, -102, -120, 83, 90, -102, -122, 32, 75, -103, 33, 54, -102, -125, 65, 84, -118, -127, 57, 99, -103, -125, 46, 101, -119, -124, 39, 99, -104, -125, 38, -25, -99, 96, -57, -115, -116, 92, -50, -99, -118, 67, -2, -99, -127, 52, 2, -100, -128, 49, 25, -101, -124, 49, 25, -101, 52, 2, -100, -125, 67, -2, -99, -123, 88, 35, -101, -126, 71, 38, -101, -128, 67, 53, -102, -125, 27, -20, -100, 29, -30, -99, -127, 33, -35, -115, -126, 38, -25, -99, -126, 67, -2, -99, -125, 52, 2, -100, -128, 67, 53, -102, 71, 38, -101, -126, 88, 35, -101, -125, 100, 31, -101, -124, 83, 90, -102, -124, 68, 67, -102, -127, 18, 20, -100, 27, -20, -100, -123, 52, 2, -100, -123, 49, 25, -101, -127, 33, 54, -102, -125, 26, 56, -118, -124, 83, 90, -102, 123, 113, -102, -127, 115, 109, -118, -126, 122, 115, -102, -126, 109, 112, -102, -127, 96, 112, -102, -126, -93, -100, -125, -69, -115, -124, -126, -61, -92, -125, -123, -70, -67, -110, -122, -78, -66, -126, -123, -88, -66, -110, -125};


// 2. Install applet
        byte[] appletAIDBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        AID appletAID = new AID(appletAIDBytes, (short)0, (byte) appletAIDBytes.length );
        byte[] inBuffer = finger1;
        simulator.installApplet(appletAID, FingerprintApplet.class, inBuffer, (short)0, (byte)1);

// 2.5 Create fingers



// 3. Select applet
        simulator.selectApplet(appletAID);

// 4. Send APDU
        byte[] apduBuf = {1,2,3,1,57};
        //CommandAPDU commandAPDU = new CommandAPDU(apduBuf);
        ResponseAPDU response = new ResponseAPDU(simulator.transmitCommand(apduBuf));
       // ResponseAPDU response2 = new ResponseAPDU(simulator.transmitCommand(apduBuf));

// 5. Check response status word
        byte[] outbuff = response.getBytes();
        System.out.print(outbuff.length);
        for (int i=0; i<outbuff.length; i++) {
            System.out.print(" ");
            System.out.print(outbuff[i]);
        }
    }
}
