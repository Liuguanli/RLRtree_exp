package com.unimelb.cis.rlrtree.exp;

import com.unimelb.cis.node.Point;
import com.unimelb.cis.rlrtree.*;
import com.unimelb.cis.structures.IRtree;
import com.unimelb.cis.structures.recursivemodel.RecursiveModelRtree;
import com.unimelb.cis.utils.ExpReturn;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class ModelExperiment {

    public void generateDataSet(ExpParam param) {
        ExpExecuter executer = new ExpExecuter.PythonCommandBuilder()
                .buildPythonFile(param.dataGeneratorPython)
                .buildDistribution(param.distribution)
                .buildDatasetSize(param.size)
                .buildSkewness(param.skewness)
                .buildDim(param.dim)
                .buildInputFile(param.getInputFile())
                .build();
        File file = new File(param.getInputFile());
        if (file.exists()) {
            System.out.println("file exists");
            buildRtree(param);
            return;
        }
        executer.executePythonCommand(executer.getDatasetCommand(), new Callback() {
            @Override
            public void onFinish() {
                System.err.println("Data set generated: " + param.getInputFile());
                buildRtree(param);
            }

            @Override
            public void onError() {
            }
        });
    }

    public IRtree buildRtree(ExpParam param) {

        ExpExecuter executer = new ExpExecuter.RtreeBuilder()
                .buildCurve(param.curve)
                .buildInputFile(param.getInputFile())
                .buildPagesize(param.pagesizeBeforetuning)
                .buildType(param.treeType)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildThreshold(param.threshold)
                .build();

        return executer.buildMLRtree(new RtreeFinishCallback() {
            @Override
            public void onFinish(IRtree rtree) {
                System.err.println("Rtree build Finish");
                exp(rtree, param);
            }

            @Override
            public void onError(IRtree rtree) {

            }
        });
    }

    public void exp(IRtree rtree, ExpParam param) {
        System.out.println("exp");
        ExpExecuter executerPoint = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_POINT_ML)
                .buildWindows(param.sides).build();
        executerPoint.executePointQuery(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Point Query Finish");
            }

            @Override
            public void onError() {
                System.out.println("Point Query Finish");
            }
        });

        ExpExecuter executerWindow = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_WINDOW_ML)
                .buildWindows(param.sides).build();
        executerWindow.executeWindowQuery(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Window Query Finish");
            }

            @Override
            public void onError() {
                System.out.println("Window Query Finish");
            }
        });

        ExpExecuter executerkNN = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_KNN_ML)
                .buildKs(param.ks)
                .build();

        ExpExecuter executerInsert = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.INSERT_ML)
                .buildPointsNum(param.insertedNum)
                .build();
        executerInsert.executeInsert(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Insert Finish");
            }

            @Override
            public void onError() {
                System.out.println("Insert Finish");
            }
        });
    }


    public void exp() {
        List<ExpParam> params = new ExpParamBuilder()
                .buildCurve("H", "Z")
                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
                .buildDataSetSize(1000000)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "skewed")
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1, 3, 5, 7, 9)
                .buildSkewness(1, 9)
                .buildTime(100)
                .buildThreshold(10000)
                .buildInsertedNum(10000)
//                .buildMLAlgorithm("MultilayerPerceptron")
                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildTypes("recursive", "partition")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();
        for (ExpParam param : params) {
            System.out.println(param);
            generateDataSet(param);
//            break;
        }
    }

    public static void main(String[] args) {
        new ModelExperiment().exp();
    }

}
