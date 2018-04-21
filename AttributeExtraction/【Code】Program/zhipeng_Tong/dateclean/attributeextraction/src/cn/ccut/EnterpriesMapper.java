package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Mapper程序
 */
public class EnterpriesMapper extends Mapper<LongWritable, Text, Text, Enterprise> {
    private static final Logger log = LoggerFactory.getLogger(EnterpriesMapper.class);
    private Enterprise enterprise = new Enterprise();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = StringUtils.split(line, ",");

        if(split.length == 6) {
            enterprise.setNsr_id(split[1]);
            enterprise.setRetain(true);
            enterprise.setLabel(split[5]);

            context.write(new Text(split[1]), enterprise);
        } else if(split.length == 9) {

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
        } else {
            System.out.println("有漏网之鱼！");
            log.info("有漏网之鱼！");
        }


    }
}
