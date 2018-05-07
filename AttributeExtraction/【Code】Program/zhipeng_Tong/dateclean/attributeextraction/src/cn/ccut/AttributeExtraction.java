package cn.ccut;

import cn.ccut.mahout.Step123;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.mahout.common.HadoopUtil;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.List;

/**
 * MapReduce程序,属性提取主类
 */
public class AttributeExtraction {
    private static FileSystem fileSystem ;

    static {
        try {
            Configuration conf = new Configuration();
            fileSystem = FileSystem.get(new URI("hdfs://111.116.20.110:9000/"), conf, "hadoop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        String inputDataPath = "C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\信息3";
        FileInputFormat.setInputPaths(job, inputDataPath);

        String s = UUID.randomUUID().toString();
        String outputDataPath = "C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\result\\" + s;

        FileOutputFormat.setOutputPath(job, new Path(outputDataPath));

        job.waitForCompletion(true);

        if(inputDataPath.contains("3")) {
            upDateTrainAndTest(outputDataPath);
        }

        //上传文件到HDFS文件系统
        uploadFileToHDFS();

        //测试
        Step123.main(new String[]{}, fileSystem);
    }

    /**
     * 上传文件到HDFS文件系统
     */
    private static void uploadFileToHDFS() throws Exception {
        //删除HDFS中的残余数据
        FileStatus[] fileStatuses = HadoopUtil.listStatus(fileSystem, new Path("/user/hadoop/mahout_IdealSpring"));

        for(FileStatus file : fileStatuses) {
            Path path = file.getPath();
            HadoopUtil.delete(new Configuration(), new Path(path.toString()));
        }

        System.out.println("Delete HDFS FileSystem Successfully......");

        //上传文件到HDFS中
        String outFile = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out";
        File file = new File(outFile);
        for(File s : file.listFiles()) {
            String filePath = new File(s.getPath()).toString();
            fileSystem.copyFromLocalFile(new Path(filePath), new Path("/user/hadoop/mahout_IdealSpring/"));
        }

        System.out.println("Uploaded HDFS FileSystem Successfully......");
    }


    /**
     * 更新训练集和测试集
     *
     * @param path
     * @throws Exception
     */
    private static void upDateTrainAndTest(String path) throws Exception {
        path = path + "\\" + "part-r-00000";
        ArrayList<String> originalData = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            originalData.add(line);
        }

        //交叉验证, 所打则数5
        double crossNum = 15;
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
    }

    private static void writeToFile(ArrayList<ArrayList<String>> data_cv) throws Exception {
        //删除该文件下的所有残留数据
        String outFile = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out";
        File file = new File(outFile);
        for(File s : file.listFiles()) {
            new File(s.getPath()).delete();
        }

        //写入数据
        for(int i = 0; i < data_cv.size(); i++) {
            //将数据输出
            String trainOutPath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out/" + i +"-CV_train.dat";
            String testOutPath = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/out/" + i +"-CV_test.dat";
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

}
