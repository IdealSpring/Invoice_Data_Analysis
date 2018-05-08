package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.*;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * reducer
 *
 * @authorMr.Robot
 * @create2018-04-27 19:51
 */
public class AnalyzeReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    // 企业
    private Enterprise enterprise = new Enterprise();
    // 企业的进项发票
    private TreeSet<Invoice> inputInvoiceSet = new TreeSet<>();
    // 企业的销项发票
    private TreeSet<Invoice> outputInvoiceSet = new TreeSet<>();


    public static final String NO_INPUT_INVOICE = " 无进项发票";

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {
        // 企业信息初始化
        init(key, values);

        if (enterprise.isRetain() && enterprise.getAbnormal().equals("1")) {
            // 分析原因
            AnalyzeUtils.analyzeAbnormality(enterprise, inputInvoiceSet, outputInvoiceSet);

            // 所属行业及注册类型
            AnalyzeUtils.industryAndType(enterprise);

            // 向文件输出
            context.write(enterprise, NullWritable.get());

            // 异常分类
            Classification.abnormalClassification(enterprise);
        }

        // 清空数据
        clean(enterprise);
        this.inputInvoiceSet.clear();
        this.outputInvoiceSet.clear();
    }


    /**
     * 清空数据
     */
    private void clean(Enterprise enterprise) {
        enterprise.setHydm("null");
        enterprise.setNsr_id("null");
        enterprise.setDjzclx_dm("null");
        enterprise.setKydjrq("null");
        enterprise.setXgrq("null");
        enterprise.setRetain(false);
        enterprise.setAbnormal("0");
        enterprise.setHy("null");
        enterprise.setDjzclx("null");

        enterprise.setFp_nid("null");
        enterprise.setXf_id("null");
        enterprise.setGf_id("null");
        enterprise.setJshj(0);
        enterprise.setSe(0);
        enterprise.setJshj(0);
        enterprise.setKpyf("null");
        enterprise.setKprq("null");
        enterprise.setZfbz("null");

        enterprise.setInvoiceUsage("");
        enterprise.setTaxChange("");
        enterprise.setInvoiceUsageChange("");
        enterprise.setInvoiceInvalidRate("");
        enterprise.setLossWarning("");
    }

    /**
     * 分离发票信息
     *
     * @param key
     * @param values
     */
    private void init(Text key, Iterable<Enterprise> values) {
        Invoice invoice;

        for (Enterprise value : values) {
            if (!value.isRetain()) {    // 若读取的数据是发票
                invoice = new Invoice();

                // 将kprq转换为Calendar格式
                Calendar kprq = Calendar.getInstance();
                String[] date = StringUtils.split(value.getKprq(), "-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]) - 1;
                int day = Integer.parseInt(date[2]);
                kprq.set(year, month, day);

                invoice.setInvoice(value.getFp_nid(), value.getXf_id(), value.getGf_id(),
                        value.getJe(), value.getSe(), value.getJshj(), value.getKpyf(),
                        kprq, value.getZfbz());

                // 将发票信息给相应的企业
                // 对进销项进行分类
                if(value.getNsr_id().equals(invoice.getGf_id())) {
                    this.inputInvoiceSet.add(invoice);
                }
                if(value.getNsr_id().equals(invoice.getXf_id())) {
                    this.outputInvoiceSet.add(invoice);
                }
            } else {
                enterprise.setHydm(value.getHydm());
                enterprise.setDjzclx_dm(value.getDjzclx_dm());
                enterprise.setKydjrq(value.getKydjrq());
                enterprise.setXgrq(value.getXgrq());
                enterprise.setAbnormal(value.getAbnormal());
                enterprise.setRetain(true);
            }
        }
        enterprise.setNsr_id(key.toString());
    }
}
