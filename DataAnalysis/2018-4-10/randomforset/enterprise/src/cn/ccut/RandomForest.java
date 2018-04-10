package cn.ccut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class RandomForest {
    // 训练数据文件地址
    private String filePath;
    // 决策树的样本占总数的占比率
    private double sampleNumRatio;
    // 样本数据的采集特征数量占总特征的比例
    private double featureNumRatio;
    // 决策树的采样样本数
    private int sampleNum;
    // 样本数据的采集采样特征数
    private int featureNum;
    // 随机森林中的决策树的数目,等于总的数据数/用于构造每棵树的数据的数量
    private int treeNum;
    // 随机数产生器
    private Random random;
    // 样本数据列属性名称行
    private String[] featureNames;
    // 原始的总的数据
    private ArrayList<String[]> totalDatas;
    // 决策树森林
    private ArrayList<DecisionTree> randomForset;

    public RandomForest(String filePath, double sampleNumRatio, double featureNumRatio) {
        this.filePath = filePath;
        this.sampleNumRatio = sampleNumRatio;
        this.featureNumRatio = featureNumRatio;

        readDataFile();
    }

    /**
     * 从文件中读取数据
     */
    private void readDataFile() {
        File file = new File(filePath);
        ArrayList<String[]> dataArray = new ArrayList<String[]>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            String[] tempArray;
            while ((str = in.readLine()) != null) {
                tempArray = str.split(" ");
                dataArray.add(tempArray);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        totalDatas = dataArray;
        featureNames = totalDatas.get(0);
        sampleNum = (int) ((totalDatas.size() - 1) * sampleNumRatio);
        //算属性数量的时候需要去掉id属性和决策属性，用条件属性计算
        featureNum = (int) ((featureNames.length -2) * featureNumRatio);
        // 算数量的时候需要去掉首行属性名称行
        treeNum =  sampleNum;
    }

    /**
     * 构造随机森林
     */
    public void constructRandomForest() {
        DecisionTree tree;
        random = new Random();
        randomForset = new ArrayList<>();

        System.out.println("下面是随机森林中的决策树：");
        // 构造决策树加入森林中
        for (int i = 0; i < treeNum; i++) {
            System.out.println("\n决策树" + (i+1));
            tree = produceDecisionTree();
            randomForset.add(tree);
        }
    }

    /**
     * 产生决策树
     */
    private DecisionTree produceDecisionTree() {
        DecisionTree tree;
        //datas包含训练每棵树的样本和属性
        ArrayList<String[]> datas;

        //获取样本和属性
        datas = getRandomSampleAndFeature();

        //校验数据
        while (!dataCheck(datas)) {
            datas = getRandomSampleAndFeature();
        }

        /*for(String[] s :datas) {
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < s.length; i++){
                buffer.append(s[i]);
                buffer.append(" ");
            }
            System.out.println(buffer.toString());
        }*/

        tree = new DecisionTree(datas);

        return tree;
    }

    /**
     * 当属性值都为一个值时，无法进行二分，返回false重新获取数据
     * @return
     */
    public boolean dataCheck(ArrayList<String[]> datas) {
        String[][] data = new String[datas.size()][];
        datas.toArray(data);
        String[] attrNames = data[0];
        int attrNum = data[0].length;

        HashMap<String, ArrayList<String>> attrValue = new HashMap<>();
        ArrayList<String> tempValues;

        // 按照列的方式，从左往右找
        for (int j = 1; j < attrNum; j++) {
            // 从一列中的上往下开始寻找值
            tempValues = new ArrayList<>();
            for (int i = 1; i < data.length; i++) {
                if (!tempValues.contains(data[i][j])) {
                    // 如果这个属性的值没有添加过，则添加
                    tempValues.add(data[i][j]);
                }
            }

            // 一列属性的值已经遍历完毕，复制到map属性表中
            attrValue.put(data[0][j], tempValues);
        }

        Boolean flug = true;
        Set<String> keySet = attrValue.keySet();
        if(keySet != null && keySet.size() > 0) {
            for(String key : keySet) {
                ArrayList<String> list = attrValue.get(key);
                if(list.size() == 1) {
                    flug = false;
                }
            }
        }


        return flug;
    }

    /**
     * 返回随机选取的样本和属性
     * @return
     */
    private ArrayList<String[]> getRandomSampleAndFeature() {
        int temp = 0;
        String[] tempData;
        //采样数据的随机行号组
        ArrayList<Integer> sampleRandomNum = new ArrayList<>();
        //采样属性特征的随机列号组
        ArrayList<Integer> featureRandomNum = new ArrayList<>();
        //要返回的数据
        ArrayList<String[]> datas = new ArrayList<>();

        //获取样本行id
        for(int i=0; i<sampleNum;){
            temp = random.nextInt(totalDatas.size());

            //如果是行首属性名称行，则跳过
            if(temp == 0){
                continue;
            }

            sampleRandomNum.add(temp);
            i++;
        }

        //获取属性列id
        for(int i=0; i<featureNum;){
            temp = random.nextInt(featureNames.length);

            //如果是第一列的数据id号或者是决策属性列，则跳过
            if(temp == 0 || temp == featureNames.length-1){
                continue;
            }

            if(!featureRandomNum.contains(temp)){
                featureRandomNum.add(temp);
                i++;
            }
        }

        String[] singleRecord;
        String[] headCulumn = null;
        // 获取随机数据行
        for(int dataIndex: sampleRandomNum){
            singleRecord = totalDatas.get(dataIndex);

            //数据长度
            tempData = new String[featureNum+2];
            headCulumn = new String[featureNum+2];

            for(int i=0,k=1; i<featureRandomNum.size(); i++,k++){
                temp = featureRandomNum.get(i);

                headCulumn[k] = featureNames[temp];
                tempData[k] = singleRecord[temp];
            }

            //加上id列的信息
            headCulumn[0] = featureNames[0];
            //加上决策分类列的信息
            headCulumn[featureNum+1] = featureNames[featureNames.length-1];
            tempData[featureNum+1] = singleRecord[featureNames.length-1];
            //tempData[0] = singleRecord[0];

            //加入此行数据
            datas.add(tempData);
        }

        //加入行首列出现名称
        datas.add(0, headCulumn);
        //对筛选出的数据重新做id分配
        temp = 0;
        for(String[] array: datas){
            //从第2行开始赋值
            if(temp > 0){
                array[0] = temp + "";
            }

            temp++;
        }

        return datas;
    }



}
