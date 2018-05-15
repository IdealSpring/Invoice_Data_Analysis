package cn.ccut.stage03;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DetailsExtractionMapper extends Mapper<LongWritable, Text, Text, DetailsExtraction> {
    private DetailsExtraction detailsExtraction = new DetailsExtraction();
    private static final Logger log = LoggerFactory.getLogger(DetailsExtractionMapper.class);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] split = StringUtils.split(line, ",");

        try {
            if(split.length == 12) {
                //设置信息
                String fp_nid = split[0];
                String xf_id = split[1];
                String gf_id = split[2];
                String date_key = split[3];
                String hwmc = split[4];
                String ggxh = split[5];
                String dw = split[6];

                double sl = 0;
                if(!"null".equals(split[7]) && !"\\N".equals(split[7])) {
                    sl = Double.parseDouble(split[7]);
                }

                double dj = 0;
                if(!"null".equals(split[8]) && !"\\N".equals(split[8])) {
                    dj = Double.parseDouble(split[8]);
                }

                double je = 0;
                if(!"null".equals(split[9]) && !"\\N".equals(split[9])) {
                    je = Double.parseDouble(split[9]);
                }

                double se = 0;
                if(!"null".equals(split[10]) && !"\\N".equals(split[10])) {
                    se = Double.parseDouble(split[10]);
                }

                String spbm = split[11];

                detailsExtraction.setFp_nidMX(fp_nid);
                detailsExtraction.setXf_id(xf_id);
                detailsExtraction.setGf_id(gf_id);
                detailsExtraction.setDate_keyMX(date_key);
                detailsExtraction.setHwmcMX(hwmc);
                detailsExtraction.setGgxhMX(ggxh);
                detailsExtraction.setDwMX(dw);
                detailsExtraction.setSlMX(sl);
                detailsExtraction.setDjMX(dj);
                detailsExtraction.setJeMX(je);
                detailsExtraction.setSeMX(se);
                detailsExtraction.setSpbmMX(spbm);

                detailsExtraction.setNsrId(split[1]);
                context.write(new Text(split[1]), detailsExtraction);

                detailsExtraction.setNsrId(split[2]);
                context.write(new Text(split[2]), detailsExtraction);
            }
        } catch (Exception e) {
            log.info("脏数据！脏数据！脏数据！");
        }

        clearUpData();
    }

    /**
     * 残留数据清理
     */
    public void clearUpData() {
        detailsExtraction.setNsrId("Null");

        detailsExtraction.setFp_nidMX("Null");
        detailsExtraction.setXf_id("Null");
        detailsExtraction.setGf_id("Null");
        detailsExtraction.setDate_keyMX("Null");
        detailsExtraction.setHwmcMX("Null");
        detailsExtraction.setGgxhMX("Null");
        detailsExtraction.setDwMX("Null");
        detailsExtraction.setSlMX(0);
        detailsExtraction.setDjMX(0);
        detailsExtraction.setJeMX(0);
        detailsExtraction.setSeMX(0);
        detailsExtraction.setSpbmMX("Null");
    }

}
