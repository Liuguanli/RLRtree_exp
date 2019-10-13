package com.unimelb.cis.rlrtree.exp;

import com.unimelb.cis.rlrtree.ExpParam;
import com.unimelb.cis.rlrtree.ExpParamBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ExperimentAll {

    public static void main(String[] args) {
//        new MLExperiment().exp();
//        new RLExperiment().exp();
//
        List<ExpParam> params = new ExpParamBuilder()
                .buildCurve("Z")
                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000)
//                .buildDataSetSize(64000000)
                .buildDim(2)
                .buildDistribution("uniform")
                .buildSkewness(1)
                .buildTime(1)
                .buildThreshold(10000)
                .buildInsertedNum(10000)
                .buildTypes("Z")
//                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
//                .buildTypes("recursive")
                .buildExpParams();

        params.forEach(expParam -> System.out.println(expParam));

        System.out.println();

        List<ExpParam> params1 = new ExpParamBuilder()
                .buildCurve("H")
                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000)
//                .buildDataSetSize(32000000, 64000000)
//                .buildDataSetSize(10000)
                .buildDim(2)
                .buildDistribution("uniform")
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1, 3, 5, 7, 9)
                .buildSkewness(1, 9)
                .buildTime(100)
                .buildThreshold(10000)
                .buildInsertedNum(10000)
//                .buildMLAlgorithm("MultilayerPerceptron")
                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildTypes("recursive")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();

        params1.forEach(expParam -> System.out.println(expParam));

    }




}
