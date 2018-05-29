package cn.ccut.abiprediction.gbts;

import java.util.HashMap;
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

public class GradientBoostingTreesMain {
  public static void main(String[] args) {
      // 创建环境
      SparkConf sparkConf = new SparkConf().setAppName("GradientBoostedTreesClassification");
      JavaSparkContext jsc = new JavaSparkContext(sparkConf);

      // 加载训练数据,并解析数据文件
      DataFileLoadAndParse loadAndParse = DataFileLoadAndParse.getInstance();
      String trainFilePath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/result/Stage-05_CrossValidationData/1-CV_train.dat";
      JavaRDD<LabeledPoint> trainData = loadAndParse.loadTrainDataByFile(jsc, trainFilePath);
      // 加载训练数据,并解析数据文件
      String testFilePath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/result/Stage-05_CrossValidationData/1-CV_test.dat";
      JavaRDD<NsrIdLinkLabelPoint> testData = loadAndParse.loadTestDataByFile(jsc, testFilePath);

      // Train a GradientBoostedTrees model.
      // The defaultParams for Classification use LogLoss by default.
      // 设置模型参数
      BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Classification");
      boostingStrategy.setNumIterations(10); // Note: Use more iterations in practice.
      boostingStrategy.getTreeStrategy().setNumClasses(2);
      boostingStrategy.getTreeStrategy().setMaxDepth(30);
      // Empty categoricalFeaturesInfo indicates all features are continuous.
      Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
      boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);

      // 训练模型
      final GradientBoostedTreesModel model = GradientBoostedTrees.train(trainData, boostingStrategy);

      // 保存模型
      model.save(jsc.sc(), "D:/idealU/workspace2/ABIPrediction/mode//GradientBoostingTreeMode");

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
      predictionAndLabel.saveAsTextFile("D:/idealU/workspace2/ABIPrediction/predictionResult/GradientBoostingTreePrediction");

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

      // 显示数结构
      //System.out.println("Learned classification GBT model:\n" + model.toDebugString());

      // 加载模型
      //GradientBoostedTreesModel sameModel = GradientBoostedTreesModel.load(jsc.sc(),
      //"target/tmp/myGradientBoostingClassificationModel");

    jsc.stop();
  }

}
