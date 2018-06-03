package cn.ccut.abiprediction.common;

import cn.ccut.abiprediction.multilayerperceptron.NsrIdLinkDataset;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Serializable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 文件操作工具
 */

public class DataFileLoadAndParse implements Serializable {
    // 保存实例对象
    private static DataFileLoadAndParse dataFileLoadAndParse = new DataFileLoadAndParse();

    public DataFileLoadAndParse() {}

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

    /**
     * 加载测试集文件
     *
     * @param jsc
     * @param filePath
     * @return
     */
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

    /**
     * 删除文件
     *
     * @param path
     * @throws Exception
     */
    public static void clearUpOutputPathFile(String path) throws Exception{
        File file = new File(path);
        if(file.exists()) {
            File[] files = file.listFiles();
            for(File f : files) {
                if(f.isDirectory()) {
                    //递归删除
                    clearUpOutputPathFile(f.toString());
                }
                f.delete();
            }
            file.delete();
        }
    }

    /**
     * 加载Dataset格式数据
     * @return
     */
    public Dataset<Row> loadTrainDataToDataset(SparkSession spark, String path) throws Exception {
        // 读取文件
        HashMap<String, String> recondMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            recondMap.put(split[0], split[1]);
        }
        reader.close();

        //将文件写入
        new File("data/MultilayerPerceptronData/tempFile/train.txt");
        new File("data/MultilayerPerceptronData/tempFile").mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/MultilayerPerceptronData/tempFile/train.txt"));
        Set<String> keySet = recondMap.keySet();
        for(String key : keySet) {
            writer.write(recondMap.get(key));
            writer.newLine();
            writer.flush();
        }
        writer.close();

        // 读取并返回文件
        return spark.read().format("libsvm").load("data/MultilayerPerceptronData/tempFile/train.txt");
    }

    public NsrIdLinkDataset loadTestDataToDataset(SparkSession spark, String path) throws Exception {
        // 读取文件
        HashMap<String, String> recondMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            recondMap.put(split[0], split[1]);
            //recondMap.put(split[0], line);
        }
        reader.close();

        //将文件写入
        new File("data/MultilayerPerceptronData/tempFile/test.txt").delete();
        new File("data/MultilayerPerceptronData/tempFile").mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/MultilayerPerceptronData/tempFile/test.txt"));
        Set<String> keySet = recondMap.keySet();
        // 将set中的内容放入list中
        ArrayList<String> nsrIdList = new ArrayList<>();
        for(String key : keySet) {
            nsrIdList.add(key);
            writer.write(recondMap.get(key));
            writer.newLine();
            writer.flush();
        }
        writer.close();

        NsrIdLinkDataset nsrIdLinkDataset = new NsrIdLinkDataset();
        nsrIdLinkDataset.setNsrIdList(nsrIdList);
        nsrIdLinkDataset.setTestDataFrame(spark.read().format("libsvm").load("data/MultilayerPerceptronData/tempFile/test.txt"));
        return nsrIdLinkDataset;
    }
}
