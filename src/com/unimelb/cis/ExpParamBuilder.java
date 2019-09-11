package com.unimelb.cis;

public class ExperimentBuilder {

    /**
     * page size
     * algorithm
     * iteration time
     */
    int pagesizeBeforetuning = 100;
    int pagesizeAftertuning = 104;
    String algorithm = "random";
    int times = 100;
    int dim = 2;

    /**
     * file template
     */
    static String inputFileTemplate = "/Users/guanli/Documents/datasets/RLRtree/raw/%s_%d_%d_.csv";
    static String outputFileTemplate = "/Users/guanli/Documents/datasets/RLRtree/trees/Z_%s_%d_%d.csv";
    static String outputFileRLTemplate = "/Users/guanli/Documents/datasets/RLRtree/newtrees/Z_%s_%d_%d_%s.csv";

    /**
     * python file
     */
    String dataGeneratorPython = "/Users/guanli/Documents/codes/DL_Rtree/structure/data_generator.py";
    String pythonFile = "/Users/guanli/Documents/codes/DL_Rtree/structure/rtree.py";

    String distribution = "normal";
    int size = 1000000;
    int skewness = 1;

    String curve = "";

}
