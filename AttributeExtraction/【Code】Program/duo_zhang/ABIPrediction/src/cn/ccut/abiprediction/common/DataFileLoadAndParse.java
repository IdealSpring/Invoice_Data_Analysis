package cn.ccut.abiprediction.common;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Serializable;

import java.util.HashMap;

/**
 * 文件操作工具
 */

public class DataFileLoadAndParse implements Serializable {
    // 保存实例对象
    private static DataFileLoadAndParse dataFileLoadAndParse = new DataFileLoadAndParse();

    private DataFileLoadAndParse() {}

    /**
     * 获取实例对象
     * @return
     */
    public static DataFileLoadAndParse getInstance() {
        return dataFileLoadAndParse;
    }

    /**
     * 从文件中加载训练数据,解析成JavaRDD<LabeledPoint>
     *
     * @param jsc 上下文环境
     * @param filePath 文件路径
     * @return
     */
    public JavaRDD<LabeledPoint> loadTrainDataByFile(JavaSparkContext jsc, String filePath) {
        JavaRDD<String> textFile = jsc.textFile(filePath);

        JavaRDD<LabeledPoint> trainData = textFile.map(new Function<String, LabeledPoint>() {
            @Override
            public LabeledPoint call(String line) throws Exception {
                String[] split = line.split(",");
                double[] values = new double[split.length - 2];
                for (int i = 1; i < split.length - 1; i++) {
                    values[i - 1] = Double.parseDouble(split[i]);
                }
                return new LabeledPoint(Double.parseDouble(split[split.length - 1]), Vectors.dense(values));
            }
        });

        return trainData;
    }

    public JavaRDD<NsrIdLinkLabelPoint> loadTestDataByFile(JavaSparkContext jsc, String filePath) {
        JavaRDD<String> textFile = jsc.textFile(filePath);

        JavaRDD<NsrIdLinkLabelPoint> hashMapJavaRDD = textFile.map(new Function<String, NsrIdLinkLabelPoint>() {
            @Override
            public NsrIdLinkLabelPoint call(String line) throws Exception {
                String[] split = line.split(",");
                double[] values = new double[split.length - 2];
                for (int i = 1; i < split.length - 1; i++) {
                    values[i - 1] = Double.parseDouble(split[i]);
                }
                LabeledPoint labeledPoint = new LabeledPoint(Double.parseDouble(split[split.length - 1]), Vectors.dense(values));
                return new NsrIdLinkLabelPoint(split[0], labeledPoint);
            }
        });

        return hashMapJavaRDD;
    }
}
