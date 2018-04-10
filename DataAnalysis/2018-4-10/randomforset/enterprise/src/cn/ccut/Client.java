package cn.ccut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws Exception {
        /*String traningDataFilePath = "D:\\idealU\\workspace\\randomforset\\enterprise\\src\\cn\\ccut\\Test.txt";
        // 决策树的样本占总数的占比率
        double sampleNumRatio = 0.4;
        // 样本数据的采集特征数量占总特征的比例
        double featureNumRatio = 0.5;

        RandomForest randomForest = new RandomForest(traningDataFilePath, sampleNumRatio, featureNumRatio);
        randomForest.constructRandomForest();*/


        ArrayList<String[]> data = new ArrayList<>();

        File file = new File("D:\\idealU\\workspace\\randomforset\\enterprise\\src\\cn\\ccut\\Test01.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(" ");
            data.add(split);
        }

        System.out.println(Arrays.toString(data.toArray()));
        DecisionTree tree = new DecisionTree(data);

        /*for(int i = 1; i < 20000; i++) {
            System.out.println("【决策树】：" + i + "号");
            DecisionTree tree = new DecisionTree(data);
        }*/


    }
}
