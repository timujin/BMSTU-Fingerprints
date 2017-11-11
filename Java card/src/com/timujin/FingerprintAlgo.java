package com.timujin;


import com.timujin.Finger.Finger;
import com.timujin.Finger.Minutia;

/*{'MinDissimilarity': 2.4599465746969438,
 'N': 2.0,
 'OptValue': 3.3156531851467816,
 'finalMatchThreshold': 9.44038096176614,
 'thresholds': [41.8639460239329,
  32.348302153124486,
  0.8611537786509325,
  1.1125524913887437],
 'weights': [0.5, 0.05, 0.05, 0.4]}*/

public class FingerprintAlgo {
    // OPTIMIZABLE PARAMETERS
    public static final float minDissimilarity = (float)2.2306;
    public static final int N = 2;
    public static final float optValue = (float)2.96;
    public static final float finalMatchThreshold = (float)9.5108;
    public static final float thresnold1 = (float)39.9786068;
    public static final float thresnold2 = (float)13.72104482880197;
    public static final float thresnold3 = (float)1.03034591789699;
    public static final int thresnold4 = 2;
    public static final float weight1 = (float)0.5;
    public static final float weight2 = (float) 0.05;
    public static final float weight3 =  (float) 0.05;
    public static final float weight4 = (float) 0.4;
    public static final float NotSimilarAtAll = 999999999;

    public static final byte Matched = 42;
    public static final byte Mismatched = 23;



    public Finger prototype;
    public Finger candidate;

    public int[] matchedCs; public int imatchedCs;
    public int[] matchedRs; public int imatchedRs;

    public FingerprintAlgo() {
        this.prototype = new Finger();
        this.candidate = new Finger();

        this.matchedCs = new int[10]; this.imatchedCs = 0;
        this.matchedRs = new int[10]; this.imatchedRs = 0;
    }

    public void initialize_prototype(byte[] inBuffer) {
        prototype.initialize(inBuffer);
    }

    public void initialize_candidate(byte[] inBuffer) {
        candidate.initialize(inBuffer);
    }

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

    private boolean stoppingConditions(float matchCost, float lastDissimilarity, int totalMatched) {
        if (matchCost / totalMatched < FingerprintAlgo.optValue)
            return true;
        else if (lastDissimilarity < FingerprintAlgo.minDissimilarity)
            return true;
        else
            return false;
    }

    public byte match() {
        //System.out.print(this.candidate.dump());
        //System.out.print(this.prototype.dump());

        //return FingerprintAlgo.Mismatched;
        float totalDissimilarity = 0;
        int minutiaeMatched = 0;
        System.out.printf("start\n");

        //for (int cindex=0; cindex < 10; cindex++) {
        for (int cindex=0; cindex < 1; cindex++) {
            if (this.isMatchedC(cindex)) continue;
            int mostSimilarIndex = -1;
            float mostSimilarDissimilarity = -1;
            System.out.printf("Matching min %d...\n", cindex);

            //for (int rindex=0; rindex<10; rindex++) {
            for (int rindex=8; rindex<9; rindex++) {
                if (this.isMatchedR(rindex)) continue;
                System.out.printf("...with %d...", rindex);
                float dissimilarity = this.prototype.minutuae[cindex].match(this.candidate.minutuae[rindex]);
                System.out.printf("diss = %f\n", dissimilarity);
                if (mostSimilarDissimilarity == -1 || mostSimilarDissimilarity > dissimilarity) {
                    mostSimilarIndex = rindex;
                    mostSimilarDissimilarity = dissimilarity;
                }
            }
            if (mostSimilarDissimilarity == FingerprintAlgo.NotSimilarAtAll)
                return FingerprintAlgo.Mismatched;
            this.addMatchedC(cindex);
            this.addMatchedR(mostSimilarIndex);
            totalDissimilarity += mostSimilarDissimilarity;
            minutiaeMatched +=1;
            if (this.stoppingConditions(totalDissimilarity, mostSimilarDissimilarity, minutiaeMatched))
                return  FingerprintAlgo.Matched;
        }
        System.out.printf("Matched");
        float finalMatch = totalDissimilarity / minutiaeMatched;
        if (finalMatch < FingerprintAlgo.finalMatchThreshold)
            return FingerprintAlgo.Matched;
        else
            return FingerprintAlgo.Mismatched;
    }

    public void reset() {
        this.imatchedCs = 0;
        this.imatchedRs = 0;
    }
}
