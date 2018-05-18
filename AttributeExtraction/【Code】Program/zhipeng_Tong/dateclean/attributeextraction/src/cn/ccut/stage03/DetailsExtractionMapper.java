package cn.ccut.stage03;

import cn.ccut.common.FilePathCollections;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class DetailsExtractionMapper extends Mapper<LongWritable, Text, Text, DetailsExtraction> {
    private DetailsExtraction detailsExtraction = new DetailsExtraction();
    private static final Logger log = LoggerFactory.getLogger(DetailsExtractionMapper.class);
    private HashMap<String, String> nsrIdLinkSpbm = new HashMap<>();

    //加载数据字典
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        HashMap<String, String> nsrIdLinkHydm = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(FilePathCollections.nsrxxFilePath));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            nsrIdLinkHydm.put(split[1], split[0].substring(0, 2));
        }
        reader.close();

        HashMap<String, String> hydmLinkSpbm = new HashMap<>();
        BufferedReader reader2 = new BufferedReader(new FileReader(FilePathCollections.hydm_link_spbmFilePath));
        while ((line = reader2.readLine()) != null) {
            String[] split = line.split(" ");
            hydmLinkSpbm.put(split[0], split[1]);
        }
        reader2.close();

        Set<String> nsrIdLinkHydmSet = nsrIdLinkHydm.keySet();
        Set<String> hydmLinkSpbmSet = hydmLinkSpbm.keySet();
        for(String nsrId : nsrIdLinkHydmSet) {
            String hydmNsr = nsrIdLinkHydm.get(nsrId);

            for(String hydm : hydmLinkSpbmSet) {
                if(hydmNsr.equals(hydm)) {
                    nsrIdLinkSpbm.put(nsrId, hydmLinkSpbm.get(hydm));
                }
            }
        }
    }

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
                //设置行业代码
                Set<String> nsrIdLinkHydmSet = nsrIdLinkSpbm.keySet();
                for(String nsrIdkey : nsrIdLinkHydmSet) {
                    if(nsrIdkey.equals(split[1])) {
                        String spbmSize = nsrIdLinkSpbm.get(nsrIdkey);
                        detailsExtraction.setHydmLinkSpbm(spbmSize);
                    }
                }
                context.write(new Text(split[1]), detailsExtraction);

                detailsExtraction.setNsrId(split[2]);
                //设置行业代码
                detailsExtraction.setHydmLinkSpbm("Null");
                Set<String> nsrIdLinkHydmSet2 = nsrIdLinkSpbm.keySet();
                for(String nsrIdkey : nsrIdLinkHydmSet2) {
                    if(nsrIdkey.equals(split[2])) {
                        String spbmSize = nsrIdLinkSpbm.get(nsrIdkey);
                        detailsExtraction.setHydmLinkSpbm(spbmSize);
                        //nsrIdLinkSpbm.remove(nsrIdkey);
                    }
                }
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
        detailsExtraction.setHydmLinkSpbm("Null");

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
