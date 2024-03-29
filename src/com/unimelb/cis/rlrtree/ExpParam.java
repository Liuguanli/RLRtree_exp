package com.unimelb.cis.rlrtree;

import java.util.Arrays;
import java.util.List;
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
    public static final String ZRRtree = "ZR";
    public static final String HCurveRtree = "H";
    public static final String HRRtree = "HR";
    public static final String RStar = "Rstar";
    public static final String PartitionModelRtree = "partition";
    public static final String RecursiveModelRtree = "recursive";
    public static final String KMeans = "kmeans";
    public static final String RMI = "RMI";
    public static final String PartitionRecursive = "partitionrecursive";

    public static final int QUERUY_TYPE_POINT = 0;
    public static final int QUERUY_TYPE_WINDOW = 1;
    public static final int QUERUY_TYPE_POINT_ML = 2;
    public static final int QUERUY_TYPE_WINDOW_ML = 3;
    public static final int QUERUY_TYPE_KNN = 4;
    public static final int QUERUY_TYPE_KNN_ML = 5;
    public static final int INSERT = 6;
    public static final int INSERT_ML = 7;
    public static final int QUERUY_TYPE_ACCURATE_WINDOW = 8;
    public static final int QUERUY_TYPE_ACCURATE_WINDOW_ML = 9;
    public static final int QUERUY_TYPE_ACCURATE_KNN = 10;
    public static final int QUERUY_TYPE_ACCURATE_KNN_ML = 11;
    public static final int INSERT_POINT_QUERY_ML = 12;
    public static final int INSERT_WINDOW_QUERY_ML = 13;
    public static final int INSERT_KNN_QUERY_ML = 14;


    public static final int DELETE_ML = 15;
    public static final int DELETE_POINT_QUERY_ML = 16;
    public static final int DELETE_WINDOW_QUERY_ML = 17;
    public static final int DELETE_KNN_QUERY_ML = 18;



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
    public static String recordRootAccWindow = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\Accwindow\\" : "/Users/guanli/Dropbox/records/RLRtree/Accwindow/";
    public static String recordRootAccWindowML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\AccwindowML\\" : "/Users/guanli/Dropbox/records/RLRtree/AccwindowML/";
    public static String recordRootKnn = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\knn\\" : "/Users/guanli/Dropbox/records/RLRtree/knn/";
    public static String recordRootKnnML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\knnML\\" : "/Users/guanli/Dropbox/records/RLRtree/knnML/";
    public static String recordRootAccKnn = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\Accknn\\" : "/Users/guanli/Dropbox/records/RLRtree/Accknn/";
    public static String recordRootAccKnnML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\AccknnML\\" : "/Users/guanli/Dropbox/records/RLRtree/AccknnML/";
    public static String recordRootInsert = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insert\\" : "/Users/guanli/Dropbox/records/RLRtree/insert/";
    public static String recordRootInsertML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insertML\\" : "/Users/guanli/Dropbox/records/RLRtree/insertML/";
    public static String recordRootInsertPointQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insertPointqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/insertPointqueryML/";
    public static String recordRootInsertWindowQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insertWindowqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/insertWindowqueryML/";
    public static String recordRootInsertKnnQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\insertKnnqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/insertKnnqueryML/";
    public static String recordRootDeleteML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\deleteML\\" : "/Users/guanli/Dropbox/records/RLRtree/deleteML/";
    public static String recordRootDeletePointQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\deletePointqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/deletePointqueryML/";
    public static String recordRootDeleteWindowQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\deleteWindowqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/deleteWindowqueryML/";
    public static String recordRootDeleteKnnQueryML = isWindows ? "C:\\Users\\Leo\\Dropbox\\records\\RLRtree\\deleteKnnqueryML\\" : "/Users/guanli/Dropbox/records/RLRtree/deleteKnnqueryML/";

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
    public int[] insertedNums;
    public int[] deleteNums;
    public int stages;

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
                ", insertedNum=" + Arrays.toString(insertedNums) +
                ", deleteNums=" + Arrays.toString(deleteNums) +
                '}';
    }
}
