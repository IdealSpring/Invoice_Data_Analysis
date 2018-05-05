package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * reducer
 *
 * @authorMr.Robot
 * @create2018-04-27 19:51
 */
public class AnalyzeReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    private Enterprise enterprise = new Enterprise();
    // 进项发票
    private TreeSet<Invoice> inputInvoiceSet = new TreeSet<>();
    // 销项发票
    private TreeSet<Invoice> outputInvoiceSet = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {
        init(key, values);

        if (enterprise.isRetain()) {
            AnalyzeUtils.analyzeAbnormality(enterprise, inputInvoiceSet, outputInvoiceSet);
        }

        // 向文件输出
        if (enterprise.isRetain()) {
            context.write(enterprise, NullWritable.get());
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
        enterprise.setNsr_id("null");
        enterprise.setRetain(false);
        enterprise.setAbnormal("0");

        enterprise.setFp_nid("null");
        enterprise.setXf_id("null");
        enterprise.setGf_id("null");
        enterprise.setJshj(0);
        enterprise.setSe(0);
        enterprise.setJshj(0);
        enterprise.setKpyf("null");
        enterprise.setKprq("null");
        enterprise.setZfbz("null");

        enterprise.setInputInvoiceNum(0);
        enterprise.setOutputInvoiceNum(0);

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
        long inputInvoiceNum = 0;
        long outputInvoiceNum = 0;
        Invoice invoice;

        for (Enterprise value : values) {
            if (!value.isRetain()) {    // 若读取的数据是发票
                invoice = new Invoice();
/*                invoice.setFp_nid(e.getFp_nid());
                invoice.setXf_id(e.getXf_id());
                invoice.setGf_id(e.getGf_id());
                invoice.setJe(e.getJe());
                invoice.setSe(e.getSe());
                invoice.setJshj(e.getJshj());
                invoice.setKpyf(e.getKpyf());
                invoice.setKprq(e.getKprq());
                invoice.setZfbz(e.getZfbz());*/
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
                invoice.setInvoice(fp_nid, xf_id, gf_id, je, se, jshj, kpyf, kprq, zfbz);

//                invoice.setInvoice(value.getFp_nid(), value.getXf_id(), value.getGf_id(),
//                        value.getJe(), value.getSe(), value.getJshj(), value.getKpyf(),
//                        value.getKprq(), value.getZfbz());

                // 将发票信息给相应的企业
                // 对进销项进行分类
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
                enterprise.setAbnormal(value.getAbnormal());
            }
        }

        enterprise.setNsr_id(key.toString());
        enterprise.setInputInvoiceNum(inputInvoiceNum);
        enterprise.setOutputInvoiceNum(outputInvoiceNum);
    }
}
