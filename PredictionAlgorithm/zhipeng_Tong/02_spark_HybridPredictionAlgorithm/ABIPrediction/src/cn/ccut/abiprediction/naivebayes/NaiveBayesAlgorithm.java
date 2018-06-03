package cn.ccut.abiprediction.naivebayes;

import cn.ccut.abiprediction.common.DataFileLoadAndParse;
import cn.ccut.abiprediction.common.NsrIdAndPredictionAndLabel;
import cn.ccut.abiprediction.common.NsrIdLinkLabelPoint;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.SparkConf;

import java.util.List;

public class NaiveBayesAlgorithm {
    // public static void main(String[] args) throws Exception {
    public static List<NsrIdAndPredictionAndLabel> run(String trainFilePath, String testFilePath) throws Exception {
       // 创建环境
        SparkConf sparkConf = new SparkConf().setAppName("NaiveBayesClassification");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // 加载训练数据,并解析数据文件
        DataFileLoadAndParse loadAndParse = DataFileLoadAndParse.getInstance();
        // String trainFilePath = "CVData/NaiveBayesData/4-CV_train.dat";
        JavaRDD<LabeledPoint> trainData = loadAndParse.loadTrainDataByFile(jsc, trainFilePath);
        // 加载训练数据,并解析数据文件
        // String testFilePath = "CVData/NaiveBayesData/4-CV_test.dat";
        JavaRDD<NsrIdLinkLabelPoint> testData = loadAndParse.loadTestDataByFile(jsc, testFilePath);

        // 训练模型
        /*final NaiveBayesModel model = NaiveBayes.train(trainData.rdd(), 1.0);

        // 保存模型
        model.save(jsc.sc(), "model/NaiveBayesModel");*/

        // 加载模型
        final NaiveBayesModel model = NaiveBayesModel.load(jsc.sc(), "model/NaiveBayesModel");

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
        DataFileLoadAndParse.clearUpOutputPathFile("prediction/NaiveBayesPrediction");
        predictionAndLabel.saveAsTextFile("prediction/NaiveBayesPrediction");

        // 计算准确率
        Double testAccuracy =
                1.0 * predictionAndLabel.filter(new Function<NsrIdAndPredictionAndLabel, Boolean>() {
                    @Override
                    public Boolean call(NsrIdAndPredictionAndLabel nsrIdAndPredictionAndLabel) throws Exception {
                        double prediction = nsrIdAndPredictionAndLabel.getPrediction();
                        double label = nsrIdAndPredictionAndLabel.getLabel();
                        return prediction == label;
                    }
                }).count() / (double) testData.count();

        System.out.println("Test NaiveBayes Accuracy: " + testAccuracy);

        List<NsrIdAndPredictionAndLabel> result = predictionAndLabel.collect();
        jsc.stop();

        // 返回结果
        return result;
    }
}
