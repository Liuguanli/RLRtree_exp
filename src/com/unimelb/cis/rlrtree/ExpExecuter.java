package com.unimelb.cis.rlrtree;

import com.unimelb.cis.geometry.Mbr;
import com.unimelb.cis.structures.IRtree;
import com.unimelb.cis.structures.hrtree.HRtree;
import com.unimelb.cis.structures.zrtree.ZRtree;
import com.unimelb.cis.utils.ExpReturn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.unimelb.cis.rlrtree.ExpParam.*;

public class ExpExecuter {

    private float[] sides;

    private int iteration = 100;

    private String inputFile;
    private String outputFile;
    private String pythonFile;
    private int dim;
    private int level;
    private int pagesize;

    private IRtree rtree;
    private String algorithm;

    private String distribution;
    private int datasetSize;
    private int skewness;

    private boolean isAfterRL;
    private String type;
    private boolean isWarmUp;

    public ExpExecuter(ExpExecuter.QueryBuilder builder) {
        this.sides = builder.sides;
        this.iteration = builder.iteration;
        this.rtree = builder.rtree;
        this.dim = builder.dim;
        this.outputFile = builder.fileName;
        this.algorithm = builder.algorithm;
    }

    public ExpExecuter(ExpExecuter.PythonCommandBuilder builder) {
        this.dim = builder.dim;
        this.inputFile = builder.inputFile;
        this.outputFile = builder.outputFile;
        this.level = builder.level;
        this.pagesize = builder.pagesize;
        this.pythonFile = builder.pythonFile;
        this.algorithm = builder.algorithm;

        this.datasetSize = builder.datasetSize;
        this.distribution = builder.distribution;
        this.skewness = builder.skewness;
        this.type = builder.type;
    }

    public ExpExecuter(ExpExecuter.RtreeBuilder builder) {
        this.inputFile = builder.inputFile;
        this.outputFile = builder.outputFile;
        this.pagesize = builder.pagesize;
        this.type = builder.type;
    }

    public IRtree buildRtree(RtreeFinishCallback callback) {
        IRtree rtree;
        switch (type) {
            case ZCurveRtree:
                rtree = new ZRtree(pagesize);
                break;
            case HCurveRtree:
                rtree = new HRtree(pagesize);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        rtree.buildRtree(inputFile);
        rtree.output(outputFile);
        callback.onFinish(rtree);
        return rtree;
    }

    public IRtree buildNewRtree() {
        IRtree rtree;
        switch (type) {
            case ZCurveRtree:
                rtree = new ZRtree(pagesize);
                break;
            case HCurveRtree:
                rtree = new HRtree(pagesize);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        rtree.buildRtreeAfterTuning(inputFile, dim, level);
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
                .append(" -a ").append(algorithm);
        return stringBuilder.toString();
    }

    public String getDrawFigureCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("python ").append(pythonFile);
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

    public String getRecordFileName(float side) {
        String file = outputFile;
        System.out.println(file);
        String[] names = file.split(separator);
        String name = names[names.length - 1].split("\\.")[0];
//        DecimalFormat df = new DecimalFormat("#.0000");
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(recordRoot).append(name);
        nameBuilder.append(algorithm);

        nameBuilder.append("_").append((int) (side * side * 1000000)).append(".txt");

        return nameBuilder.toString();
    }

    public void executeWindowQuery(Callback callback) {
        for (int i = 0; i < sides.length; i++) {
            List<Mbr> mbrs = Mbr.getMbrs(sides[i], iteration, dim);
            ExpResultHelper expResultHelper = new ExpResultHelper();
            // execute once before record the result
            if (isWarmUp) {
                for (int j = 0; j < mbrs.size(); j++) {
                    Mbr mbr = mbrs.get(j);
                    rtree.windowQuery(mbr);
                }
            }
            for (int j = 0; j < mbrs.size(); j++) {
                Mbr mbr = mbrs.get(j);
                expResultHelper.addReturn(rtree.windowQuery(mbr));
            }
            ExpReturn expReturn = expResultHelper.getResult();
            String name = getRecordFileName(sides[i]);
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

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }


        private int pagesize;
        private String inputFile;
        private String outputFile;
        private String type;

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

        public ExpExecuter.PythonCommandBuilder buildAlgorithm(String algorithm) {
            this.algorithm = algorithm;
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

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }

        private String inputFile;
        private String outputFile;
        private String pythonFile;
        private int dim;
        private int level;
        private int pagesize;
        private String algorithm;

        private String distribution;
        private int datasetSize;
        private int skewness;

        private String type;

    }

    public static class QueryBuilder {

        public ExpExecuter.QueryBuilder buildWindows(float... sides) {
            this.sides = sides;
            return this;
        }

        public ExpExecuter.QueryBuilder buildFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ExpExecuter.QueryBuilder buildAlgorithm(String algorithm) {
            this.algorithm = algorithm;
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

        public ExpExecuter.QueryBuilder buildIsWarmUp(boolean isWarmUp) {
            this.isWarmUp = isWarmUp;
            return this;
        }

        public ExpExecuter build() {
            return new ExpExecuter(this);
        }

        private float[] sides;
        private int iteration = 100;
        private IRtree rtree;
        private int dim;
        private String fileName;
        private String algorithm;
        private boolean isWarmUp;
    }
}