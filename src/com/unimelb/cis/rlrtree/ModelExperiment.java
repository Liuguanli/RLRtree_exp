package com.unimelb.cis.rlrtree;

import com.unimelb.cis.structures.IRtree;

import java.io.File;
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
                pointQuery(rtree, param);
//                windowQuery(rtree, param);
            }

            @Override
            public void onError(IRtree rtree) {

            }
        });
    }

    public void windowQuery(IRtree rtree, ExpParam param) {
        System.out.println("windowQuery");
//        System.out.println(rtree.windowQuery(new Mbr(0.5f, 0.5f, 0.6f, 0.6f)));
        ExpExecuter executer = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_WINDOW_ML)
                .buildWindows(param.side).build();
        executer.executeWindowQuery(new Callback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {
                System.out.println("Window Query Finish");
            }
        });
    }

    public void pointQuery(IRtree rtree, ExpParam param) {
        System.out.println("pointQuery");
        ExpExecuter executer = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_POINT_ML)
                .buildWindows(param.side).build();
        executer.executePointQuery(new Callback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {
                System.out.println("Window Query Finish");
            }
        });
    }


    public void exp() {
        List<ExpParam> params = new ExpParamBuilder()
                .buildAlgorithm("DQN", "random")
                .buildCurve("H")
                .buildDataSetSize(10000, 20000, 40000, 80000, 160000)
                .buildDim(2, 3)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "skewed")
                .buildSides(0.2f, 0.1f, 0.05f, 0.025f, 0.0125f)
                .buildSkewness(1, 3, 5, 7, 9)
                .buildTime(100)
                .buildThreshold(10000)
//                .buildMLAlgorithm("MultilayerPerceptron")
                .buildMLAlgorithm("LinearRegression") // time=211635175  //pageaccess=19866 //windowQuery //time=12758763 //pageaccess=2
                .buildTypes("partition")
                .buildPageSizeAfterTuning(100)
                .buildExpParams();
        for (ExpParam param : params) {
            generateDataSet(param);
            break;
        }
    }

    public static void main(String[] args) {
        new ModelExperiment().exp();
    }

}
