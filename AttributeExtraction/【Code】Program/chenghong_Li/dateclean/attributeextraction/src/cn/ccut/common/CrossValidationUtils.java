package cn.ccut.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.HadoopUtil;
import org.apache.xerces.util.DOMUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 交叉验证类
 */
public class CrossValidationUtils {
    private static final Logger log = LoggerFactory.getLogger(CrossValidationUtils.class);

    private CrossValidationUtils() {}

    /**
     * 分出训练集还有测试集
     *
     * @param crossNum 交叉验证折数
     */
    public static void upDateTrainAndTest(double crossNum) throws Exception {
//        String path = FilePathCollections.stage04ResultOutputPath + "/part-r-00000";
        String path = FilePathCollections.stage01ResultOutputPath + "/part-r-00000";
        ArrayList<String> originalData = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            originalData.add(line);
        }

        int selectSampleCount = (int) (originalData.size() / crossNum);

        ArrayList<ArrayList<String>> data_CV = new ArrayList<>();

        List<Integer> randomNum = new ArrayList<>();
        for(int i = 0; i < originalData.size(); i++) {
            randomNum.add(i);
        }

        for(int i = 0; i < crossNum; i ++) {
            ArrayList<Integer> sampleRandomNum = new ArrayList<>();
            ArrayList<String> unitData = new ArrayList<>();

            setSelectSampleNum(selectSampleCount, randomNum, sampleRandomNum);
            setDataSet(unitData, sampleRandomNum, originalData);

            data_CV.add(unitData);
        }

        writeToFile(data_CV);

        log.info("交叉验证测试集训练集划分完毕！");
    }

    /**
     * 设置要取出的数据索引
     *
     * @param selectSample
     * @param randomNum
     * @param sampleRandomNum_CV
     */
    private static void setSelectSampleNum(int selectSample, List<Integer> randomNum, ArrayList<Integer> sampleRandomNum_CV) {
        int temp = 0;
        Random random = new Random();

        for (int i = 0; i < selectSample; ) {
            temp = random.nextInt(randomNum.size());

            if(!sampleRandomNum_CV.contains(randomNum.get(temp))) {
                sampleRandomNum_CV.add(randomNum.get(temp));
                i++;
                randomNum.remove(temp);
            }
        }
    }

    /**
     * 根据索引去除数据
     *
     * @param data_cv_1
     * @param sampleRandomNum_cv_1
     * @param list
     */
    private static void setDataSet(ArrayList<String> data_cv_1, ArrayList<Integer> sampleRandomNum_cv_1, ArrayList<String> list) {
        for(int number : sampleRandomNum_cv_1) {
            data_cv_1.add(list.get(number));
        }
    }

    private static void writeToFile(ArrayList<ArrayList<String>> data_cv) throws Exception {
        //删除该文件下的所有残留数据
        FilePathCollections.clearUpresultOutputPathFile(FilePathCollections.stage05CrossValidationPath);

        //写入数据
        for(int i = 0; i < data_cv.size(); i++) {
            //将数据输出
            new File(FilePathCollections.stage05CrossValidationPath).mkdir();
            String trainOutPath = FilePathCollections.stage05CrossValidationPath + "/" + i +"-CV_train.dat";
            String testOutPath = FilePathCollections.stage05CrossValidationPath + "/" + i + "-CV_test.dat";
            BufferedWriter writerTrain = new BufferedWriter(new FileWriter(trainOutPath));
            BufferedWriter writerTest = new BufferedWriter(new FileWriter(testOutPath));

            ArrayList<String> dataTest = data_cv.get(i);
            for(String data : dataTest) {
                writerTest.write(data);
                writerTest.newLine();
                writerTest.flush();
            }

            for(int j = 0; j < data_cv.size(); j++) {
                if(i != j) {
                    ArrayList<String> dataTrain = data_cv.get(j);
                    for(String data : dataTrain) {
                        writerTrain.write(data);
                        writerTrain.newLine();
                        writerTrain.flush();
                    }
                }
            }

            writerTrain.close();
            writerTest.close();
        }
    }

    /**
     * 上传文件到HDFS文件系统
     */
    public static void uploadFileToHDFS() throws Exception {
        //删除HDFS中的残余数据
        FileStatus[] fileStatuses = HadoopUtil.listStatus(FilePathCollections.fileSystem, new Path(FilePathCollections.stage06HDFSTrainAndTestPath));

        for(FileStatus file : fileStatuses) {
            Path path = file.getPath();
            HadoopUtil.delete(new Configuration(), new Path(path.toString()));
        }

        System.out.println("Delete HDFS FileSystem Successfully......");

        //上传文件到HDFS中
        String outFile = FilePathCollections.stage05CrossValidationPath;
        File file = new File(outFile);
        for(File s : file.listFiles()) {
            String filePath = new File(s.getPath()).toString();
            FilePathCollections.fileSystem.copyFromLocalFile(new Path(filePath), new Path(FilePathCollections.stage06HDFSTrainAndTestPath));
        }

        System.out.println("Uploaded HDFS FileSystem Successfully......");
    }
}
