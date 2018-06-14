package cn.ccut.stage02;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 处理发票map类
 */
public class InvoiceDetailsMapper extends Mapper<LongWritable, Text, Text, InvoiceDetails> {
    private InvoiceDetails custom = new InvoiceDetails();
    private static final Logger log = LoggerFactory.getLogger(InvoiceDetailsMapper.class);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = StringUtils.split(line, ",");

        if(split.length == 9 && line.contains("fpid") && (line.lastIndexOf(",N") == (line.length() - 2) || line.lastIndexOf(",Y") == (line.length() - 2))) {

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

            custom.setFp_nid(fp_nid);
            custom.setXf_id(xf_id);
            custom.setGf_id(gf_id);
            custom.setJe(je);
            custom.setSe(se);
            custom.setJshj(jshj);
            custom.setKpyf(kpyf);
            custom.setKprq(kprq);
            custom.setZfbz(zfbz);

            custom.setFp_Id(fp_nid);
            context.write(new Text(fp_nid), custom);
        } else {
            //过滤脏数据
            try {
                if(split.length == 10) {
                    //设置为发票明细
                    custom.setDetails(true);

                    //设置信息
                    String fp_nid = split[0];
                    String date_key = split[1];
                    String hwmc = split[2];
                    String ggxh = split[3];
                    String dw = split[4];

                    double sl = 0;
                    if(!"null".equals(split[5]) && !"\\N".equals(split[5])) {
                        sl = Double.parseDouble(split[5]);
                    }

                    double dj = 0;
                    if(!"null".equals(split[6]) && !"\\N".equals(split[6])) {
                        dj = Double.parseDouble(split[6]);
                    }

                    double je = 0;
                    if(!"null".equals(split[7]) && !"\\N".equals(split[7])) {
                        je = Double.parseDouble(split[7]);
                    }

                    double se = 0;
                    if(!"null".equals(split[8]) && !"\\N".equals(split[8])) {
                        se = Double.parseDouble(split[8]);
                    }

                    String spbm = split[9];

                    custom.setFp_Id(fp_nid);

                    custom.setFp_nidMX(fp_nid);
                    custom.setDate_keyMX(date_key);
                    custom.setHwmcMX(hwmc);
                    custom.setGgxhMX(ggxh);
                    custom.setDwMX(dw);
                    custom.setSlMX(sl);
                    custom.setDjMX(dj);
                    custom.setJeMX(je);
                    custom.setSeMX(se);
                    custom.setSpbmMX(spbm);

                    context.write(new Text(fp_nid), custom);
                }
            } catch (Exception e) {
                log.info("脏数据！脏数据！脏数据！");
            }

        }

        clearData();
    }

    /**
     * 处理类中残余数据
     */
    private void clearData() {
        custom.setFp_Id("Null");
        custom.setDetails(false);

        /**
         * 发票信息
         */
        custom.setFp_nid("Null");
        custom.setXf_id("Null");
        custom.setGf_id("Null");
        custom.setJe(0);
        custom.setSe(0);
        custom.setJshj(0);
        custom.setKpyf("Null");
        custom.setKprq("Null");
        custom.setZfbz("Null");

        /**
         * 发票明细
         */
        custom.setFp_nidMX("Null");
        custom.setDate_keyMX("Null");
        custom.setHwmcMX("Null");
        custom.setGgxhMX("Null");
        custom.setDwMX("Null");
        custom.setSlMX(0);
        custom.setDjMX(0);
        custom.setJeMX(0);
        custom.setSeMX(0);
        custom.setSpbmMX("Null");
    }

}
