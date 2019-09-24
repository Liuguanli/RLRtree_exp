package com.unimelb.cis.rlrtree;

import java.util.ArrayList;
import java.util.List;

public class ExpParamBuilder {

    int pagesizeBeforetuning = 100;
    int pagesizeAftertuning = 104;
    String[] distributions;
    int[] sizes;
    int[] skewnesses;
    String[] algorithms;
    int time;
    int[] dims;
    String[] curves;
    float[] sides;

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

    public ExpParamBuilder buildAlgorithm(String... algorithm) {
        this.algorithms = algorithm;
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


    /**
     * windows
     * curves
     * algorithms
     * dims
     * skewness
     * distributions
     * data set sizes
     */
    public List<ExpParam> buildExpParams() {
        List<ExpParam> expParams = new ArrayList();
        for (int i = 0; i < curves.length; i++) {
            for (int j = 0; j < distributions.length; j++) {
                for (int k = 0; k < sizes.length; k++) {
                    for (int l = 0; l < dims.length; l++) {
                        for (int m = 0; m < algorithms.length; m++) {
                            for (int n = 0; n < sides.length; n++) {
                                for (int o = 0; o < skewnesses.length; o++) {
                                    ExpParam expParam = new ExpParam();
                                    expParam.pagesizeBeforetuning = pagesizeBeforetuning;
                                    expParam.pagesizeAftertuning = pagesizeAftertuning;
                                    expParam.curve = curves[i];
                                    expParam.distribution = distributions[j];
                                    expParam.size = sizes[k];
                                    expParam.dim = dims[l];
                                    expParam.algorithm = algorithms[m];
                                    expParam.side = sides[n];
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
        return expParams;
    }

}
