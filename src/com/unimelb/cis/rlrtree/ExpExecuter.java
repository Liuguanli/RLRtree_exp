package com.unimelb.cis.rlrtree;

import com.unimelb.cis.geometry.Mbr;
import com.unimelb.cis.node.Point;
import com.unimelb.cis.structures.IRtree;
import com.unimelb.cis.structures.RLRtree;
import com.unimelb.cis.structures.hrtree.HRRtree;
import com.unimelb.cis.structures.hrtree.HRtree;
import com.unimelb.cis.structures.partitionmodel.PartitionModelRtree;
import com.unimelb.cis.structures.partitionmodel.RecursivePartition;
import com.unimelb.cis.structures.recursivemodel.OriginalRecursiveModel;
import com.unimelb.cis.structures.recursivemodel.RecursiveModelRtree;
import com.unimelb.cis.structures.rstar.RstarTree;
import com.unimelb.cis.structures.unsupervisedPartition.UnsupervisedPartitionModel;
import com.unimelb.cis.structures.zrtree.ZRRtree;
import com.unimelb.cis.structures.zrtree.ZRtree;
import com.unimelb.cis.utils.ExpReturn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.unimelb.cis.rlrtree.ExpParam.*;

public class ExpExecuter {

    private int[] ks;
    private float[] sides;
    private int[] insertedPoints;

    private int iteration = 1;

    private String inputFile;
    private String outputFile;
    private String pythonFile;
    private int dim;
    private int level;
    private int pagesize;

    private IRtree rtree;
    private String rlAgorithm;

    private String distribution;
    private int datasetSize;
    private int skewness;

    private boolean isAfterRL;
    private String treeType;

    private String curve;
    private int threshold;
    private String mlAlgorithm;

    private int queryType;
    private float sideForEachMbr;
    private int k;
    private int insertedNum;
    private int deletedNum;

    private int stages;

    public ExpExecuter(ExpExecuter.QueryBuilder builder) {
        this.ks = builder.ks;
        this.sides = builder.sides;
        this.iteration = builder.iteration;
        this.rtree = builder.rtree;
        this.dim = builder.dim;
        this.outputFile = builder.fileName;
        this.isAfterRL = builder.isAfterRL;
        this.rlAgorithm = builder.rlAlgorithm;
        this.mlAlgorithm = builder.mlAlgorithm;
        this.threshold = builder.threshold;
        this.queryType = builder.queryType;
        this.insertedPoints = builder.insertedPoints;
        this.treeType = builder.treeType;
        this.stages = builder.stages;
    }

    public ExpExecuter(ExpExecuter.PythonCommandBuilder builder) {
        this.dim = builder.dim;
        this.inputFile = builder.inputFile;
        this.outputFile = builder.outputFile;
        this.level = builder.level;
        this.pagesize = builder.pagesize;
        this.pythonFile = builder.pythonFile;
        this.rlAgorithm = builder.rlAlgorithm;

        this.datasetSize = builder.datasetSize;
        this.distribution = builder.distribution;
        this.skewness = builder.skewness;
        this.treeType = builder.type;
        this.mlAlgorithm = builder.mlAlgorithm;
        this.threshold = builder.threshold;
    }

    public ExpExecuter(ExpExecuter.RtreeBuilder builder) {
        this.inputFile = builder.inputFile;
        this.outputFile = builder.outputFile;
        this.pagesize = builder.pagesize;
        this.treeType = builder.type;
        this.mlAlgorithm = builder.mlAlgorithm;
        this.threshold = builder.threshold;
        this.curve = builder.curve;
        this.stages = builder.stages;
    }

