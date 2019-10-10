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
            System.out.println(file.getAbsoluteFile());
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
//                pointQuery(rtree, param);
//                windowQuery(rtree, param);
//                knnQuery(rtree, param);
//                insert(rtree, param);
            }

            @Override
            public void onError(IRtree rtree) {

            }
        });
    }

    public void pointQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerPoint = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile())
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
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
    }

    public void windowQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerWindow = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
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
    }

    public void knnQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerkNN = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildQueryType(ExpParam.QUERUY_TYPE_KNN_ML)
                .buildKs(param.ks)
                .build();

        executerkNN.executeKnnQuery(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("knn Finish");
            }

            @Override
            public void onError() {

            }
        });
    }

    public void insert(IRtree rtree, ExpParam param) {
        ExpExecuter executerInsert = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
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

        List<ExpParam> paramsRtree = new ExpParamBuilder()
                .buildCurve("H", "Z")
                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "skewed")
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1, 3, 5, 7, 9)
                .buildSkewness(1, 9)
                .buildTime(10)
                .buildThreshold(10000)
                .buildInsertedNum(10000)
                .buildTypes("H", "Z")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();
        for (ExpParam param : paramsRtree) {
//            System.out.println(param);
            generateDataSet(param);
        }

        List<ExpParam> params = new ExpParamBuilder()
                .buildCurve("H")
                .buildDataSetSize(64000000)
//                .buildDataSetSize(32000000, 64000000)
//                .buildDataSetSize(10000)
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
                .buildTypes("partition")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();
        for (ExpParam param : params) {
//            System.out.println(param);
            generateDataSet(param);
        }


        params = new ExpParamBuilder()
                .buildCurve("H")
                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
//                .buildDataSetSize(32000000, 64000000)
//                .buildDataSetSize(10000)
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
                .buildTypes("recursive")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();
        for (ExpParam param : params) {
//            System.out.println(param);
            generateDataSet(param);
        }
    }

    public void expInsert() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("H","Z")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000)
                .buildDataSetSize(16000000)
                .buildDim(2)
                .buildDistribution("skewed")
//                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildSides(0.04f)
//                .buildKs(1, 5, 25, 125, 625)
                .buildKs(25)
                .buildSkewness(1,3,5,7,9)
                .buildTime(10)
                .buildThreshold(10000)
//                .buildInsertedNum(10000)
              //  .buildMLAlgorithm("LinearRegression", "NaiveBayes")
//                .buildTypes("recursive", "partition")
                .buildTypes("H", "Z")
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
            windowQuery(rtree, expParam);
            knnQuery(rtree, expParam);
//            insert(rtree, expParam);
        }

        paramsRec = new ExpParamBuilder()
                .buildCurve("H")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000)
                .buildDataSetSize(16000000)
                .buildDim(2)
                .buildDistribution("skewed")
//                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildSides(0.04f)
//                .buildKs(1, 5, 25, 125, 625)
                .buildKs(25)
                .buildSkewness(1,3,5,7,9)
                .buildTime(10)
                .buildThreshold(10000)
//                .buildInsertedNum(10000)
                  .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildTypes("recursive", "partition")
//                .buildTypes("H", "Z")
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
            windowQuery(rtree, expParam);
            knnQuery(rtree, expParam);
//            insert(rtree, expParam);
        }

    }

    public static void main(String[] args) {
//        new ModelExperiment().exp();
        new ModelExperiment().expInsert();
    }

}
