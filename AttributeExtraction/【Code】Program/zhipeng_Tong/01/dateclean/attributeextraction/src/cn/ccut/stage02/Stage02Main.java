package cn.ccut.stage02;

import cn.ccut.common.FilePathCollections;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 第二阶段
 *      数据处理，将hwmx中添加发票个销方还有购方ID
 */

public class Stage02Main {
    private static final Logger log = LoggerFactory.getLogger(Stage02Main.class);

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MySecondJob");

        job.setJarByClass(Stage02Main.class);
        job.setMapperClass(InvoiceDetailsMapper.class);
        job.setReducerClass(InvoiceDetailsReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(InvoiceDetails.class);

        job.setOutputKeyClass(InvoiceDetails.class);
        job.setOutputValueClass(NullWritable.class);

        //输入文件路径
        String zzsfpFilePath = FilePathCollections.zzsfpFilePath;
        String hwmxFilePath = FilePathCollections.hwmxFilePath;
        FileInputFormat.addInputPath(job, new Path(zzsfpFilePath));
        FileInputFormat.addInputPath(job, new Path(hwmxFilePath));

        //输出文件路径
        FilePathCollections.clearUpresultOutputPathFile(FilePathCollections.stage02ResultOutputPath);

        FileOutputFormat.setOutputPath(job, new Path(FilePathCollections.stage02ResultOutputPath));

        job.waitForCompletion(true);
        log.info("Stage02Main(hwmx表与zzsfp表xf和gf字段连接完毕) Successful......");
    }
}
