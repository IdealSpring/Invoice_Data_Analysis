package cn.ccut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientRun {
    public static void main(String[] args) throws Exception {
        //训练数据路径
        String traningDataFilePath = "D:\\idealU\\workspace\\randomforset\\demo\\src\\cn\\ccut\\tranData.txt";
        //测试数据路径
        String testDataFilePath = "D:\\idealU\\workspace\\randomforset\\demo\\src\\cn\\ccut\\testData.txt";

        // 决策树的样本占总数的占比率
        double sampleNumRatio = 0.4;
        // 样本数据的采集特征数量占总特征的比例
        double featureNumRatio = 0.5;

        RandomForest randomForest = new RandomForest(traningDataFilePath, sampleNumRatio, featureNumRatio);
        randomForest.constructRandomForest();

        randomForest.testData(testDataFilePath);
        //resultClassType = randomForest.forestJudgeClassType(queryStr);

        //System.out.println();
        //System.out.println(MessageFormat.format("查询属性描述{0},预测的分类结果为BuysCompute:{1}", queryStr,resultClassType));
    }
}
