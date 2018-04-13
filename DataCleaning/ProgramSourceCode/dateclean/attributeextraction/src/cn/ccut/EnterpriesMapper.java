package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Mapper程序
 */
public class EnterpriesMapper extends Mapper<LongWritable, Text, Text, Enterprise> {
    private Enterprise enterprise = new Enterprise();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = StringUtils.split(line, ",");

        //设置信息
        String fp_nid = split[0];
        String xf_id = split[1];
        String gf_id = split[2];
        double je = Double.parseDouble(split[3]);
        double se = Double.parseDouble(split[4]);
        double jshj = Double.parseDouble(split[5]);
        String kpyf = split[6];
        String kprq = split[7];
        String zfbz = split[8];

        enterprise.setFp_nid(fp_nid);
        enterprise.setXf_id(xf_id);
        enterprise.setGf_id(gf_id);
        enterprise.setJe(je);
        enterprise.setSe(se);
        enterprise.setJshj(jshj);
        enterprise.setKpyf(kpyf);
        enterprise.setKprq(kprq);
        enterprise.setZfbz(zfbz);

        //将一张发票根据销方，购方变为两张发票,并输出
        enterprise.setNsr_id(xf_id);
        context.write(new Text(xf_id), enterprise);

        enterprise.setNsr_id(gf_id);
        context.write(new Text(gf_id), enterprise);
    }
}
