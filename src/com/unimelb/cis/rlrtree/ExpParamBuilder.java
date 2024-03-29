package com.unimelb.cis.rlrtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpParamBuilder {

    int pagesizeBeforetuning = 100;
    int pagesizeAftertuning = 104;
    String[] distributions = new String[]{"uniform"};
    int[] sizes = new int[]{10000};
    int[] skewnesses = new int[]{1};
    String[] rlAlgorithms = new String[]{"null"};
    int time = 100;
    int[] dims = new int[]{2};
    String[] curves = new String[]{"H"};
    float[] sides = new float[]{0.1f};
    int[] thresholds = new int[]{10000};
    String[] mlAlgorithms = new String[]{"null"};
    String[] types = new String[]{"null"};
    int[] insertedNums = new int[]{1};
    float[] insertedNumsPerc = new float[]{1};
    float[] deletedNumsPerc = new float[]{1};
    int[] ks = new int[]{1};
    int[] stages = new int[]{2};

    public ExpParamBuilder buildCurve(String... name) {
        this.curves = name;
        return this;
    }

    public ExpParamBuilder buildDistribution(String... distribution) {
        this.distributions = distribution;
        return this;
    }

    public ExpParamBuilder buildDataSetSize(int... size) {
        this.sizes = size;
        return this;
    }

    public ExpParamBuilder buildSkewness(int... skewness) {
        this.skewnesses = skewness;
        return this;
    }

    public ExpParamBuilder buildDim(int... dim) {
        this.dims = dim;
        return this;
    }

    public ExpParamBuilder buildTime(int time) {
        this.time = time;
        return this;
    }

    public ExpParamBuilder buildSides(float... sides) {
        this.sides = sides;
        return this;
    }

    public ExpParamBuilder buildKs(int... ks) {
        this.ks = ks;
        return this;
    }

    public ExpParamBuilder buildInsertedNum(int... insertedNums) {
        this.insertedNums = insertedNums;
        return this;
    }

    public ExpParamBuilder buildInsertedNum(float... insertedNumsPerc) {
        this.insertedNumsPerc = insertedNumsPerc;
        return this;
    }

    public ExpParamBuilder buildDeletedNum(float... deletedNumsPerc) {
        this.deletedNumsPerc = deletedNumsPerc;
        return this;
    }

    public ExpParamBuilder buildAlgorithm(String... algorithm) {
        this.rlAlgorithms = algorithm;
        return this;
    }

    public ExpParamBuilder buildPageSizeBeforeTuning(int pagesize) {
        this.pagesizeBeforetuning = pagesize;
        return this;
    }

    public ExpParamBuilder buildPageSizeAfterTuning(int pagesize) {
        this.pagesizeAftertuning = pagesize;
        return this;
    }

    public ExpParamBuilder buildMLAlgorithm(String... mlAlgorithms) {
        this.mlAlgorithms = mlAlgorithms;
        return this;
    }

    public ExpParamBuilder buildTypes(String... types) {
        this.types = types;
        return this;
    }

    public ExpParamBuilder buildThreshold(int... thresholds) {
        this.thresholds = thresholds;
        return this;
    }

    public ExpParamBuilder buildStages(int... stages) {
        this.stages = stages;
        return this;
    }


    /**
     * windows
     * curves
     * rlAlgorithms
     * dims
     * skewness
     * distributions
     * data set sizes
     */
    public List<ExpParam> buildExpParams() {
        List<ExpParam> expParams = new ArrayList();
        for (int r = 0; r < types.length; r++) {
            for (int k = 0; k < sizes.length; k++) {
                for (int i = 0; i < curves.length; i++) {
                    for (int l = 0; l < dims.length; l++) {
                        for (int m = 0; m < rlAlgorithms.length; m++) {
                            for (int o = 0; o < skewnesses.length; o++) {
                                for (int p = 0; p < mlAlgorithms.length; p++) {
                                    for (int q = 0; q < thresholds.length; q++) {
                                        for (int j = 0; j < distributions.length; j++) {
                                            for (int n = 0; n < stages.length; n++) {
                                                ExpParam expParam = new ExpParam();
                                                expParam.pagesizeBeforetuning = pagesizeBeforetuning;
                                                expParam.pagesizeAftertuning = pagesizeAftertuning;
                                                expParam.curve = curves[i];
                                                expParam.distribution = distributions[j];
                                                expParam.size = sizes[k];
                                                expParam.dim = dims[l];
                                                expParam.rlAlgorithm = rlAlgorithms[m];
                                                expParam.sides = sides;
                                                expParam.ks = ks;

                                                expParam.insertedNums = new int[insertedNumsPerc.length];
                                                expParam.deleteNums = new int[deletedNumsPerc.length];

                                                for (int s = 0; s < insertedNumsPerc.length; s++) {
                                                    expParam.insertedNums[s] = (int) (expParam.size * insertedNumsPerc[s]);
                                                }

                                                for (int s = 0; s < deletedNumsPerc.length; s++) {
                                                    expParam.deleteNums[s] = (int) (expParam.size * deletedNumsPerc[s]);
                                                }

                                                expParam.mlAlgorithm = mlAlgorithms[p];
                                                expParam.threshold = thresholds[q];
                                                expParam.treeType = types[r];
                                                expParam.skewness = skewnesses[o];
                                                if (types[r].equals("Z") || types[r].equals("H")) {
                                                    if (!types[r].equals(curves[i])) {
                                                        continue;
                                                    }
                                                } else if (types[r].equals("ZR")) {
                                                    if (curves[i].equals("H")) {
                                                        continue;
                                                    }
                                                } else if (types[r].equals("HR")) {
                                                    if (curves[i].equals("Z")) {
                                                        continue;
                                                    }
                                                }
                                                expParam.stages = stages[n];
                                                if (distributions[j].equals("skewed")) {
                                                    expParams.add(expParam);
                                                } else {
                                                    expParam.skewness = 1;
                                                    expParams.add(expParam);
                                                }
                                                expParam.times = time;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return expParams;
    }

}
