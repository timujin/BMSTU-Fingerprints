package com.timujin.Finger;

import com.timujin.FingerprintAlgo;

public class Minutia {
    public int mx;
    public int my;
    public int dir;
    public Neighbour[] neigh;

    public Minutia() {
        this.mx=0; this.my=0; this.dir=0;
        this.neigh = new Neighbour[5];
        for (int i=0; i<5; i++) {
            neigh[i] = new Neighbour();
        }

        this.matchedCs = new int[5]; this.imatchedCs = 0;
        this.matchedRs = new int[5]; this.imatchedRs = 0;
    }

    public void set(byte mx, byte my, byte dir) {
        this.mx=mx; this.my=my; this.dir=dir;
    }

    public void setNeigh(int i, byte mx, byte my, byte dir, byte rc) {
        this.neigh[i].set(mx,my,dir,rc);
    }

    public void initialize(byte[] inBuffer, int start, int fin) {
        int i = start;
        this.mx=inBuffer[i] +128; i++;
        this.my=inBuffer[i] +128; i++;
        this.dir=inBuffer[i] +128; i++;
        int k = 0;
        while (i<fin) {
            this.neigh[k].initialize(inBuffer, i, i+Neighbour.NEIGHBOUR_SIZE);
            k+=1; i+=Neighbour.NEIGHBOUR_SIZE;
        }
    }

    // Match logic


    public int[] matchedCs; public int imatchedCs;
    public int[] matchedRs; public int imatchedRs;


    private void addMatchedC (int i) {
        this.matchedCs[this.imatchedCs] = i;
        this.imatchedCs += 1;
    }

    private void addMatchedR (int i) {
        this.matchedRs[this.imatchedRs] = i;
        this.imatchedRs += 1;
    }

    private boolean isMatchedC (int i) {
        for (int k=0; k<this.imatchedCs; k+=1) {
            if (this.matchedCs[k] == i)
                return true;
        }
        return false;
    }

    private boolean isMatchedR (int i) {
        for (int k=0; k<this.imatchedRs; k+=1) {
            if (this.matchedRs[k] == i)
                return true;
        }
        return false;
    }



    public float match(Minutia other) {
        float totalDissimilarity = 0;
        int neighboursMatched = 0;
        for (int iindex=0; iindex<5; iindex++) {
            if (this.isMatchedC(iindex)) continue;
            int mostSimilarIndex = -1;
            float mostSimilarDissimilarity = -1;
            for (int jindex=0; jindex<5; jindex++) {
                if (this.isMatchedR(jindex)) continue;
                float dissimilarity = Neighbour.match(this,other, this.neigh[iindex], other.neigh[jindex]);
                System.out.printf("Neighbour diss %f...\n", dissimilarity);
                //if (dissimilarity == FingerprintAlgo.NotSimilarAtAll) continue;
                if (mostSimilarDissimilarity == -1 || mostSimilarDissimilarity > dissimilarity) {
                    mostSimilarIndex = jindex;
                    mostSimilarDissimilarity = dissimilarity;
                }
            }
            if (mostSimilarDissimilarity == FingerprintAlgo.NotSimilarAtAll) return FingerprintAlgo.NotSimilarAtAll;
            if (mostSimilarIndex == -1) continue;
            this.addMatchedC(iindex);
            this.addMatchedR(mostSimilarIndex);
            totalDissimilarity += mostSimilarDissimilarity;
            neighboursMatched+=1;
            if (neighboursMatched > FingerprintAlgo.N)
                return (totalDissimilarity / neighboursMatched);
        }
        return FingerprintAlgo.NotSimilarAtAll;
    }

    public String dump() {
        String res = String.format("M: %d,%d,%d; (", mx,my,dir);
        for (Neighbour n: neigh
             ) {
            res += n.dump() + "; ";
        }
        return res+");";
    }
}
