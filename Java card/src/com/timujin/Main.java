package com.timujin;

import com.licel.jcardsim.base.Simulator;
import com.licel.jcardsim.samples.HelloWorldApplet;
import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.ISO7816;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.awt.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        int successes = 0;
        System.out.print("Expected matching pairs:\n");
        for (int i=0; i< TestingData0.data.size(); i++) {
            byte[] finger1 = TestingData0.data.get(i);
            //byte[] finger2 = TestingData1.data.get(TestingData0.data.size() - i - 1);
            byte[] finger2 = TestingData1.data.get(i);
            byte result = Test.compare_fingers(finger1, finger2);
            if (result == 42) {
                System.out.print(i);
                successes +=1;}
        }
        System.out.print("\n");
        System.out.print(successes);
        System.out.print(" of ");
        System.out.print(TestingData0.data.size());


        successes = 0;
        System.out.print("\n\nExpected mismatching pairs:\n");
        for (int i=0; i< TestingData0.data.size(); i++) {
            byte[] finger1 = TestingData0.data.get(i);
            byte[] finger2 = TestingData1.data.get(TestingData0.data.size() - i - 1);
            //byte[] finger2 = TestingData1.data.get(i);
            byte result = Test.compare_fingers(finger1, finger2);
            if (result == 42)
                successes +=1;
        }
        System.out.print("\n");
        System.out.print(successes);
        System.out.print(" of ");
        System.out.print(TestingData0.data.size());
    }
}
