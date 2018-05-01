package cn.ccut;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * 分析具体异常主类
 *
 * @authorMr.Robot
 * @create2018-04-27 19:46
 */
public class SpecificAbnormality {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 设置job中的资源所在的jar包
        job.setJarByClass(SpecificAbnormality.class);

        // job使用的mapper类
        job.setMapperClass(AnalyzeMapper.class);
        // job使用的reducer类
        job.setReducerClass(AnalyzeReducer.class);
    }
}
