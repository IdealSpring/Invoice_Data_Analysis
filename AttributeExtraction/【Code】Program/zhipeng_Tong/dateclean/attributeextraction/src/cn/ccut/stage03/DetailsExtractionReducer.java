package cn.ccut.stage03;

import cn.ccut.common.AttributeUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Calendar;
import java.util.TreeSet;

public class DetailsExtractionReducer extends Reducer<Text, DetailsExtraction, DetailsExtraction, NullWritable> {
    private DetailsExtraction detailsExtraction = new DetailsExtraction();
    //一个企业进项发票
    private TreeSet<InvoicesDetailsS3> inputDetailsSet = new TreeSet<>();
    //一个企业销项发票
    private TreeSet<InvoicesDetailsS3> outputDetailsSet = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<DetailsExtraction> values, Context context) throws IOException, InterruptedException {
        //数据初始化
        init(key, values);
        //16.inputAndOutputDeviation    --进销项偏离指数
        AttributeUtils.setInputAndOutputDeviation(detailsExtraction, inputDetailsSet, outputDetailsSet);

        context.write(detailsExtraction, NullWritable.get());

        //清理残余数据
        inputDetailsSet.clear();
        outputDetailsSet.clear();
        clearUpData();
    }

    /**
     * 数据初始化
     */
    private void init(Text key, Iterable<DetailsExtraction> values) {
        String nsr_id = key.toString();
        InvoicesDetailsS3 detailsS3 = null;

        for(DetailsExtraction extraction : values) {
            String nsrId = extraction.getNsrId();

            String fp_nidMX = extraction.getFp_nidMX();
            String xf_id = extraction.getXf_id();
            String gf_id = extraction.getGf_id();
            String date_keyMX = extraction.getDate_keyMX();
            String hwmcMX = extraction.getHwmcMX();
            String ggxhMX = extraction.getGgxhMX();
            String dwMX = extraction.getDwMX();
            double slMX = extraction.getSlMX();
            double djMX = extraction.getDjMX();
            double jeMX = extraction.getJeMX();
            double seMX = extraction.getSeMX();
            String spbmMX = extraction.getSpbmMX();

            detailsS3 = new InvoicesDetailsS3();
            detailsS3.setFp_nidMX(fp_nidMX);

            Calendar date = Calendar.getInstance();
            int year = Integer.parseInt(date_keyMX.substring(0, 4));
            int month = Integer.parseInt(date_keyMX.substring(4)) - 1;
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            detailsS3.setDate_keyMX(date);

            detailsS3.setHwmcMX(hwmcMX);
            detailsS3.setGgxhMX(ggxhMX);
            detailsS3.setDwMX(dwMX);
            detailsS3.setSlMX(slMX);
            detailsS3.setDjMX(djMX);
            detailsS3.setJeMX(jeMX);
            detailsS3.setSeMX(seMX);
            detailsS3.setSpbmMX(spbmMX);

            if(nsrId.equals(gf_id)) {
                inputDetailsSet.add(detailsS3);
            }
            if(nsrId.equals(xf_id)) {
                outputDetailsSet.add(detailsS3);
            }
        }

        detailsExtraction.setNsrId(nsr_id);
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
