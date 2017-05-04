package com.timujin;

import com.licel.jcardsim.samples.BaseApplet;
import com.licel.jcardsim.samples.HelloWorldApplet;
import javacard.framework.APDU;
import javacard.framework.ISOException;
import javacard.framework.Util;

public class TestApplet extends BaseApplet {

    protected TestApplet(byte[] bArray, short bOffset, byte bLength) {
        this.register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        new TestApplet(bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        short a = 5;
        for (int i = 0; i<99; i++)
            for (int j = 0; j<99; j++)
                for (int k = 0; k<99; k++)
                   for (int l = 0; l<99; l++)
                       for (int m = 0; m<99; m++)
                                a ++;
        ISOException.throwIt((short)a);
    }
}
