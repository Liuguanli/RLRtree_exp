package com.unimelb.cis.rlrtree;

import com.unimelb.cis.structures.IRtree;

import java.io.File;
import java.util.List;


public class Experiment {

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
                .buildType(param.curve)
                .buildInputFile(param.getInputFile())
                .buildOutputFile(param.getOutputFile())
                .buildPagesize(param.pagesizeBeforetuning)
                .build();

        return executer.buildRLRtree(new RtreeFinishCallback() {
            @Override
            public void onFinish(IRtree rtree) {
                System.err.println("Rtree build Finish");
                query(rtree, param);
                System.err.println("Rtree query Finish");
                executeRLAlgorithmsByPython(rtree.getLevel(), param);
            }

            @Override
            public void onError(IRtree rtree) {

            }
        });
    }

    public void executeRLAlgorithmsByPython(int level, ExpParam param) {
        ExpExecuter executer = new ExpExecuter.PythonCommandBuilder()
                .buildPythonFile(param.pythonFile)
                .buildInputFile(param.getOutputFile())
                .buildOutputFile(param.getOutputFileRL())
                .buildDim(param.dim)
                .buildLevel(level)
                .buildPagesize(param.pagesizeAftertuning)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildType(param.curve)
                .build();
        executer.executePythonCommand(executer.getRLCommand(), new Callback() {
            @Override
            public void onFinish() {
                System.err.println("RL Finish");
                query(executer.buildNewRtree(), param, true);
            }

            @Override
            public void onError() {
                System.out.println("RL error");
            }
        });
    }

    public void query(IRtree rtree, ExpParam param) {
        query(rtree, param, false);
    }

    public void query(IRtree rtree, ExpParam param, boolean buildIsAfterRL) {
        ExpExecuter executer = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildIsAfterRL(buildIsAfterRL).buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildQueryType(ExpParam.QUERUY_TYPE_WINDOW)
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

    public void drawFigures() {
        String command = "python /Users/guanli/Documents/codes/DL_Rtree/structure/draw_figures.py";
        ExpExecuter executer = new ExpExecuter.PythonCommandBuilder().build();
        executer.executePythonCommand(command, new Callback() {
            @Override
            public void onFinish() {
                System.out.println("draw finish!");
            }

            @Override
            public void onError() {

            }
        });
    }

    public void exp() {
        List<ExpParam> params = new ExpParamBuilder()
                .buildAlgorithm("DQN", "random")
                .buildCurve("Z", "H")
                .buildDataSetSize(160000, 20000, 40000, 80000, 10000)
//                .buildDim(2, 3)
                .buildDim(2)
                .buildDistribution("uniform", "normal", "skewed")
                .buildSides(0.2f, 0.1f, 0.05f, 0.025f, 0.0125f)
                .buildSkewness(1, 3, 5, 7, 9)
                .buildTime(100)
                .buildPageSizeBeforeTuning(100)
                .buildPageSizeAfterTuning(108)
                .buildExpParams();
        for (ExpParam param : params) {
            generateDataSet(param);
            break;
        }
    }

    // todo generate 100 rectangles with different shape. and write to file, them
    public static void main(String[] args) {

        new Experiment().exp();

//        String distribution = "normal";
//        int size = 1000000;
//        int skewness = 1;
//        String inputFile = String.format(inputFileTemplate, distribution, size, skewness);
//
//        new Experiment().generateDataSet(distribution, size, skewness, inputFile);

//        String inputFile = "/Users/guanli/Documents/datasets/RLRtree/raw/normal_2000000_.csv";
//        String outputFile = "/Users/guanli/Documents/datasets/RLRtree/trees/Z_uniform_1000000_1.csv";
//        String outputFileRL = "/Users/guanli/Documents/datasets/RLRtree/newtrees/Z_uniform_1000000_1_random.csv";
//        new Experiment().buildMLRtree(inputFile, outputFile, outputFileRL);

//        new Experiment().drawFigures();
//        String outputFile = String.format(outputFileTemplate, distribution, size, skewness);
//        String outputFileRL = String.format(outputFileRLTemplate, distribution, size, skewness, "random");
//        new Experiment().executeRLAlgorithmsByPython(2, 3, outputFile, outputFileRL);

//        ZRtree zRtree = new ZRtree(100);
//        zRtree.buildRtreeAfterTuning(outputFileRL, 2, 3);
//        zRtree.buildMLRtree(inputFile);
//        new Experiment().query(zRtree, outputFileRL, true);
    }

}