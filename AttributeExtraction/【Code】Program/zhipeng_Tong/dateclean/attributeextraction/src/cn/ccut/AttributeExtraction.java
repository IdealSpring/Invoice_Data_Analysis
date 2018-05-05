package cn.ccut;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.*;
import java.util.*;

/**
 * MapReduce程序,属性提取主类
 */
public class AttributeExtraction {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MyFirstJob");

        job.setJarByClass(AttributeExtraction.class);
        job.setMapperClass(EnterpriesMapper.class);
        job.setReducerClass(EnterpriesReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Enterprise.class);

        job.setOutputKeyClass(Enterprise.class);
        job.setOutputValueClass(NullWritable.class);

        //FileInputFormat.setInputPaths(job, args[0]);
        //FileOutputFormat.setOutputPath(job, new Path(args[1]));
        String inputDataPath = "C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\信息3";
        FileInputFormat.setInputPaths(job, inputDataPath);

        String s = UUID.randomUUID().toString();
        String outputDataPath = "C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\result\\" + s;

        FileOutputFormat.setOutputPath(job, new Path(outputDataPath));

        job.waitForCompletion(true);

        if(inputDataPath.contains("3")) {
            upDateTrainAndTest(outputDataPath);
        }
    }

    private static void upDateTrainAndTest(String path) throws Exception {
        path = path + "\\" + "part-r-00000";
        List<String> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            list.add(line);
        }

        List<String> data_CV_1 = new ArrayList<>();
        List<String> data_CV_2 = new ArrayList<>();
        List<String> data_CV_3 = new ArrayList<>();
        List<String> data_CV_4 = new ArrayList<>();
        List<String> data_CV_5 = new ArrayList<>();
        HashMap<Integer, List<String>> map = new HashMap<>();
        map.put(1, data_CV_1);
        map.put(2, data_CV_2);
        map.put(3, data_CV_3);
        map.put(4, data_CV_4);
        map.put(5, data_CV_5);

        List<Integer> sampleRandomNum_CV_1 = new ArrayList<>();
        List<Integer> sampleRandomNum_CV_2 = new ArrayList<>();
        List<Integer> sampleRandomNum_CV_3 = new ArrayList<>();
        List<Integer> sampleRandomNum_CV_4 = new ArrayList<>();
        List<Integer> sampleRandomNum_CV_5 = new ArrayList<>();

        List<Integer> randomNum = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            randomNum.add(i);
        }

        //取出总数的0.7作为训练数据
        int selectSample = (int) (list.size() / 5.0);

        setSelectSampleNum(selectSample, randomNum, sampleRandomNum_CV_1);
        setSelectSampleNum(selectSample, randomNum, sampleRandomNum_CV_2);
        setSelectSampleNum(selectSample, randomNum, sampleRandomNum_CV_3);
        setSelectSampleNum(selectSample, randomNum, sampleRandomNum_CV_4);
        setSelectSampleNum(selectSample, randomNum, sampleRandomNum_CV_5);

        //取出
        setDataSet(data_CV_1, sampleRandomNum_CV_1, list);
        setDataSet(data_CV_2, sampleRandomNum_CV_2, list);
        setDataSet(data_CV_3, sampleRandomNum_CV_3, list);
        setDataSet(data_CV_4, sampleRandomNum_CV_4, list);
        setDataSet(data_CV_5, sampleRandomNum_CV_5, list);


        for(int i = 1; i <= 5; i++) {
            //将数据输出
            String trainOutPath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out/" + i +"-CV_train.dat";
            String testOutPath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out/" + i +"-CV_test.dat";
            BufferedWriter writerTrain = new BufferedWriter(new FileWriter(trainOutPath));
            BufferedWriter writerTest = new BufferedWriter(new FileWriter(testOutPath));

            for(String s : map.get(i)) {
                writerTest.write(s);
                writerTest.newLine();
                writerTest.flush();
            }

            for(int j = 1; j <= 5; j++) {
                if(i != j) {
                    for(String s : map.get(j)) {
                        writerTrain.write(s);
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
     * 根据索引去除数据
     *
     * @param data_cv_1
     * @param sampleRandomNum_cv_1
     * @param list
     */
    private static void setDataSet(List<String> data_cv_1, List<Integer> sampleRandomNum_cv_1, List<String> list) {
        for(int number : sampleRandomNum_cv_1) {
            data_cv_1.add(list.get(number));
        }
    }

    /**
     * 设置要取出的数据索引
     *
     * @param selectSample
     * @param randomNum
     * @param sampleRandomNum_CV
     */
    private static void setSelectSampleNum(int selectSample, List<Integer> randomNum, List<Integer> sampleRandomNum_CV) {
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
}
