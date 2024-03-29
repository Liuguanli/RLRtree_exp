package com.unimelb.cis.rlrtree.exp;

import com.unimelb.cis.rlrtree.*;
import com.unimelb.cis.structures.IRtree;

import java.io.File;
import java.util.List;


public class RLExperiment {

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
            System.out.println("file exists:" +param.getInputFile());
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

        boolean isNotForRL = param.rlAlgorithm.equals("null");

        return executer.buildRtree(isNotForRL, new RtreeFinishCallback() {
            @Override
            public void onFinish(IRtree rtree) {
                System.err.println("Rtree build Finish");
                if (isNotForRL) {
                    exp(rtree, param);
                    System.err.println("Rtree exp Finish");
                    rtree.visualize(600, 600).save(param.curve + "_" + param.rlAlgorithm + ".png");
                } else {
                    executeRLAlgorithmsByPython(rtree.getLevel(), param);
                }
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
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildPagesize(param.pagesizeAftertuning)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildType(param.curve)
                .build();

        executer.executePythonCommand(executer.getRLCommand(), new Callback() {
            @Override
            public void onFinish() {
                System.err.println("RL Finish");
                IRtree rtree = executer.buildRLRtree();
//                rtree.visualize(600, 600).save(param.curve + "_" + param.rlAlgorithm + ".png");
                exp(rtree, param, true);
            }

            @Override
            public void onError() {
                System.out.println("RL error");
            }
        });
    }

    public void exp(IRtree rtree, ExpParam param) {
        exp(rtree, param, false);
    }

    public void exp(IRtree rtree, ExpParam param, boolean buildIsAfterRL) {
        ExpExecuter executerWindow = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
                .buildIsAfterRL(buildIsAfterRL).buildFileName(param.getOutputFile()).buildIteration(param.times)
                .buildRLAlgorithm(param.rlAlgorithm)
                .buildMLAlgorithm(param.mlAlgorithm)
                .buildTreeType(param.treeType)
                .buildQueryType(ExpParam.QUERUY_TYPE_WINDOW)
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

//        ExpExecuter executerPoint = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
//                .buildIsAfterRL(buildIsAfterRL).buildFileName(param.getOutputFile()).buildIteration(param.times)
//                .buildMLAlgorithm(param.mlAlgorithm)
//                .buildTreeType(param.treeType)
//                .buildRLAlgorithm(param.rlAlgorithm)
//                .buildQueryType(ExpParam.QUERUY_TYPE_POINT)
//                .build();
//        executerPoint.executePointQuery(new Callback() {
//            @Override
//            public void onFinish() {
//                System.out.println("Point Query Finish");
//            }
//
//            @Override
//            public void onError() {
//                System.out.println("Point Query Finish");
//            }
//        });

//        ExpExecuter executerInsert = new ExpExecuter.QueryBuilder().buildIRtree(rtree).buildDim(rtree.getDim())
//                .buildIsAfterRL(buildIsAfterRL).buildFileName(param.getOutputFile()).buildIteration(param.times)
//                .buildRLAlgorithm(param.rlAlgorithm)
//                .buildMLAlgorithm(param.mlAlgorithm)
//                .buildTreeType(param.treeType)
//                .buildQueryType(ExpParam.INSERT)
//                .buildPointsNum(param.insertedNum)
//                .build();
//        executerInsert.executeInsert(new Callback() {
//            @Override
//            public void onFinish() {
//                System.out.println("Insert Finish");
//            }
//
//            @Override
//            public void onError() {
//                System.out.println("Insert Finish");
//            }
//        });
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
                .buildAlgorithm("DQN", "null")
//                .buildAlgorithm("null", "DQN")
                .buildCurve("Z", "H")
//                .buildCurve("Z")
                .buildDataSetSize(10000)
//                .buildDataSetSize(8000000)
//                .buildDataSetSize(10000, 20000, 40000, 80000, 160000, 320000, 640000)
                .buildDim(2)
//                .buildDistribution("uniform", "normal", "skewed")
                .buildDistribution("uniform")
                .buildSides(0.01f, 0.02f, 0.04f, 0.08f, 0.16f)
//                .buildKs(1, 5, 25, 125, 625)
                .buildSkewness(1)
                .buildTime(100)
                .buildPageSizeBeforeTuning(80)
                .buildPageSizeAfterTuning(100)
//                .buildInsertedNum(10000)
                .buildExpParams();
        for (int i = 0; i < params.size(); i++) {
            System.out.println(params.get(i));
            generateDataSet(params.get(i));
            executeRLAlgorithmsByPython(3, params.get(i));
            break;
        }
//        for (ExpParam param : params) {
//            System.out.println(param);
//            generateDataSet(param);
//        }
    }

    // todo generate 100 rectangles with different shape. and write to file, them
    public static void main(String[] args) {

        new RLExperiment().exp();
//        new RLExperiment().drawFigures();
//        String distribution = "normal";
//        int size = 1000000;
//        int skewness = 1;
//        String inputFile = String.format(inputFileTemplate, distribution, size, skewness);
//
//        new RLExperiment().generateDataSet(distribution, size, skewness, inputFile);

//        String inputFile = "/Users/guanli/Documents/datasets/RLRtree/raw/normal_2000000_.csv";
//        String outputFile = "/Users/guanli/Documents/datasets/RLRtree/trees/Z_uniform_1000000_1.csv";
//        String outputFileRL = "/Users/guanli/Documents/datasets/RLRtree/newtrees/Z_uniform_1000000_1_random.csv";
//        new RLExperiment().buildMLRtree(inputFile, outputFile, outputFileRL);

//        new RLExperiment().drawFigures();
//        String outputFile = String.format(outputFileTemplate, distribution, size, skewness);
//        String outputFileRL = String.format(outputFileRLTemplate, distribution, size, skewness, "random");
//        new RLExperiment().executeRLAlgorithmsByPython(2, 3, outputFile, outputFileRL);

//        ZRtree zRtree = new ZRtree(100);
//        zRtree.buildRtreeAfterTuning(outputFileRL, 2, 3);
//        zRtree.buildMLRtree(inputFile);
//        new RLExperiment().exp(zRtree, outputFileRL, true);
    }

}