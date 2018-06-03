package cn.ccut.abiprediction.gbts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ccut.abiprediction.common.DataFileLoadAndParse;
import cn.ccut.abiprediction.common.NsrIdAndPredictionAndLabel;
import cn.ccut.abiprediction.common.NsrIdLinkLabelPoint;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;
import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;

public class GradientBoostingTreeAlgorithm {
    //public static void main(String[] args) throws Exception {
    public static List<NsrIdAndPredictionAndLabel> run(String trainFilePath, String testFilePath) throws Exception {
        // 创建环境
        SparkConf sparkConf = new SparkConf().setAppName("GradientBoostedTreesClassification");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // 加载训练数据,并解析数据文件
        DataFileLoadAndParse loadAndParse = DataFileLoadAndParse.getInstance();
        // String trainFilePath = "CVData/GradientBoostingTreesData/1-CV_train.dat";
        JavaRDD<LabeledPoint> trainData = loadAndParse.loadTrainDataByFile(jsc, trainFilePath);
        // 加载训练数据,并解析数据文件
        // String testFilePath = "CVData/GradientBoostingTreesData/1-CV_test.dat";
        JavaRDD<NsrIdLinkLabelPoint> testData = loadAndParse.loadTestDataByFile(jsc, testFilePath);

        // Train a GradientBoostedTrees model.
        // The defaultParams for Classification use LogLoss by default.
        // 设置模型参数
        /*BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Classification");
        boostingStrategy.setNumIterations(10); // Note: Use more iterations in practice.
        boostingStrategy.getTreeStrategy().setNumClasses(2);
        boostingStrategy.getTreeStrategy().setMaxDepth(20);
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        categoricalFeaturesInfo.put(2, 2);
        categoricalFeaturesInfo.put(3, 2);

        categoricalFeaturesInfo.put(10, 2);
        categoricalFeaturesInfo.put(11, 2);
        categoricalFeaturesInfo.put(12, 2);
        categoricalFeaturesInfo.put(13, 2);
        categoricalFeaturesInfo.put(14, 2);

        boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

        // 训练模型
        final GradientBoostedTreesModel model = GradientBoostedTrees.train(trainData, boostingStrategy);

        // 保存模型
        model.save(jsc.sc(), "model/GradientBoostingTreeMode");*/

        // 加载模型
        final GradientBoostedTreesModel model = GradientBoostedTreesModel.load(jsc.sc(), "model/GradientBoostingTreeMode");

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
        DataFileLoadAndParse.clearUpOutputPathFile("prediction/GradientBoostingTreePrediction");
        predictionAndLabel.saveAsTextFile("prediction/GradientBoostingTreePrediction");

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
        System.out.println("Test GradientBoostingTree Accuracy: " + testAccuracy);

        // 显示数结构
        //System.out.println("Learned classification GBT model:\n" + model.toDebugString());

        List<NsrIdAndPredictionAndLabel> result = predictionAndLabel.collect();
        jsc.stop();

        // 返回预测结果
        return result;
  }
}
