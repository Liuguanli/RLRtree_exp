package com.unimelb.cis.rm;

import com.unimelb.cis.rlrtree.ExpExecuter;
import com.unimelb.cis.rlrtree.ExpParam;
import com.unimelb.cis.rlrtree.RtreeFinishCallback;
import com.unimelb.cis.structures.IRtree;
import com.unimelb.cis.node.Point;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static weka.core.Attribute.ARFF_ATTRIBUTE_NUMERIC;

/**
 * first we get ZRtree or only the points with Z value and cal the last level index
 * <p>
 * then Use the model to classify to sub models. Then generate new points with new index!!!
 * <p>
 * All the model in this part should be stored to later prediction and insertion.
 * <p>
 * The model stop Until the sub data set less than a threshold.
 */
public class RecursiveModelRtree {

    /**
     * step 1
     * @param param
     * @return
     */
    public IRtree buildRtree(ExpParam param) {
        ExpExecuter executer = new ExpExecuter.RtreeBuilder()
                .buildType(param.curve)
                .buildInputFile(param.getInputFile())
                .buildOutputFile(param.getOutputFile())
                .buildPagesize(param.pagesizeBeforetuning)
                .build();
        return executer.buildRtree(new RtreeFinishCallback() {
            @Override
            public void onFinish(IRtree rtree) {
                prepareDataSet(rtree.getPoints());
            }

            @Override
            public void onError(IRtree rtree) {

            }
        });
    }

    /**
     * step 2
     * @param points
     * @return
     */
    public Instances prepareDataSet(List<Point> points) {
        int dim = points.get(0).getDim();
        FastVector atts = new FastVector();
        for (int i = 0; i < dim; i++) {
            atts.addElement(new Attribute("att" + (i + 1)));
        }
        atts.addElement(new Attribute("index"));
        Instances dataSet = new Instances("tree", atts, 0);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            double[] vals = new double[dataSet.numAttributes()];
            for (int j = 0; j < vals.length - 1; j++) {
                vals[j] = point.getLocation()[j];
            }
            vals[vals.length - 1] = point.getIndex();
            dataSet.add(new Instance(1.0, vals));
        }
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        return dataSet;
    }

    /**
     * step 3
     * @param param
     * @param instances
     */
    public void getModels(ExpParam param, Instances instances) {
        Classifier classifier = new weka.classifiers.functions.LinearRegression();
        try {
            classifier.buildClassifier(instances);
            for (int i = 0; i < instances.numInstances(); i++) {
                Instance instance = instances.instance(i);
                double value = classifier.classifyInstance(instance);
                System.out.println(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildRtree(List<Point> points) {

    }

    public void trainRecursiveModel() {

    }

    /**
     * add data
     * https://waikato.github.io/weka-wiki/formats_and_processing/creating_arff_file/
     *
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
    }

}
