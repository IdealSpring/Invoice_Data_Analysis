package cn.ccut.abiprediction.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * 交叉验证类
 */
public class CrossValidationUtils {
    private static final Logger log = LoggerFactory.getLogger(CrossValidationUtils.class);
    private static String nsrxxIdFilePath = "data/RandomForestData/RFAttributeList.txt";
    // 保存所有纳税人Id, CV
    private static ArrayList<ArrayList<String>> nsrIdCV = new ArrayList<>();
    // 随机数生成工具
    private static Random random = new Random();

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======================有异常===================");
        }
    }

    public CrossValidationUtils() throws Exception {

    }

    /**
     * 初始化
     */
    private static void init() throws Exception {
        ArrayList<String> nsrIdList = new ArrayList<>();

        BufferedReader nsrIdBuffer = new BufferedReader(new FileReader(nsrxxIdFilePath));
        String line = null;
        while ((line = nsrIdBuffer.readLine()) != null) {
            String[] split = line.split(",");
            nsrIdList.add(split[0]);
        }
        nsrIdBuffer.close();

        // 五则中，每则选取样本数量
        int nsrIdSampleNumCV = nsrIdList.size()/5;

        // 划分数据
        for(int i = 0; i < 5; i++) {
            ArrayList<String> tempId = new ArrayList<>();

            for(int j = 0; j < nsrIdSampleNumCV; ) {
                int randomNum = random.nextInt(nsrIdList.size());
                String id = nsrIdList.get(randomNum);
                if(!tempId.contains(id)) {
                    tempId.add(id);
                    j++;
                }
                nsrIdList.remove(randomNum);
            }

            nsrIdCV.add(tempId);
        }
    }

    /**
     * 创建数据集
     * @param filePath
     * @throws IOException
     */
    public static void createDataSet(String filePath, String outFilePath) throws Exception {
        // 将文件加载到map中
        BufferedReader readerFile = new BufferedReader(new FileReader(filePath));
        HashMap<String, String> dataSet = new HashMap<>();
        String line = null;
        while((line = readerFile.readLine()) != null) {
            String[] split = line.split(",");

            // 对神经网络的数据进行特殊处理
            dataSet.put(split[0], line);
        }
        readerFile.close();

        //删除残余数据
        DataFileLoadAndParse.clearUpOutputPathFile(outFilePath);
        //创建文件夹
        new File(outFilePath).mkdirs();

        // 生成交叉验证五则文件
        for(int i = 0; i < 5; i++) {
            String trainOutPath = outFilePath + "/" + i +"-CV_train.dat";
            String testOutPath = outFilePath + "/" + i + "-CV_test.dat";
            BufferedWriter writerTrain = new BufferedWriter(new FileWriter(trainOutPath));
            BufferedWriter writerTest = new BufferedWriter(new FileWriter(testOutPath));

            //取出id
            ArrayList<String> idList = nsrIdCV.get(i);
            for(int x = 0; x < idList.size(); x++) {
                // 获取
                String tempId = idList.get(x);
                String str = dataSet.get(tempId);

                // 写
                writerTest.write(str);
                writerTest.newLine();
                writerTest.flush();
            }

            for(int j = 0; j < 5; j++) {
                if(i != j) {
                    ArrayList<String> idTemp = nsrIdCV.get(j);
                    for(int y = 0; y < idTemp.size(); y++) {
                        // 获取
                        String tempId = idTemp.get(y);
                        String str = dataSet.get(tempId);

                        // 写
                        writerTrain.write(str);
                        writerTrain.newLine();
                        writerTrain.flush();
                    }
                }
            }

            writerTrain.close();
            writerTest.close();
        }
    }

}
