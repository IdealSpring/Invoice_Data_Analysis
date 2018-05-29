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

public class NaiveBayesMain {
    public static void main(String[] args) {
       // 创建环境
        SparkConf sparkConf = new SparkConf().setAppName("NaiveBayesClassification");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // 加载训练数据,并解析数据文件
        DataFileLoadAndParse loadAndParse = DataFileLoadAndParse.getInstance();
        String trainFilePath = "D:\\dao\\bayesdatas\\train\\part-r-00000.txt";
        JavaRDD<LabeledPoint> trainData = loadAndParse.loadTrainDataByFile(jsc, trainFilePath);
        // 加载训练数据,并解析数据文件
        String testFilePath = "D:\\dao\\bayesdatas\\test\\part-r-00000.txt";
        JavaRDD<NsrIdLinkLabelPoint> testData = loadAndParse.loadTestDataByFile(jsc, testFilePath);

        // 训练模型
        final NaiveBayesModel model = NaiveBayes.train(trainData.rdd(), 1.0);

        // 保存模型
        //model.save(jsc.sc(), "D:/idealU/workspace2/ABIPrediction/mode//myNaiveBayesModel");

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
        //predictionAndLabel.saveAsTextFile("D:/idealU/workspace2/ABIPrediction/predictionResult/NaiveBayesPrediction");

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

        // 加载模型
        //NaiveBayesModel sameModel = NaiveBayesModel.load(jsc.sc(), "D:/idealU/workspace2/ABIPrediction/mode//myNaiveBayesModel");

        jsc.stop();
    }
}
