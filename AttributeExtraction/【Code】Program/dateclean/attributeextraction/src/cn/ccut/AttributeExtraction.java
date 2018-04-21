package cn.ccut;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.util.UUID;

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
        FileInputFormat.setInputPaths(job, "C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\信息");

        String s = UUID.randomUUID().toString();

        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\zhipeng-Tong\\Desktop\\异常企业资料\\result\\" + s));

        job.waitForCompletion(true);
    }
}
