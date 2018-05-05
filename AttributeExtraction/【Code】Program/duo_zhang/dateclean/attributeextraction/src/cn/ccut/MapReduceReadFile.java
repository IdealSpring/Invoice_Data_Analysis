package cn.ccut;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

public class MapReduceReadFile {

    private static SequenceFile.Reader reader = null;
    private static Configuration conf = new Configuration();


    public static class ReadFileMapper extends
            Mapper<LongWritable, Text, LongWritable, Text> {


        /* (non-Javadoc)
         * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN, org.apache.hadoop.mapreduce.Mapper.Context)
         */
        @Override
        public void map(LongWritable key, Text value, Context context) {
            key = (LongWritable) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            value = (Text) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
            try {
                while (reader.next(key, value)) {
                    System.out.printf("%s\t%s\n", key, value);
                    context.write(key, value);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Job job = new Job(conf,"read seq file");
        job.setJarByClass(MapReduceReadFile.class);
        job.setMapperClass(ReadFileMapper.class);
        job.setMapOutputValueClass(Text.class);
        Path path = new Path("forest.seq");
        FileSystem fs = FileSystem.get(conf);
        reader = new SequenceFile.Reader(fs, path, conf);
        FileInputFormat.addInputPath(job, path);
        FileOutputFormat.setOutputPath(job, new Path("result"));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}