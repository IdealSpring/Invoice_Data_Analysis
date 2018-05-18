package cn.ccut.stage03;

import cn.ccut.common.AttributeUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

public class DetailsExtractionReducer extends Reducer<Text, DetailsExtraction, DetailsExtraction, NullWritable> {
    private DetailsExtraction detailsExtraction = new DetailsExtraction();
    //一个企业进项发票
    private TreeSet<InvoicesDetailsS3> inputDetailsSet = new TreeSet<>();
    //一个企业销项发票
    private TreeSet<InvoicesDetailsS3> outputDetailsSet = new TreeSet<>();
    //一个企业经营范围，商品编码范围
    private ArrayList<String> spbmRange = new ArrayList<>();

    @Override
    protected void reduce(Text key, Iterable<DetailsExtraction> values, Context context) throws IOException, InterruptedException {
        //数据初始化
        init(key, values);
        //16.inputAndOutputDeviation    --进销项偏离指数
        //17.inputInvoiceBusinessScope	--进项经营范围
        //18.outputInvoiceBusinessScope	--销项经营范围
        AttributeUtils.setInputAndOutputDeviation(detailsExtraction, inputDetailsSet, outputDetailsSet, spbmRange);

        /*if(!detailsExtraction.getInputInvoiceBusinessScope().equals("Null") && !detailsExtraction.getOutputInvoiceBusinessScope().equals("Null")) {
            context.write(detailsExtraction, NullWritable.get());
        }*/
        context.write(detailsExtraction, NullWritable.get());

        //清理残余数据
        inputDetailsSet.clear();
        outputDetailsSet.clear();
        spbmRange.clear();
        clearUpData();
    }

    /**
     * 数据初始化
     */
    private void init(Text key, Iterable<DetailsExtraction> values) {
        String nsr_id = key.toString();
        InvoicesDetailsS3 detailsS3 = null;
        String spbm = null;

        for(DetailsExtraction extraction : values) {
            String nsrId = extraction.getNsrId();
            spbm = extraction.getHydmLinkSpbm();
            detailsExtraction.setHydmLinkSpbm(spbm);

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

        if(spbm != null) {
            if(spbm.equals("other")) {
                spbmRange.add("101");
                spbmRange.add("102");
                spbmRange.add("103");
                spbmRange.add("104");
                spbmRange.add("105");
                spbmRange.add("106");
                spbmRange.add("107");
                spbmRange.add("108");
                spbmRange.add("109");
                spbmRange.add("110");

                spbmRange.add("201");
                spbmRange.add("202");
                spbmRange.add("203");
                spbmRange.add("204");

                spbmRange.add("301");
                spbmRange.add("302");
                spbmRange.add("303");
                spbmRange.add("304");
                spbmRange.add("305");
                spbmRange.add("306");
                spbmRange.add("307");

                spbmRange.add("401");
                spbmRange.add("402");
                spbmRange.add("403");
                spbmRange.add("404");
                spbmRange.add("405");
                spbmRange.add("406");

                spbmRange.add("501");
                spbmRange.add("502");
                spbmRange.add("503");
            } else {
                if(!spbm.equals("Null")) {
                    String[] split = spbm.split(",");

                    for(String s : split) {
                        spbmRange.add(s);
                    }
                }
            }
        }

        detailsExtraction.setNsrId(nsr_id);
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

        detailsExtraction.setInputAndOutputDeviation("Null");
        detailsExtraction.setInputInvoiceBusinessScope("Null");
        detailsExtraction.setOutputInvoiceBusinessScope("Null");
    }
}
