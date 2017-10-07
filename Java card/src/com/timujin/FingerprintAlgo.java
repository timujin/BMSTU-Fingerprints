package com.timujin;


import com.timujin.Finger.Finger;
import com.timujin.Finger.Minutia;

public class FingerprintAlgo {
    // OPTIMIZABLE PARAMETERS
    public static final float minDissimilarity = (float)2.23;
    public static final int N = 2;
    public static final float optValue = (float)2.96;
    public static final float finalMatchThreshold = 10;
    public static final float thresnold1 = 150;
    public static final float thresnold2 = 6;
    public static final float thresnold3 = 5;
    public static final float thresnold4 = 5;
    public static final float weight1 = 1;
    public static final float weight2 = 1;
    public static final float weight3 = 1;
    public static final float weight4 = 1;



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
        float totalDissimilarity = 0;
        int minutiaeMatched = 0;

        for (int cindex=0; cindex < 10; cindex+=1) {
            if (this.isMatchedC(cindex)) continue;
            int mostSimilarIndex = -1;
            float mostSimilarDissimilarity = -1;

            for (int rindex=0; rindex<10; rindex++) {
                if (this.isMatchedR(rindex)) continue;
                float dissimilarity = this.prototype.minutuae[cindex].match(this.candidate.minutuae[rindex]);
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

        float finalMatch = totalDissimilarity / minutiaeMatched;
        if (finalMatch < FingerprintAlgo.finalMatchThreshold)
            return FingerprintAlgo.Matched;
        else
            return FingerprintAlgo.Mismatched;
    }
}
