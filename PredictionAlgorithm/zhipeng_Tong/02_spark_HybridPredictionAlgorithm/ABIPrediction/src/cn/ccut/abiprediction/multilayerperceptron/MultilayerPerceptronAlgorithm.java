package cn.ccut.abiprediction.multilayerperceptron;

import cn.ccut.abiprediction.common.DataFileLoadAndParse;
import cn.ccut.abiprediction.common.NsrIdAndPredictionAndLabel;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 多层感知器
 *
 * @authorMr.Robot
 * @create2018-05-28 16:24
 */
public class MultilayerPerceptronAlgorithm {
    // public static void main(String[] args) throws Exception {
    public static List<NsrIdAndPredictionAndLabel> run(String trainFilePath, String testFilePath) throws Exception {
        SparkSession spark = SparkSession
                .builder()
                .appName("MultilayerPerceptron")
                .getOrCreate();

        // 加载训练集
        DataFileLoadAndParse dataFileLoadAndParse = new DataFileLoadAndParse();
        // String trainFilePath = "CVData/MultilayerPerceptronData/0-CV_train.dat";
        Dataset<Row> train = dataFileLoadAndParse.loadTrainDataToDataset(spark, trainFilePath);

        // specify layers for the neural network:
        /*int[] layers = new int[] {14, 15, 14, 2};

        // create the trainer and set its parameters
        MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
                .setLayers(layers)
                .setBlockSize(128)
                .setSeed(1234L)
                .setMaxIter(100)
                .setFeaturesCol("features")
                .setLabelCol("label");

        // train the model
        MultilayerPerceptronClassificationModel model = trainer.fit(train);

        // save the model
        model.write().overwrite().save("model/MultilayerPerceptronMode");*/

        // load the model
        MultilayerPerceptronClassificationModel model = MultilayerPerceptronClassificationModel.load("model/MultilayerPerceptronMode");

        // compute accuracy on the test set
        // 加载测试集
        // String testFilePath = "CVData/MultilayerPerceptronData/0-CV_test.dat";
        NsrIdLinkDataset nsrIdLinkDataset = dataFileLoadAndParse.loadTestDataToDataset(spark, testFilePath);
        Dataset<Row> test = nsrIdLinkDataset.getTestDataFrame();
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");

        // 转换为List，方便操作
        List<Row> predictionLabels = predictionAndLabels.toJavaRDD().collect();
        // 最终预测结果
        List<NsrIdAndPredictionAndLabel> predictionLabelsResult = new ArrayList<>();
        ArrayList<String> idList =  nsrIdLinkDataset.getNsrIdList();
        for(int i = 0; i < idList.size(); i++) {
            String id = idList.get(i);
            Row row = predictionLabels.get(i);
            double prediction = (double) row.get(0);
            double label = (double) row.get(1);

            NsrIdAndPredictionAndLabel predictionAndLabel = new NsrIdAndPredictionAndLabel();
            predictionAndLabel.setNsrId(id);
            predictionAndLabel.setPrediction(prediction);
            predictionAndLabel.setLabel(label);

            predictionLabelsResult.add(predictionAndLabel);
        }

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        System.out.println("Test MultilayerPerceptron accuracy = " + evaluator.evaluate(predictionAndLabels));
        spark.stop();

        //保存预测结果
        DataFileLoadAndParse.clearUpOutputPathFile("prediction/MultilayerPerceptron");
        JavaSparkContext jsc = new JavaSparkContext(new SparkConf().setAppName("MultilayerPerceptron"));
        jsc.parallelize(predictionLabelsResult).saveAsTextFile("prediction/MultilayerPerceptron");
        jsc.stop();

        // 返回
        return predictionLabelsResult;
    }
}
