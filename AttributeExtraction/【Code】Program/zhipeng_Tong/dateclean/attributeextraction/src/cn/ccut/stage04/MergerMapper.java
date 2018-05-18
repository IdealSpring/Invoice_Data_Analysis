package cn.ccut.stage04;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MergerMapper extends Mapper<LongWritable, Text, Text, MergerTwo> {
    private MergerTwo mergerTwo = new MergerTwo();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = line.split(",");

        //if(split.length == 17) {
        if(split.length > 10) {
            mergerTwo.setMain(true);
            mergerTwo.setMainFields(line);

            context.write(new Text(split[0]), mergerTwo);
        //} else if(split.length == 4) {
        } else if(split.length < 10) {
            mergerTwo.setSecondaryFields(line);

            context.write(new Text(split[0]), mergerTwo);
        }

        clearUp();
    }

    private void clearUp() {
        mergerTwo.setMain(false);
        mergerTwo.setMainFields("Null");
        mergerTwo.setSecondaryFields("Null");
        mergerTwo.setResultFields("Null");
    }

}
