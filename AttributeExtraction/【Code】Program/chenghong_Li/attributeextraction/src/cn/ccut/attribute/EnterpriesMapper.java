package cn.ccut.attribute;

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

        if(split.length == 6 && !line.contains("fpid")) {
            enterprise.setNsr_id(split[1]);
            enterprise.setRetain(true);
            enterprise.setLabel(split[5]);

            context.write(new Text(split[1]), enterprise);
        } else if(split.length == 9 && line.contains("fpid") && (line.lastIndexOf(",N") == (line.length() - 2) || line.lastIndexOf(",Y") == (line.length() - 2))) {

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

        cleanUp(enterprise);
    }

    /**
     * 清理enterprise残留信息
     *
     * @param enterprise
     */
    private void cleanUp(Enterprise enterprise) {
        enterprise.setNsr_id("Null");
        enterprise.setRetain(false);

        /**
         * 发票信息
         */
        enterprise.setFp_nid("Null");
        enterprise.setXf_id("Null");
        enterprise.setGf_id("Null");
        enterprise.setJe(0);
        enterprise.setSe(0);
        enterprise.setJshj(0);
        enterprise.setKpyf("Null");
        enterprise.setKprq("Null");
        enterprise.setZfbz("Null");

        /**
         * 发票明细
         */
        enterprise.setFp_nidMX("Null");
        enterprise.setDate_keyMX("Null");
        enterprise.setHwmcMX("Null");
        enterprise.setGgxhMX("Null");
        enterprise.setDwMX("Null");
        enterprise.setSlMX(0);
        enterprise.setDjMX(0);
        enterprise.setJeMX(0);
        enterprise.setSeMX(0);
        enterprise.setSpbmMX("Null");

        enterprise.setInputInvoiceNum(0);
        enterprise.setOutputInvoiceNum(0);

        enterprise.setInputInvoice("Null");
        enterprise.setOutputInvoice("Null");
        enterprise.setInputInterval("Null");
        enterprise.setOutputInterval("Null");
        enterprise.setTaxChangeRate("Null");
        enterprise.setInvoiceUsageChange("Null");
        enterprise.setInputTaxAndOutputTaxRatio("Null");
        enterprise.setInvoiceInvalidRatio("Null");
        enterprise.setContinuousLoss("Null");
        enterprise.setInvoiceBalance("Null");

        enterprise.setLabel("0");
    }
}
