package cn.ccut.abiprediction.randomforest;

import java.util.HashMap;
import java.util.List;

import cn.ccut.abiprediction.common.DataFileLoadAndParse;
import cn.ccut.abiprediction.common.NsrIdAndPredictionAndLabel;
import cn.ccut.abiprediction.common.NsrIdLinkLabelPoint;
import org.apache.spark.mllib.linalg.Vectors;
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;

public class RandomForestAlgorithm {
    public static List<NsrIdAndPredictionAndLabel> run(String trainFilePath, String testFilePath) throws Exception {
    //public static void main(String[] args) throws Exception {
        // 创建环境
        SparkConf sparkConf = new SparkConf().setAppName("RandomForestClassification");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // 加载训练数据,并解析数据文件
        DataFileLoadAndParse loadAndParse = DataFileLoadAndParse.getInstance();
        // String trainFilePath = "data/RandomForestData/2-CV_train.dat";
        JavaRDD<LabeledPoint> trainData = loadAndParse.loadTrainDataByFile(jsc, trainFilePath);
        // 加载测试数据,并解析数据文件
        // String testFilePath = "data/RandomForestData/2-CV_test.dat";
        JavaRDD<NsrIdLinkLabelPoint> testData = loadAndParse.loadTestDataByFile(jsc, testFilePath);

        // 随机森林训练参数
        /*Integer numClasses = 2;
        HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        categoricalFeaturesInfo.put(2, 2);
        categoricalFeaturesInfo.put(3, 2);
        categoricalFeaturesInfo.put(4, 4);
        categoricalFeaturesInfo.put(5, 4);
        categoricalFeaturesInfo.put(6, 3);
        categoricalFeaturesInfo.put(7, 3);
        categoricalFeaturesInfo.put(8, 3);
        categoricalFeaturesInfo.put(9, 3);
        categoricalFeaturesInfo.put(10, 4);
        categoricalFeaturesInfo.put(11, 3);
        categoricalFeaturesInfo.put(12, 2);
        categoricalFeaturesInfo.put(13, 2);
        categoricalFeaturesInfo.put(14, 3);

        Integer numTrees = 100; // Use more in practice.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        String impurity = "gini";
        Integer maxDepth = 20;
        Integer maxBins = 32;
        Integer seed = 12345;

        // 训练模型
        final RandomForestModel model = RandomForest.trainClassifier(trainData, numClasses,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,
                seed);

        // 保存模型
        model.save(jsc.sc(), "model/RandomForestModel");*/

        // 加载模型
        final RandomForestModel model = RandomForestModel.load(jsc.sc(), "model/RandomForestModel");

        // 对数据进行预测
        JavaRDD<NsrIdAndPredictionAndLabel> predictionAndLabel = testData.map(new Function<NsrIdLinkLabelPoint, NsrIdAndPredictionAndLabel>() {
            @Override
            public NsrIdAndPredictionAndLabel call(NsrIdLinkLabelPoint nsrIdLinkLabelPoint) throws Exception {
                LabeledPoint p = nsrIdLinkLabelPoint.getLabeledPoint();
                double label = p.label();
                return new NsrIdAndPredictionAndLabel(nsrIdLinkLabelPoint.getNsrId(), model.predict(p.features()), label);
            }
        });

        // 保存预测结果
        DataFileLoadAndParse.clearUpOutputPathFile("prediction/RandomForsetPrediction");
        predictionAndLabel.saveAsTextFile("prediction/RandomForsetPrediction");

        // 计算准确率
        Double testAccuracy =
                1.0 * predictionAndLabel.filter(new Function<NsrIdAndPredictionAndLabel, Boolean>() {
                    @Override
                    public Boolean call(NsrIdAndPredictionAndLabel nsrIdAndPredictionAndLabel) throws Exception {
                        double prediction = nsrIdAndPredictionAndLabel.getPrediction();
                        double label = nsrIdAndPredictionAndLabel.getLabel();
                        return prediction == label;
                    }
                }).count() / testData.count();
        System.out.println("Test RandomForest Accuracy: " + testAccuracy);

        // 展示随机森林
        //System.out.println("Learned classification forest model:\n" + model.toDebugString());

        List<NsrIdAndPredictionAndLabel> result = predictionAndLabel.collect();
        jsc.stop();

        // 返回预测结果
        return result;
    }
}
