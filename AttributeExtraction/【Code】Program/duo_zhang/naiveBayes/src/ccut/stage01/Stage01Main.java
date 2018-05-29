package ccut.stage01;

import ccut.common.FilePathCollections;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 第一阶段MapReduce主类
 *      提取前15个属性
 */
public class Stage01Main {
    private static final Logger log = LoggerFactory.getLogger(Stage01Main.class);

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        //向服务器提交执行
        Job job = Job.getInstance(conf, "MyFirstJob");

        job.setJarByClass(Stage01Main.class);
        job.setMapperClass(EnterpriesMapper.class);
        job.setReducerClass(EnterpriesReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Enterprise.class);

        job.setOutputKeyClass(Enterprise.class);
        job.setOutputValueClass(NullWritable.class);

        String nsrxxFilePath = FilePathCollections.nsrxxFilePath;
        String zzsfpFilePath = FilePathCollections.zzsfpFilePath;
        FileInputFormat.addInputPath(job, new Path(nsrxxFilePath));
        FileInputFormat.addInputPath(job, new Path(zzsfpFilePath));

        //删除原有文件
        FilePathCollections.clearUpresultOutputPathFile(FilePathCollections.stage01ResultOutputPath);

        FileOutputFormat.setOutputPath(job, new Path(FilePathCollections.stage01ResultOutputPath));

        job.waitForCompletion(true);
        log.info("Stage01Main(13个属性提取完毕) Successful......");
    }
}
