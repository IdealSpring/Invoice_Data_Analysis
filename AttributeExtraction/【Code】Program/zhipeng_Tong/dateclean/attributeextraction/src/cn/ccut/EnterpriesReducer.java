package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * mapper程序
 */
public class EnterpriesReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    //实体企业
    private Enterprise enterprise = new Enterprise();
    //一个企业进项发票
    private TreeSet<Invoice> inputInvoiceSet = new TreeSet<>();
    //一个企业销项发票
    private TreeSet<Invoice> outputInvoiceSet = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {
        //System.out.println("程序调通！");
        //对数据初始化
        init(key, values);
        //1.inputInvoice、2.outputInvoice两个属性
        AttributeUtils.setInputAndOutputInoive(enterprise);
        //3.inputInterval 	--最近两次进项开票时间
        AttributeUtils.setInputInterval(enterprise, inputInvoiceSet);
        //4.outputInterval 	--最近两次销项开票时间
        AttributeUtils.setOutputInterval(enterprise, outputInvoiceSet);
        //5.taxChangeRate		--税负变动率
        AttributeUtils.setTaxChangeRate(enterprise, inputInvoiceSet, outputInvoiceSet);
        //6.invoiceUsageChange  --发票用量变动
        AttributeUtils.setInvoiceUsageChange(enterprise, inputInvoiceSet, outputInvoiceSet);
        //7.inputTaxAndOutputTaxRatio   --进项税额变动率高于销项税额变动率
        AttributeUtils.setInputTaxAndOutputTaxRatio(enterprise, inputInvoiceSet, outputInvoiceSet);
        //8.invoiceInvalidRatio --发票作废率
        AttributeUtils.setInvoiceInvalidRatio(enterprise, inputInvoiceSet, outputInvoiceSet);
        //9.setContinuousLoss        --发票显示连续亏损
        AttributeUtils.setContinuousLoss(enterprise, inputInvoiceSet, outputInvoiceSet);
        //10.invoiceBalance     --进销项差额
        AttributeUtils.setInvoiceBalance(enterprise, inputInvoiceSet, outputInvoiceSet);
        //11.inputInvoiceInvalid
        AttributeUtils.setInputInvoiceInvalid(enterprise, inputInvoiceSet);
        //12.outputInvoiceInvalid
        AttributeUtils.setOutputInvoiceInvalid(enterprise, outputInvoiceSet);

        //写出
        if(enterprise.isRetain()) {
            context.write(enterprise, NullWritable.get());
        }

        //清空enterprise残留信息
        cleanUp(enterprise);
        //每次重新调用清理TreeSet
        this.inputInvoiceSet.clear();
        this.outputInvoiceSet.clear();
    }

    /**
     * 清理enterprise残留信息
     *
     * @param enterprise
     */
    private void cleanUp(Enterprise enterprise) {
        enterprise.setNsr_id("Null");
        enterprise.setRetain(false);

        enterprise.setFp_nid("Null");
        enterprise.setXf_id("Null");
        enterprise.setGf_id("Null");
        enterprise.setJe(0);
        enterprise.setSe(0);
        enterprise.setJshj(0);
        enterprise.setKpyf("Null");
        enterprise.setKprq("Null");
        enterprise.setZfbz("Null");

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

    /**
     * 对数据初始化
     * @param values
     */
    private void init(Text key, Iterable<Enterprise> values) {
        long inputInvoiceNum = 0;
        long outputInvoiceNum = 0;
        //发票数据
        Invoice invoice;

        for(Enterprise value : values) {
            if(!value.isRetain()) {
                //分离发票信息
                invoice = new Invoice();

                String fp_nid = value.getFp_nid();
                String xf_id = value.getXf_id();
                String gf_id = value.getGf_id();
                double je = value.getJshj();
                double se = value.getSe();
                double jshj = value.getJshj();
                String kpyf = value.getKpyf();
                Calendar kprq = Calendar.getInstance();
                String[] date = StringUtils.split(value.getKprq(), "-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]) - 1;
                int day = Integer.parseInt(date[2]);
                kprq.set(year, month, day);
                String zfbz = value.getZfbz();

                invoice.setParas(fp_nid, xf_id, gf_id, je, se, jshj, kpyf, kprq, zfbz);

                //对进销项发票进项分类
                if(value.getNsr_id().equals(invoice.getGf_id())) {
                    this.inputInvoiceSet.add(invoice);
                }
                if(value.getNsr_id().equals(invoice.getXf_id())) {
                    this.outputInvoiceSet.add(invoice);
                }

                if(value.getGf_id().equals(value.getNsr_id())) {
                    inputInvoiceNum ++;
                }
                if(value.getXf_id().equals(value.getNsr_id())) {
                    outputInvoiceNum ++;
                }
            } else {
                enterprise.setRetain(true);
                enterprise.setLabel(value.getLabel());
            }
        }

        enterprise.setNsr_id(key.toString());
        enterprise.setInputInvoiceNum(inputInvoiceNum);
        enterprise.setOutputInvoiceNum(outputInvoiceNum);
    }
}









