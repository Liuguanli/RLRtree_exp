package com.unimelb.cis.rlrtree.exp;

import com.unimelb.cis.rlrtree.*;
import com.unimelb.cis.structures.IRtree;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.unimelb.cis.rlrtree.ExpParam.*;

public class MLExperiment {

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
//            buildRtree(param);
            return;
        }
        executer.executePythonCommand(executer.getDatasetCommand(), new Callback() {
            @Override
            public void onFinish() {
                System.err.println("Data set generated: " + param.getInputFile());
//                buildRtree(param);
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
                .buildStages(param.stages)
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
        ExpExecuter executerPoint = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile())
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildDim(param.dim)
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
        ExpExecuter executerWindow = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildQueryType(QUERUY_TYPE_WINDOW_ML)
                .buildDim(param.dim)
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

    public void accurateWindowQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerWindow = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildDim(param.dim)
                .buildQueryType(QUERUY_TYPE_ACCURATE_WINDOW_ML)
                .buildWindows(param.sides).build();
        executerWindow.executeAccurateWindowQuery(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Accurate Window Query Finish");
            }

            @Override
            public void onError() {
                System.out.println("Accurate Window Query Finish");
            }
        });
    }

    public void accurateKnnQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerkNN = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildDim(param.dim)
                .buildQueryType(ExpParam.QUERUY_TYPE_ACCURATE_KNN_ML)
                .buildKs(param.ks)
                .build();
        executerkNN.executeAccurateKnnQuery(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Accurate Window Query Finish");
            }

            @Override
            public void onError() {
                System.out.println("Accurate Window Query Finish");
            }
        });
    }

    public void knnQuery(IRtree rtree, ExpParam param) {
        ExpExecuter executerkNN = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildDim(param.dim)
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
        ExpExecuter executerInsert = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildQueryType(ExpParam.INSERT_ML)
                .buildPointsNum(param.insertedNums)
                .buildDim(param.dim)
                .build();
        executerInsert.executeInsert(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Insert Finish");
            }

            @Override
            public void onError() {
                System.err.println("Insert Error");
            }
        });
    }

    public void delete(IRtree rtree, ExpParam param) {
        ExpExecuter executerDelete = new ExpExecuter.QueryBuilder().buildIRtree(rtree)
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildStages(param.stages)
                .buildQueryType(ExpParam.DELETE_ML)
                .buildPointsNum(param.deleteNums)
                .buildDim(param.dim)
                .build();
        executerDelete.executeDelete(new Callback() {
            @Override
            public void onFinish() {
                System.out.println("Delete Finish");
            }

            @Override
            public void onError() {
                System.err.println("Delete Error");
            }
        });
    }

    public void expKmeans() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("H")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000)
                .buildDataSetSize(16000000)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "real")
                .buildSkewness(1)
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
                .buildTime(100)
                .buildThreshold(10000)
//                .buildInsertedNum(10000)
                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
//                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildMLAlgorithm("NaiveBayes")
                .buildTypes(KMeans)
//                .buildTypes("H", "Z")
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
            pointQuery(rtree, expParam);
            windowQuery(rtree, expParam);
            accurateWindowQuery(rtree, expParam);
            knnQuery(rtree, expParam);
            insert(rtree, expParam);
        }
    }

    public void expRMI() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("Z")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
//                .buildDataSetSize(5000000, 6000000, 7000000, 8000000)
                .buildDim(2)
                .buildDistribution("uniform")
//                .buildDistribution("uniform", "normal", "skewed", "real")
                .buildSkewness(9)
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1,3,5,7,9)
                .buildTime(100)
                .buildThreshold(10000)
//                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
                .buildInsertedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildDeletedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
//                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
//                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildMLAlgorithm("MultilayerPerceptron")
                .buildTypes(RMI)
                .buildStages(2, 3)
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            generateDataSet(expParam);
            IRtree rtree = buildRtree(expParam);
//            pointQuery(rtree, expParam);
//            windowQuery(rtree, expParam);
//            accurateWindowQuery(rtree, expParam);
//            knnQuery(rtree, expParam);
//            accurateKnnQuery(rtree, expParam);
            insert(rtree, expParam);
            rtree = buildRtree(expParam);
            delete(rtree, expParam);
        }
    }

    public void expTrees() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("H")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
                .buildDataSetSize(16000000)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "skewed", "real")
//                .buildDistribution("uniform")
                .buildSkewness(9)
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1,3,5,7,9)
                .buildTime(1000)
                .buildThreshold(10000)
//                .buildInsertedNum(10000)
//                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
                .buildInsertedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildDeletedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
//                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildTypes(RStar, HRRtree, HCurveRtree)
//                .buildTypes(HCurveRtree)
//                .buildTypes("H", "Z")
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
//            pointQuery(rtree, expParam);
//            windowQuery(rtree, expParam);
//            knnQuery(rtree, expParam);
            insert(rtree, expParam);
            rtree = buildRtree(expParam);
            delete(rtree, expParam);
        }
    }

    public void expPartition() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("H")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
                .buildDataSetSize(16000000)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "real")
                .buildSkewness(1)
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1, 5, 25, 125, 625)
//                .buildSkewness(1,3,5,7,9)
                .buildTime(100)
                .buildThreshold(10000)
//                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
                .buildInsertedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildDeletedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildMLAlgorithm("LinearRegression", "NaiveBayes")
                .buildTypes(PartitionModelRtree)
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
            pointQuery(rtree, expParam);
            windowQuery(rtree, expParam);
            accurateWindowQuery(rtree, expParam);
            knnQuery(rtree, expParam);
            accurateKnnQuery(rtree, expParam);
            insert(rtree, expParam);
        }
    }

    public void expPartitionRegression() {
        List<ExpParam> paramsRec = new ExpParamBuilder()
                .buildCurve("H")
//                .buildDataSetSize(1000000, 2000000, 4000000, 8000000, 16000000, 32000000, 64000000)
                .buildDataSetSize(1000000)
                .buildDim(2)
//                .buildDistribution("uniform", "normal", "skewed", "real")
                .buildDistribution("uniform")
                .buildSkewness(9)
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
                .buildKs(1000,2000,3000,4000,5000) //1, 5, 25, 125, 625,
//                .buildSkewness(1,3,5,7,9)
                .buildTime(100)
                .buildThreshold(10000)
//                .buildInsertedNum(10000)
//                .buildInsertedNum(10000, 20000, 40000, 80000, 160000)
                .buildInsertedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildDeletedNum(0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                .buildTypes(PartitionRecursive)
//                .buildTypes("H", "Z")
                .buildExpParams();

        for (int i = 0; i < paramsRec.size(); i++) {
            ExpParam expParam = paramsRec.get(i);
            System.out.println(expParam);
            IRtree rtree = buildRtree(expParam);
//            pointQuery(rtree, expParam);
//            windowQuery(rtree, expParam);
//            accurateWindowQuery(rtree, expParam);
            knnQuery(rtree, expParam);
//            accurateKnnQuery(rtree, expParam);
//            insert(rtree, expParam);
//            rtree = buildRtree(expParam);
//            delete(rtree, expParam);
        }
    }

    public static void main(String[] args) {
        new MLExperiment().expPartitionRegression();
//        new MLExperiment().expRMI();
//        new MLExperiment().expTrees();
//        new MLExperiment().expPartition();
//        new MLExperiment().expKmeans();
    }

}
