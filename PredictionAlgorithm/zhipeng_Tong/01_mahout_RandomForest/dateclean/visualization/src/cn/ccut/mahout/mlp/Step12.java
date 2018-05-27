package cn.ccut.mahout.mlp;

import org.apache.mahout.classifier.mlp.RunMultilayerPerceptron;
import org.apache.mahout.classifier.mlp.TrainMultilayerPerceptron;

public class Step12 {
    public static void main(String[] args) throws Exception {
        trainMlp();

        testMlp();
    }

    /**
     * 2.测试
     */
    private static void testMlp() throws Exception {
        String[] args = new String[]{
                "-i", "hdfs://111.116.20.110:9000/user/hadoop/mlp/irisdata.csv",
                "-cr", "0", "3",
                "-mo", "hdfs://111.116.20.110:9000/user/hadoop/mlp/model.model",
                "-o", "hdfs://111.116.20.110:9000/user/hadoop/mlp/labelResult.txt"
        };

        RunMultilayerPerceptron.main(args);
    }

    /**
     * 1.训练多层感知器模型
     */
    private static void trainMlp() throws Exception {
        String[] args = new String[]{
                "-i", "hdfs://111.116.20.110:9000/user/hadoop/mlp/irisdata.csv",
                "-labels", "Iris-setosa", "Iris-versicolor", "Iris-virginica",
                "-mo", "hdfs://111.116.20.110:9000/user/hadoop/mlp/model.model",
                "-ls", "4", "8", "3", "-l", "0.2", "-m", "0.35", "-r", "0.0001"
        };
        TrainMultilayerPerceptron.main(args);
    }
}
