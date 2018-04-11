package cn.ccut;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class EnterpriseCount {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MyFirstJob");

        job.setJarByClass(EnterpriseCount.class);
        job.setMapperClass(EnterpriseMapper.class);
        job.setReducerClass(EnterpriseReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Enterprise.class);

        job.setOutputKeyClass(Enterprise.class);
        job.setOutputValueClass(NullWritable.class);

        //FileInputFormat.setInputPaths(job, args[0]);
        //FileOutputFormat.setOutputPath(job, new Path(args[1]));
        FileInputFormat.setInputPaths(job, args[0]);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
