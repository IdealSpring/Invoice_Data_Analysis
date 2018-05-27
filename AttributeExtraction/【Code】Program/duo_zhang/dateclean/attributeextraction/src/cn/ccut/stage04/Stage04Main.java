package cn.ccut.stage04;

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
 * 第四阶段MapReduce：
 *
 *  将前15个属性和hwmx中提取的属性合并
 */
public class Stage04Main {
    private static final Logger log = LoggerFactory.getLogger(Stage04Main.class);

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MyThirdJob");

        job.setJarByClass(Stage04Main.class);
        job.setMapperClass(MergerMapper.class);
        job.setReducerClass(MergerReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(MergerTwo.class);

        job.setOutputKeyClass(MergerTwo.class);
        job.setOutputValueClass(NullWritable.class);

        //输入文件路径
        String stage01ResultOutputPath = FilePathCollections.stage01ResultOutputPath + "/part-r-00000";
        String stage03ResultOutputPath = FilePathCollections.stage03ResultOutputPath + "/part-r-00000";

        FileInputFormat.addInputPath(job, new Path(stage01ResultOutputPath));
        FileInputFormat.addInputPath(job, new Path(stage03ResultOutputPath));

        //输出文件路径
        FilePathCollections.clearUpresultOutputPathFile(FilePathCollections.stage04ResultOutputPath);

        FileOutputFormat.setOutputPath(job, new Path(FilePathCollections.stage04ResultOutputPath));

        job.waitForCompletion(true);
        log.info("Stage04Main(所有属性提取完毕) Successful......");
    }
}
