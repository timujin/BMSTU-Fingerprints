package com.timujin;

import com.licel.jcardsim.base.Simulator;
import com.licel.jcardsim.samples.HelloWorldApplet;
import javacard.framework.AID;
import javacard.framework.APDU;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class Main {

    public static void main(String[] args) {
        // 1. Create simulator
        Simulator simulator = new Simulator();

// 2. Install applet
        byte[] appletAIDBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        AID appletAID = new AID(appletAIDBytes, (short)0, (byte) appletAIDBytes.length );
        simulator.installApplet(appletAID, TestApplet.class);

// 3. Select applet
        simulator.selectApplet(appletAID);

// 4. Send APDU
        CommandAPDU commandAPDU = new CommandAPDU(0x10, 0x10, 0x10, 0x10);
        ResponseAPDU response = new ResponseAPDU(simulator.transmitCommand(new byte[] {0,1,0,0,0,0,0}));

// 5. Check response status word
        System.out.print(response.getSW());
        //assertEquals(0x9000, response.getSW());
    }
}
