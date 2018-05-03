package cn.ccut.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.df.data.DescriptorException;
import org.apache.mahout.classifier.df.mapreduce.BuildForest;
import org.apache.mahout.classifier.df.mapreduce.TestForest;
import org.apache.mahout.classifier.df.tools.Describe;
import org.apache.mahout.common.HadoopUtil;

import java.io.IOException;
import java.util.Arrays;

public class Step123 {
    public static void main(String[] args) throws Exception {
        Step1Describe();
        Step2BuildForest();
        Step3TestForest();
    }

    /**
     * Step1：描述数据文件
     *
     * @throws IOException
     * @throws DescriptorException
     */
    public static void Step1Describe() throws IOException, DescriptorException {
        String[] args =new String[]{
                "-p", "hdfs://111.116.20.110:9000/user/hadoop/mahout/1-CV_train.dat",
                "-f", "hdfs://111.116.20.110:9000/user/hadoop/mahout/train.info",
                "-d", "I", "2", "C", "L"
        };

        HadoopUtil.delete(new Configuration(), new Path(args[Arrays.asList(args).indexOf("-f") + 1]));
        Describe.main(args);
    }

    /**
     * Step2：训练森林
     *
     * @throws Exception
     */
    private static void Step2BuildForest() throws Exception {
        String[] args = new String[]{
                "-d", "hdfs://111.116.20.110:9000/user/hadoop/mahout/1-CV_train.dat",
                "-ds", "hdfs://111.116.20.110:9000/user/hadoop/mahout/train.info",
                "-o", "hdfs://111.116.20.110:9000/user/hadoop/mahout/forest_result",
                "-sl", "1",
                "-p", "-t", "2"
        };

        HadoopUtil.delete(new Configuration(), new Path(args[Arrays.asList(args).indexOf("-o") + 1]));
        BuildForest.main(args);
    }

    private static void Step3TestForest() throws Exception {
        String[] args = new String[]{
                "-i", "hdfs://111.116.20.110:9000/user/hadoop/mahout/1-CV_test.dat",
                "-ds", "hdfs://111.116.20.110:9000/user/hadoop/mahout/train.info",
                "-o", "hdfs://111.116.20.110:9000/user/hadoop/mahout/predictions",
                "-m", "hdfs://111.116.20.110:9000/user/hadoop/mahout/forest_result",
                "-a"
        };

        HadoopUtil.delete(new Configuration(), new Path(args[Arrays.asList(args).indexOf("-o") + 1]));
        //TestForest.main(args);
        TestForest.main(args);
    }
}