    public RLRtree buildRtree(boolean isNotForRL, RtreeFinishCallback callback) {
        RLRtree rtree;
        switch (treeType) {
            case ZCurveRtree:
                rtree = new ZRtree(pagesize);
                break;
            case ZRRtree:
                rtree = new ZRRtree(pagesize);
                break;
            case HCurveRtree:
                rtree = new HRtree(pagesize);
                break;
            case HRRtree:
                rtree = new HRRtree(pagesize);
                break;
            case RStar:
                rtree = new RstarTree(pagesize, true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + treeType);
        }
        rtree.buildRtree(inputFile);
        System.out.println("buildRtree:" + outputFile);
        if (!isNotForRL) {
            File file = new File(outputFile);
            if (file.exists()) {
                System.out.println("file exists:" + outputFile);
            } else {
                rtree.output(outputFile);
            }
        }
        callback.onFinish(rtree);
        return rtree;
    }

    public IRtree buildMLRtreeByRL(RtreeFinishCallback callback) {
        IRtree rtree;
        switch (treeType) {
            case ExpParam.PartitionModelRtree:
                rtree = new PartitionModelRtree(threshold, treeType, pagesize, mlAlgorithm);
                break;
            case ExpParam.RecursiveModelRtree:
                rtree = new RecursiveModelRtree(threshold, treeType, pagesize, mlAlgorithm);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + treeType);
        }
//        rtree.buildRtree()
//        rtree.buildRtree(inputFile);
//        callback.onFinish(rtree);
        return rtree;
    }

    public IRtree buildMLRtree(RtreeFinishCallback callback) {
        IRtree rtree;
        switch (treeType) {
            case ZCurveRtree:
                rtree = new ZRtree(pagesize);
                break;
            case ZRRtree:
                rtree = new ZRRtree(pagesize);
                break;
            case HCurveRtree:
                rtree = new HRtree(pagesize);
                break;
            case HRRtree:
                rtree = new HRRtree(pagesize);
                break;
            case RStar:
                rtree = new RstarTree(pagesize, true);
                break;
            case PartitionModelRtree:
                rtree = new PartitionModelRtree(threshold, curve, pagesize, mlAlgorithm);
                break;
            case RecursiveModelRtree:
                rtree = new RecursiveModelRtree(threshold, curve, pagesize, mlAlgorithm);
                break;
            case KMeans:
                rtree = new UnsupervisedPartitionModel(threshold, curve, pagesize, mlAlgorithm, 10);
                break;
            case RMI:
                rtree = new OriginalRecursiveModel(pagesize, false, "Z", stages);
                break;
            case PartitionRecursive:
                int maxPartitionNumEachDim = 16;
                rtree = new RecursivePartition(curve, maxPartitionNumEachDim, 5000, "MultilayerPerceptron");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + treeType);
        }
        rtree.buildRtree(inputFile);
        callback.onFinish(rtree);
        return rtree;
    }

    public RLRtree buildRLRtree() {
        RLRtree rtree;
        switch (treeType) {
            case ZRRtree:
            case ZCurveRtree:
                rtree = new ZRRtree(pagesize);
                break;
            case HRRtree:
            case HCurveRtree:
                rtree = new HRRtree(pagesize);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + treeType);
        }
        rtree.buildRtreeAfterTuning(outputFile, dim, level);
        return rtree;
    }

    /**
     * "python /Users/guanli/Documents/codes/DL_Rtree/structure/rtree.py -l 3
     * -i /Users/guanli/Documents/datasets/RLRtree/trees/Z_normal_2000000_.csv
     * -o /Users/guanli/Documents/datasets/RLRtree/newtrees/Z_normal_2000000_new.csv
     * -d 2 -p 102"
     *
     * @return
     */
    public String getRLCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("python ").append(pythonFile)
                .append(" -l ").append(level)
                .append(" -i ").append(inputFile)
                .append(" -o ").append(outputFile)
                .append(" -d ").append(dim)
                .append(" -p ").append(pagesize)
                .append(" -a ").append(rlAgorithm);
        return stringBuilder.toString();
    }

    public String getDatasetCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("python ").append(pythonFile)
                .append(" -d ").append(distribution)
                .append(" -s ").append(datasetSize)
                .append(" -n ").append(skewness)
                .append(" -f ").append(inputFile)
                .append(" -m ").append(dim);
        return stringBuilder.toString();
    }

    public void executePythonCommand(String command, Callback callback) {
        if (outputFile != null) {
            File file = new File(outputFile);
            if (file.exists()) {
                System.out.println("file exists:" + outputFile);
                callback.onFinish();
                return;
            }
        }
        System.err.println(command);
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(command);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.err.println("from python:" + line);
            }
            in.close();
            proc.waitFor();
            callback.onFinish();
        } catch (IOException e) {
            callback.onError();
            e.printStackTrace();
        } catch (InterruptedException e) {
            callback.onError();
            e.printStackTrace();
        }
    }

    public String getRecordFileName() {
        String file = outputFile;
        Path path = Paths.get(outputFile);
        String fileName = path.getFileName().toString();
//        String[] names = file.split(File.separator);
        String name = fileName.split("\\.")[0];
//        DecimalFormat df = new DecimalFormat("#.0000");
        StringBuilder nameBuilder = new StringBuilder();
        String prefix = null;
        String tag = "";
        switch (queryType) {
            case QUERUY_TYPE_POINT:
                prefix = recordRootPoint;
                break;
            case QUERUY_TYPE_POINT_ML:
                prefix = recordRootPointML;
                break;
            case QUERUY_TYPE_WINDOW:
                tag = "" + (int) (sideForEachMbr * sideForEachMbr * 1000000);
                prefix = recordRootWindow;
                break;
            case QUERUY_TYPE_WINDOW_ML:
                tag = "" + (int) (sideForEachMbr * sideForEachMbr * 1000000);
                prefix = recordRootWindowML;
                break;
            case QUERUY_TYPE_ACCURATE_WINDOW:
                tag = "" + (int) (sideForEachMbr * sideForEachMbr * 1000000);
                prefix = recordRootAccWindow;
                break;
            case QUERUY_TYPE_ACCURATE_WINDOW_ML:
                tag = "" + (int) (sideForEachMbr * sideForEachMbr * 1000000);
                prefix = recordRootAccWindowML;
                break;
            case QUERUY_TYPE_KNN:
                tag = "" + k;
                prefix = recordRootKnn;
                break;
            case QUERUY_TYPE_KNN_ML:
                tag = "" + k;
                prefix = recordRootKnnML;
                break;
            case QUERUY_TYPE_ACCURATE_KNN:
                tag = "" + k;
                prefix = recordRootAccKnn;
                break;
            case QUERUY_TYPE_ACCURATE_KNN_ML:
                tag = "" + k;
                prefix = recordRootAccKnnML;
                break;
            case INSERT:
                tag = "" + insertedNum;
                prefix = recordRootInsert;
                break;
            case INSERT_ML:
                tag = "" + insertedNum;
                prefix = recordRootInsertML;
                break;
            case INSERT_POINT_QUERY_ML:
                tag = "" + insertedNum;
                prefix = recordRootInsertPointQueryML;
                break;
            case INSERT_WINDOW_QUERY_ML:
                tag = "" + insertedNum;
                prefix = recordRootInsertWindowQueryML;
                break;
            case INSERT_KNN_QUERY_ML:
                tag = "" + insertedNum;
                prefix = recordRootInsertKnnQueryML;
                break;
            case DELETE_ML:
                tag = "" + deletedNum;
                prefix = recordRootDeleteML;
                break;
            case DELETE_POINT_QUERY_ML:
                tag = "" + deletedNum;
                prefix = recordRootDeletePointQueryML;
                break;
            case DELETE_WINDOW_QUERY_ML:
                tag = "" + deletedNum;
                prefix = recordRootDeleteWindowQueryML;
                break;
            case DELETE_KNN_QUERY_ML:
                tag = "" + deletedNum;
                prefix = recordRootDeleteKnnQueryML;
                break;
        }
        nameBuilder.append(prefix).append(name);
        if (!isAfterRL) {
            nameBuilder.append(originalRtree);
        } else {
            nameBuilder.append(rlAgorithm);
        }
        nameBuilder.append("_").append(treeType);
        if (treeType.equals(RMI)) {
            nameBuilder.append(stages);
        }
        nameBuilder.append("_").append(mlAlgorithm);
        nameBuilder.append("_").append(tag).append(".txt");
        return nameBuilder.toString();
    }

    public void executeDelete(Callback callback) {
        ExpReturn expReturn = new ExpReturn();
        int sum = 0;
        int oldQueryType = queryType;
        int defaultTime = 1000;
        for (int i = 0; i < insertedPoints.length; i++) {
            List<Point> points = rtree.getPoints(insertedPoints[i]);
            points = points.subList(sum, insertedPoints[i]);
            sum = insertedPoints[i];


            ExpReturn temp = rtree.delete(points);
            expReturn.time += temp.time;
            System.out.println("deleted points:" + points.size() + "time:" + temp.time);
            expReturn.pageaccess += temp.pageaccess;
            deletedNum = insertedPoints[i];
            String name = getRecordFileName();
            System.out.println(name);
            FileRecoder.write(name, expReturn.toString());

            // point query after deletion
            queryType = DELETE_POINT_QUERY_ML;
            ExpReturn pointExpReturn = rtree.pointQuery(rtree.getPoints());
            name = getRecordFileName();
            System.out.println("query point size:" + rtree.getPoints().size());
            FileRecoder.write(name, pointExpReturn.toString());

            // window query after deletion
            queryType = DELETE_WINDOW_QUERY_ML;
            List<Mbr> mbrs = Mbr.getMbrs(rtree.getPoints(), 0.01f, defaultTime, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            mbrs.forEach(mbr -> expResultHelper.addReturn(rtree.windowQuery(mbr)));
            ExpReturn windowExpReturn = expResultHelper.getResult();
            name = getRecordFileName();
//            System.out.println("window:" + name);
            FileRecoder.write(name, windowExpReturn.toString());

            // knn query after deletion
            queryType = DELETE_KNN_QUERY_ML;
            List<Point> knnQueryPoints = Point.getPoints(rtree.getPoints(), defaultTime, dim);
            ExpResultHelper kNNExpResultHelper = new ExpResultHelper();
            knnQueryPoints.forEach(point -> kNNExpResultHelper.addReturn(rtree.knnQuery(point, 25)));
            ExpReturn kNNExpReturn = kNNExpResultHelper.getResult();
            name = getRecordFileName();
//            System.out.println("knn:" + name);
            FileRecoder.write(name, kNNExpReturn.toString());

            queryType = oldQueryType;


        }
        callback.onFinish();
    }

    public void executeInsert(Callback callback) {
        ExpReturn expReturn = new ExpReturn();
        int sum = 0;
        int oldQueryType = queryType;
        int defaultTime = 1000;
        for (int i = 0; i < insertedPoints.length; i++) {
            List<Point> points = Point.getPoints(insertedPoints[i], dim);
            points = points.subList(sum, insertedPoints[i]);
            sum = insertedPoints[i];
            ExpReturn temp = rtree.insertByLink(points);
            expReturn.time += temp.time;
            expReturn.pageaccess += temp.pageaccess;
            insertedNum = insertedPoints[i];
            String name = getRecordFileName();
            System.out.println(name);
            FileRecoder.write(name, expReturn.toString());

            // point query after insertion
            queryType = INSERT_POINT_QUERY_ML;
            ExpReturn pointExpReturn = rtree.pointQuery(rtree.getPoints());
            name = getRecordFileName();
            System.out.println("query point size:" + rtree.getPoints().size());
            FileRecoder.write(name, pointExpReturn.toString());

            // window query after insertion
            queryType = INSERT_WINDOW_QUERY_ML;
            List<Mbr> mbrs = Mbr.getMbrs(rtree.getPoints(), 0.01f, defaultTime, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            mbrs.forEach(mbr -> expResultHelper.addReturn(rtree.windowQuery(mbr)));
            ExpReturn windowExpReturn = expResultHelper.getResult();
            name = getRecordFileName();
//            System.out.println("window:" + name);
            FileRecoder.write(name, windowExpReturn.toString());

            // knn query after insertion
            queryType = INSERT_KNN_QUERY_ML;
            List<Point> knnQueryPoints = Point.getPoints(rtree.getPoints(), defaultTime, dim);
            ExpResultHelper kNNExpResultHelper = new ExpResultHelper();
            knnQueryPoints.forEach(point -> kNNExpResultHelper.addReturn(rtree.knnQuery(point, 25)));
            ExpReturn kNNExpReturn = kNNExpResultHelper.getResult();
            name = getRecordFileName();
//            System.out.println("knn:" + name);
            FileRecoder.write(name, kNNExpReturn.toString());

            queryType = oldQueryType;
        }
        callback.onFinish();
    }

    public void executePointQuery(Callback callback) {
        ExpResultHelper expResultHelper = new ExpResultHelper();
        for (int i = 0; i < iteration; i++) {
            expResultHelper.addReturn(rtree.pointQuery(rtree.getPoints()));
        }
        ExpReturn expReturn = expResultHelper.getResult();
        String name = getRecordFileName();
        FileRecoder.write(name, expReturn.toString());
        callback.onFinish();
    }

    public void executeWindowQuery(Callback callback) {
        for (int i = 0; i < sides.length; i++) {
            List<Mbr> mbrs = Mbr.getMbrs(rtree.getPoints(), sides[i], iteration, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            mbrs.forEach(mbr -> expResultHelper.addReturn(rtree.windowQuery(mbr)));
            ExpReturn expReturn = expResultHelper.getResult();
            sideForEachMbr = sides[i];
            String name = getRecordFileName();
            System.out.println(name);
            System.out.println("executeWindowQuery:" + expReturn);
            FileRecoder.write(name, expReturn.toString());
        }
        callback.onFinish();
    }

    public void executeAccurateWindowQuery(Callback callback) {
        for (int i = 0; i < sides.length; i++) {
            List<Mbr> mbrs = Mbr.getMbrs(rtree.getPoints(), sides[i], iteration, dim);
            ExpReturn expReturn = rtree.windowQueryByScanAll(mbrs);
            sideForEachMbr = sides[i];
            String name = getRecordFileName();
            System.out.println(name);
            System.out.println("executeWindowQuery:" + expReturn);
            FileRecoder.write(name, expReturn.toString());
        }
        callback.onFinish();
    }

    public void executeKnnQuery(Callback callback) {
        for (int i = 0; i < ks.length; i++) {
            List<Point> points = Point.getPoints(rtree.getPoints(), iteration, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            k = ks[i];
            points.forEach(point -> expResultHelper.addReturn(rtree.knnQuery(point, k)));
            ExpReturn expReturn = expResultHelper.getResult();
            String name = getRecordFileName();
            System.out.println(name);
            FileRecoder.write(name, expReturn.toString());
        }
        callback.onFinish();
    }

    public void executeAccurateKnnQuery(Callback callback) {
        for (int i = 0; i < ks.length; i++) {
            List<Point> points = Point.getPoints(rtree.getPoints(), iteration, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            k = ks[i];

            if (rtree instanceof OriginalRecursiveModel) {
                points.forEach(point -> expResultHelper.addReturn(((OriginalRecursiveModel) rtree).accurateKnnQuery(point, k)));
            }
            if (rtree instanceof RecursivePartition) {
                points.forEach(point -> expResultHelper.addReturn(((RecursivePartition) rtree).accurateKnnQuery(point, k)));
            }
            ExpReturn expReturn = expResultHelper.getResult();
            String name = getRecordFileName();
            System.out.println(name);
            FileRecoder.write(name, expReturn.toString());
        }
        callback.onFinish();
    }


    public static class RtreeBuilder {

        public ExpExecuter.RtreeBuilder buildInputFile(String file) {
            this.inputFile = file;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildOutputFile(String file) {
            this.outputFile = file;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildPagesize(int pagesize) {
            this.pagesize = pagesize;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildType(String type) {
            this.type = type;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildCurve(String curve) {
            this.curve = curve;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildThreshold(int threshold) {
            this.threshold = threshold;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildMLAlgorithm(String mlAlgorithm) {
            this.mlAlgorithm = mlAlgorithm;
            return this;
        }

        public ExpExecuter.RtreeBuilder buildStages(int stages) {
            this.stages = stages;
            return this;
        }

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }


        private int pagesize;
        private String inputFile;
        private String outputFile;
        private String type;
        private String mlAlgorithm;
        private int threshold;
        private String curve;
        private int stages;

    }

    public static class PythonCommandBuilder {
        public ExpExecuter.PythonCommandBuilder buildInputFile(String file) {
            this.inputFile = file;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildOutputFile(String file) {
            this.outputFile = file;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildPythonFile(String file) {
            this.pythonFile = file;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildPagesize(int pagesize) {
            this.pagesize = pagesize;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildLevel(int level) {
            this.level = level;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildDim(int dim) {
            this.dim = dim;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildRLAlgorithm(String rlAlgorithm) {
            this.rlAlgorithm = rlAlgorithm;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildDistribution(String distribution) {
            this.distribution = distribution;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildDatasetSize(int datasetSize) {
            this.datasetSize = datasetSize;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildSkewness(int skewness) {
            this.skewness = skewness;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildType(String type) {
            this.type = type;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildThreshold(int threshold) {
            this.threshold = threshold;
            return this;
        }

        public ExpExecuter.PythonCommandBuilder buildMLAlgorithm(String mlAlgorithm) {
            this.mlAlgorithm = mlAlgorithm;
            return this;
        }

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }

        private String inputFile;
        private String outputFile;
        private String pythonFile;
        private int dim;
        private int level;
        private int pagesize;
        private String rlAlgorithm;

        private String distribution;
        private int datasetSize;
        private int skewness;

        private String type;
        private String mlAlgorithm;
        private int threshold;
    }

    public static class QueryBuilder {

        public ExpExecuter.QueryBuilder buildKs(int... ks) {
            this.ks = ks;
            return this;
        }

        public ExpExecuter.QueryBuilder buildWindows(float... sides) {
            this.sides = sides;
            return this;
        }

        public ExpExecuter.QueryBuilder buildPointsNum(int... insertedPoints) {
            this.insertedPoints = insertedPoints;
            return this;
        }

        public ExpExecuter.QueryBuilder buildFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ExpExecuter.QueryBuilder buildRLAlgorithm(String rlAlgorithm) {
            this.rlAlgorithm = rlAlgorithm;
            return this;
        }

        public ExpExecuter.QueryBuilder buildIteration(int iteration) {
            this.iteration = iteration;
            return this;
        }

        public ExpExecuter.QueryBuilder buildIRtree(IRtree rtree) {
            this.rtree = rtree;
            return this;
        }

        public ExpExecuter.QueryBuilder buildDim(int dim) {
            this.dim = dim;
            return this;
        }

        public ExpExecuter.QueryBuilder buildThreshold(int threshold) {
            this.threshold = threshold;
            return this;
        }

        public ExpExecuter.QueryBuilder buildMLAlgorithm(String mlAlgorithm) {
            this.mlAlgorithm = mlAlgorithm;
            return this;
        }

        public ExpExecuter.QueryBuilder buildIsAfterRL(boolean isAfterRL) {
            this.isAfterRL = isAfterRL;
            return this;
        }

        public ExpExecuter.QueryBuilder buildQueryType(int queryType) {
            this.queryType = queryType;
            return this;
        }

        public ExpExecuter.QueryBuilder buildTreeType(String treeType) {
            this.treeType = treeType;
            return this;
        }

        public ExpExecuter.QueryBuilder buildStages(int stages) {
            this.stages = stages;
            return this;
        }

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }

        private int[] ks;
        private float[] sides;
        private int iteration = 1;
        private IRtree rtree;
        private int dim;
        private String fileName;
        private String rlAlgorithm;
        private String mlAlgorithm;
        private boolean isAfterRL;
        private int threshold;
        private int queryType;
        private int[] insertedPoints;
        private String treeType;
        private int stages;
    }
}


