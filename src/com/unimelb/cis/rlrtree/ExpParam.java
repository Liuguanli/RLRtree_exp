package com.unimelb.cis.rlrtree;

public class ExpParam {

    static boolean isWindows = false;
    static boolean isWarmUp = true;

    static {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            isWindows = true;
        }
    }

    public static String originalRtree = "null";
    public static final String ZCurveRtree = "Z";
    public static final String HCurveRtree = "H";
    public static final String PartitionModelRtree = "partition";
    public static final String RecursiveModelRtree = "recursive";

    public static final int QUERUY_TYPE_POINT = 0;
    public static final int QUERUY_TYPE_WINDOW = 1;
    public static final int QUERUY_TYPE_POINT_ML = 2;
    public static final int QUERUY_TYPE_WINDOW_ML = 3;

    /**
     * file template
     * distribution, size, skewness, dim
     * curve, distribution, size, skewness, dim
     * curve, distribution, size, skewness, dim, rlAlgorithm
     */

    public static String recordRootPoint = isWindows ?"C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\point\\" :"/Users/guanli/Dropbox/records/RLRtree/point/";
    public static String recordRootPointML = isWindows ?"C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\pointML\\" :"/Users/guanli/Dropbox/records/RLRtree/pointML/";
    public static String recordRootWindow = isWindows ?"C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\window\\" :"/Users/guanli/Dropbox/records/RLRtree/window/";
    public static String recordRootWindowML = isWindows ?"C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\windowML\\" :"/Users/guanli/Dropbox/records/RLRtree/windowML/";

    static String inputFileTemplate = isWindows ? "D:\\datasets\\RLRtree\\raw\\%s_%d_%d_%d_.csv" : "/Users/guanli/Documents/datasets/RLRtree/raw/%s_%d_%d_%d_.csv";
    static String outputFileTemplate = isWindows ? "D:\\datasets\\RLRtree\\trees\\%s_%s_%d_%d_%d_.csv" : "/Users/guanli/Documents/datasets/RLRtree/trees/%s_%s_%d_%d_%d_.csv";
    static String outputFileRLTemplate = isWindows ? "D:\\datasets\\RLRtree\\newtrees\\%s_%s_%d_%d_%d_%s.csv" : "/Users/guanli/Documents/datasets/RLRtree/newtrees/%s_%s_%d_%d_%d_%s.csv";

    /**
     * python file
     */
    String dataGeneratorPython = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\data_generator.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/data_generator.py";
    String pythonFile = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\rtree.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/rtree.py";
    static String drawFigures = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\draw_figures.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/draw_figures.py";

    static String separator = isWindows ? "\\\\" : "/";

    String distribution = "uniform";
    int size = 1000000;
    int skewness = 1;
    String rlAlgorithm = "random";
    public int pagesizeBeforetuning = 100;
    int pagesizeAftertuning = 104;
    int times = 100;
    int dim = 2;
    float side;
    public String curve = "";
    String mlAlgorithm;
    int threshold;
    String treeType;
    int queryType;

    public String getInputFile() {
        return String.format(inputFileTemplate, distribution, size, skewness, dim);
    }

    public String getOutputFile() {
        return String.format(outputFileTemplate, curve, distribution, size, skewness, dim);
    }

    public String getOutputFileRL() {
        return String.format(outputFileRLTemplate, curve, distribution, size, skewness, dim, rlAlgorithm);
    }

    @Override
    public String toString() {
        return "ExpParam{" +
                "distribution='" + distribution + '\'' +
                ", size=" + size +
                ", skewness=" + skewness +
                ", rlAlgorithm='" + rlAlgorithm + '\'' +
                ", pagesizeBeforetuning=" + pagesizeBeforetuning +
                ", pagesizeAftertuning=" + pagesizeAftertuning +
                ", times=" + times +
                ", dim=" + dim +
                ", side=" + side +
                ", curve='" + curve + '\'' +
                '}';
    }
}
