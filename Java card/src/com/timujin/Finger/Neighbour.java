package com.timujin.Finger;

import com.timujin.FingerprintAlgo;
import sun.security.x509.EDIPartyName;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

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

    private static class Vec {
        public float v0;
        public float v1;
        public Vec(float v0, float v1) {
            this.v0=v0; this.v1=v1;
        }
    }

    private static double angle(Vec vec1, Vec vec2) {
        float dotproduct = vec1.v0 * vec2.v0 + vec1.v1*vec2.v1;
        float determinant= vec1.v0 * vec2.v1 + vec1.v1*vec2.v0;
        return Math.atan2(determinant, dotproduct) + Math.PI;
    }

    private static Vec diff(Minutia P, Neighbour N) {
        float x = P.mx - N.mx;
        float y = P.my - N.my;
        return new Vec(x,y); // TODO: this is a memory leak! Fix!
    }

    private static Vec rotate(Vec vector, int steps) {
        float x = (float) (vector.v0 * Math.cos(steps * Math.PI / 16) - vector.v1 * Math.sin(steps * Math.PI / 16));
        float y = (float) (vector.v0 * Math.sin(steps * Math.PI / 16) - vector.v1 * Math.cos(steps * Math.PI / 16));
        return new Vec(x,y);
    }

    private static float euclidean_distance(Minutia P, Neighbour N) {
        Vec D = Neighbour.diff(P,N);
        return (float) Math.pow(D.v0*D.v0 + D.v1*D.v1,0.5);
    }

    private static double distance_relative_angle(Minutia P, Neighbour N) {
        Vec up = new Vec (0,-1);
        Vec Pdir = Neighbour.rotate(up,P.dir);
        Vec D = Neighbour.diff(P,N);
        return Neighbour.angle(new Vec(-D.v0, -D.v1), Pdir);
    }

    private static double orientation_relative_angle(Minutia P, Neighbour N) {
        Vec up = new Vec (0,-1);
        Vec Pdir = Neighbour.rotate(up, P.dir);
        Vec Ndir = Neighbour.rotate(up, N.dir);
        return Neighbour.angle(Pdir, Ndir);
    }

    private static int ridge_count(Minutia P, Neighbour N) {
        return N.rc;
    }

    private static float bounding_box_1(float diff) {
        if (diff >= FingerprintAlgo.thresnold1)
            return -1;
        return (diff / FingerprintAlgo.thresnold1);
    }
    private static double bounding_box_2(double diff) {
        if (diff >= FingerprintAlgo.thresnold2)
            return -1;
        return (diff / FingerprintAlgo.thresnold2);
    }
    private static double bounding_box_3(double diff) {
        if (diff >= FingerprintAlgo.thresnold3)
            return -1;
        return (diff / FingerprintAlgo.thresnold3);
    }
    private static int bounding_box_4(int diff) {
        if (diff >= FingerprintAlgo.thresnold4)
            return -1;
        return (diff / FingerprintAlgo.thresnold4);
    }

    public static float match (Minutia p1, Minutia p2, Neighbour n1, Neighbour n2) {
        float Ed1 = Neighbour.euclidean_distance(p1,n1);
        float Ed2 = Neighbour.euclidean_distance(p2,n2);
        double Dra1= Neighbour.distance_relative_angle(p1,n1);
        double Dra2= Neighbour.distance_relative_angle(p2,n2);
        double Ora1= Neighbour.orientation_relative_angle(p1,n1);
        double Ora2= Neighbour.orientation_relative_angle(p2,n2);
        int Rc1 = Neighbour.ridge_count(p1,n1);
        int Rc2 = Neighbour.ridge_count(p2,n2);

        float EdDiff = Math.abs(Ed2-Ed1);
        double DraDiff= Math.abs(Dra2-Dra1);
        double OraDiff= Math.abs(Ora2-Ora1);
        int RcDiff = Math.abs(Rc2-Rc1);

        EdDiff = bounding_box_1(EdDiff);
        if (EdDiff == -1) return -1;
        DraDiff = bounding_box_2(DraDiff);
        if (DraDiff == -1) return -1;
        OraDiff = bounding_box_3(OraDiff);
        if (OraDiff == -1) return -1;
        RcDiff = bounding_box_4(RcDiff);
        if (RcDiff == -1) return -1;

        float wEdDiff = EdDiff * FingerprintAlgo.weight1;
        double wDraDiff = DraDiff * FingerprintAlgo.weight1;
        double wOraDiff = OraDiff * FingerprintAlgo.weight1;
        float wRcDiff = RcDiff * FingerprintAlgo.weight1;
        return wEdDiff + (float)wDraDiff + (float)wOraDiff + wRcDiff;
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
