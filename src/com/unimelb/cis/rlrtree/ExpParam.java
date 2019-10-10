package com.unimelb.cis.rlrtree;

import java.util.Arrays;
import java.util.Objects;

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
    public static final int QUERUY_TYPE_KNN = 4;
    public static final int QUERUY_TYPE_KNN_ML = 5;
    public static final int INSERT = 6;
    public static final int INSERT_ML = 7;


    /**
     * file template
     * distribution, size, skewness, dim
     * curve, distribution, size, skewness, dim
     * curve, distribution, size, skewness, dim, rlAlgorithm
     */

    public static String recordRootPoint = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\point\\" : "/Users/guanli/Dropbox/records/RLRtree/point/";
    public static String recordRootPointML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\pointML\\" : "/Users/guanli/Dropbox/records/RLRtree/pointML/";
    public static String recordRootWindow = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\window\\" : "/Users/guanli/Dropbox/records/RLRtree/window/";
    public static String recordRootWindowML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\windowML\\" : "/Users/guanli/Dropbox/records/RLRtree/windowML/";
    public static String recordRootKnn = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\knn\\" : "/Users/guanli/Dropbox/records/RLRtree/knn/";
    public static String recordRootKnnML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\knnML\\" : "/Users/guanli/Dropbox/records/RLRtree/knnML/";
    public static String recordRootInsert = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insert\\" : "/Users/guanli/Dropbox/records/RLRtree/insert/";
    public static String recordRootInsertML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insertML\\" : "/Users/guanli/Dropbox/records/RLRtree/insertML/";

    static String inputFileTemplate = isWindows ? "D:\\datasets\\RLRtree\\raw\\%s_%d_%d_%d_.csv" : "/Users/guanli/Documents/datasets/RLRtree/raw/%s_%d_%d_%d_.csv";
    static String outputFileTemplate = isWindows ? "D:\\datasets\\RLRtree\\trees\\%s_%s_%d_%d_%d_.csv" : "/Users/guanli/Documents/datasets/RLRtree/trees/%s_%s_%d_%d_%d_.csv";
    static String outputFileRLTemplate = isWindows ? "D:\\datasets\\RLRtree\\newtrees\\%s_%s_%d_%d_%d_%s.csv" : "/Users/guanli/Documents/datasets/RLRtree/newtrees/%s_%s_%d_%d_%d_%s.csv";

    /**
     * python file
     */
    public String dataGeneratorPython = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\data_generator.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/data_generator.py";
    public String pythonFile = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\rtree.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/rtree.py";
    public static String drawFigures = isWindows ? "C:\\Users\\Leo\\Dropbox\\shared\\RLR-trees\\codes\\python\\RLRtree\\structure\\draw_figures.py" : "/Users/guanli/Dropbox/shared/RLR-trees/codes/python/RLRtree/structure/draw_figures.py";

    public static String separator = isWindows ? "\\\\" : "/";

    public String distribution = "uniform";
    public int size = 1000000;
    public int skewness = 1;
    public String rlAlgorithm = "random";
    public int pagesizeBeforetuning = 100;
    public int pagesizeAftertuning = 104;
    public int times = 100;
    public int dim = 2;
    public float[] sides;
    public int[] ks;
    public String curve = "";
    public String mlAlgorithm;
    public int threshold;
    public String treeType;
    public int insertedNum;

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
//                "dataGeneratorPython='" + dataGeneratorPython + '\'' +
//                ", pythonFile='" + pythonFile + '\'' +
                ", distribution='" + distribution + '\'' +
                ", size=" + size +
                ", skewness=" + skewness +
                ", rlAlgorithm='" + rlAlgorithm + '\'' +
                ", pagesizeBeforetuning=" + pagesizeBeforetuning +
                ", pagesizeAftertuning=" + pagesizeAftertuning +
                ", times=" + times +
                ", dim=" + dim +
                ", sides=" + Arrays.asList(sides) +
                ", ks=" + Arrays.asList(ks) +
                ", curve='" + curve + '\'' +
                ", mlAlgorithm='" + mlAlgorithm + '\'' +
                ", threshold=" + threshold +
                ", treeType='" + treeType + '\'' +
                ", insertedNum=" + insertedNum +
                '}';
    }
}
