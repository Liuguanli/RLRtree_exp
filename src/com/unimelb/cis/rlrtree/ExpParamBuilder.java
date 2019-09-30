package com.unimelb.cis.rlrtree;

import java.util.ArrayList;
import java.util.List;

public class ExpParamBuilder {

    int pagesizeBeforetuning = 100;
    int pagesizeAftertuning = 104;
    String[] distributions = new String[]{"uniform"};
    int[] sizes = new int[]{10000};
    int[] skewnesses = new int[]{1};
    String[] rlAlgorithms = new String[]{"QDN"};
    int time = 100;
    int[] dims = new int[]{2};
    String[] curves = new String[]{"H"};
    float[] sides = new float[]{0.1f};
    int[] thresholds = new int[]{10000};
    String[] mlAlgorithms = new String[]{"MultilayerPerceptron"};
    String[] types = new String[]{"partition"};
    int[] insertedNums = new int[]{1};

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

    public ExpParamBuilder buildInsertedNum(int... insertedNums) {
        this.insertedNums = insertedNums;
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
        for (int i = 0; i < curves.length; i++) {
            for (int k = 0; k < sizes.length; k++) {
                for (int l = 0; l < dims.length; l++) {
                    for (int m = 0; m < rlAlgorithms.length; m++) {
                        for (int n = 0; n < sides.length; n++) {
                            for (int o = 0; o < skewnesses.length; o++) {
                                for (int p = 0; p < mlAlgorithms.length; p++) {
                                    for (int q = 0; q < thresholds.length; q++) {
                                        for (int r = 0; r < types.length; r++) {
                                            for (int s = 0; s < insertedNums.length; s++) {
                                                for (int j = 0; j < distributions.length; j++) {
                                                    ExpParam expParam = new ExpParam();
                                                    expParam.pagesizeBeforetuning = pagesizeBeforetuning;
                                                    expParam.pagesizeAftertuning = pagesizeAftertuning;
                                                    expParam.curve = curves[i];
                                                    expParam.distribution = distributions[j];
                                                    expParam.size = sizes[k];
                                                    expParam.dim = dims[l];
                                                    expParam.rlAlgorithm = rlAlgorithms[m];
                                                    expParam.side = sides[n];
                                                    expParam.mlAlgorithm = mlAlgorithms[p];
                                                    expParam.threshold = thresholds[q];
                                                    expParam.treeType = types[r];
                                                    expParam.insertedNum = insertedNums[s];
                                                    if (distributions[j].equals("skewed")) {
                                                        expParam.skewness = skewnesses[o];
                                                        expParams.add(expParam);
                                                    } else {
                                                        expParam.skewness = 1;
                                                        expParams.add(expParam);
                                                        break;
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
            }

        }
        return expParams;
    }

}
