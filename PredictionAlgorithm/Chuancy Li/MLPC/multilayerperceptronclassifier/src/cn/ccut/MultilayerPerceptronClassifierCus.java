package cn.ccut;

import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

/**
 * 多层感知器
 *
 * @authorMr.Robot
 * @create2018-05-28 16:24
 */
public class MultilayerPerceptronClassifierCus {
    public static final String DATA_PATH = "data/";

    public static void main(String[] args) {
        CleanFiles.clean();

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaMultilayerPerceptronClassifierExample")
                .getOrCreate();

        // Load training data
        String path = DATA_PATH + "sourceData/14";
        Dataset<Row> dataFrame = spark.read().format("libsvm").load(path);

        // Split the data into train and test
        Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.5, 0.5}, 1234L);
        Dataset<Row> train = splits[0];
        // Dataset<Row> test = splits[1];
        Dataset<Row> test = dataFrame;

        // specify layers for the neural network:
        int[] layers = new int[] {14, 15, 14, 2};

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
        // model.write().overwrite().save(DATA_PATH + "model");

        // load the model
        // MultilayerPerceptronClassificationModel model = MultilayerPerceptronClassificationModel.load(DATA_PATH + "model");

        // compute accuracy on the test set
        Dataset<Row> result = model.transform(test);
        Dataset<Row> predictionAndLabels = result.select("prediction", "label");

        predictionAndLabels.toJavaRDD().saveAsTextFile(DATA_PATH  + "/tempOut/predictionAndLabels");

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setMetricName("accuracy");

        System.out.println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels));

        spark.stop();

        try {
            MergeIdAndPredition.merge();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
