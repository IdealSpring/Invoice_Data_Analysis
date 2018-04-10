package cn.ccut;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

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
                tempArray = str.split(",");
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
        //treeNum =  sampleNum;
        treeNum = 100;
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

        tree = new DecisionTree(datas);

        return tree;
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

    /**
     * 根据给定的属性条件进行类别的决策
     *
     * @param features
     *            给定的已知的属性描述
     * @return
     */
    public String forestJudgeClassType(String features) {
        // 结果类型值
        String resultClassType = "";
        String classType = "";
        int count = 0;
        Map<String, Integer> type2Num = new HashMap<String, Integer>();

        for (DecisionTree tree : randomForset) {
            classType = tree.decideClassType(features);
            if (type2Num.containsKey(classType)) {
                // 如果类别已经存在，则使其计数加1
                count = type2Num.get(classType);
                count++;
            } else {
                count = 1;
            }

            type2Num.put(classType, count);
        }

        // 选出其中类别支持计数最多的一个类别值
        count = -1;
        for (Map.Entry entry : type2Num.entrySet()) {
            if ((int) entry.getValue() > count) {
                count = (int) entry.getValue();
                resultClassType = (String) entry.getKey();
            }
        }

        return resultClassType;
    }

    /**
     * 测试数据
     */
    public void testData(String filePath) throws Exception {
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = null;

        // 计数器
        int count = 0;
        // 将 [正类] 预测为 [正类] 数, 假设0为正类， 1为负类
        // 将 [0] 预测为 [0] 数
        double TP = 0;
        // 将 [负类] 预测为 [正类] 数
        // 将 [1] 预测为 [0] 数
        double FP = 0;
        // 将 [负类] 预测为 [负类] 数
        // 将 [1] 预测为 [1] 数
        double TN = 0;
        // 将 [正类] 预测为 [负类] 数
        // 将 [0] 预测为 [1] 数
        double FN = 0;

        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");

            String resultClassType = "";
            StringBuffer buffer = new StringBuffer();
            //buffer.append("nsrId=").append(split[0]).append(",");
            buffer.append("onlyInputInvoice=").append(split[1]).append(",");
            buffer.append("onlyOutputInvoice=").append(split[2]).append(",");
            buffer.append("inputNoDeclareOverOne=").append(split[3]).append(",");
            buffer.append("outputNoDeclareOverOne=").append(split[4]).append(",");
            buffer.append("taxChangeRate=").append(split[5]);

            resultClassType = forestJudgeClassType(buffer.toString());
            System.out.println();
            System.out.println(MessageFormat.format("查询属性描述:nsr_id={0},预测的分类结果为BuysCompute:{1}", split[0] + "," +  buffer.toString(),resultClassType));

            count ++;

            if(split[6].equals("0")) {
                // 将 [0] 预测为 [0] 数
                if(resultClassType.equals("0")) {
                    TP ++;
                } else {
                    //将 [0] 预测为 [1] 数
                    FN ++;
                }
            }

            if(split[6].equals("1")) {
                //将 [1] 预测为 [1] 数
                if(resultClassType.equals("1")) {
                    TN ++;
                } else {
                    //将 [1] 预测为 [0] 数
                    FP ++;
                }
            }

        }

        //准确率
        double accuracy = (TP + TN)/(TP + TN + FP + FN);
        //错误率
        double errorRate = (FP + FN)/(TP + TN + FP + FN);
        //灵敏度
        double sensitive = TP/(TP + FN);
        //特效度
        double specificity = TN/(FN + TN);
        //精确率
        double precision = TP/(TP + FP);
        //召回率
        double recall = TP/(TP + FN);
        //F1-score
        double F1_score = (2 * precision * recall)/(precision + recall);
        System.out.println("一共：" + count + "条");
        System.out.println("算法评测：");
        System.out.println("准确率:" + accuracy);
        System.out.println("错误率:" + errorRate);
        System.out.println("灵敏度:" + sensitive);
        System.out.println("特效度:" + specificity);
        System.out.println("精确率:" + precision);
        System.out.println("召回率:" + recall);
        System.out.println("F1-score:" + F1_score);
        System.out.println();
        System.out.println("PS:");
        System.out.println("错误率:则与准确率相反，描述被分类器错分的比例.");
        System.out.println("灵敏度:表示的是所有正例中被分对的比例，衡量了分类器对正例的识别能力.");
        System.out.println("特效度:表示的是所有负例中被分对的比例，衡量了分类器对负例的识别能力.");
        System.out.println("精确率:表示被分为正例的示例中实际为正例的比例.");
        System.out.println("召回率:召回率是覆盖面的度量，度量有多个正例被分为正例.");
        System.out.println("F1-score:F1综合了P和R的结果，当F1较高时则能说明试验方法比较有效.");
    }
}
