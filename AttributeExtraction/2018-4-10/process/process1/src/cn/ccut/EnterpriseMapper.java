package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class EnterpriseMapper extends Mapper<LongWritable, Text, Text, Enterprise> {
    private Enterprise enterprise = new Enterprise();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = StringUtils.split(line, ",");

        String inputNsrId = split[1];
        String outputNsrId = split[2];

        enterprise.setNsr_id(inputNsrId);
        enterprise.setFp_nid(split[0]);
        enterprise.setXf_id(split[1]);
        enterprise.setGf_id(split[2]);
        enterprise.setInovice(line);
        context.write(new Text(inputNsrId), enterprise);

        enterprise.setNsr_id(outputNsrId);
        context.write(new Text(outputNsrId), enterprise);
    }
}
