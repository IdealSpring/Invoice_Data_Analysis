package cn.ccut;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.TreeSet;

/**
 * reducer
 *
 * @authorMr.Robot
 * @create2018-04-27 19:51
 */
public class AnalyzeReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    private Enterprise enterprise = new Enterprise();
    private TreeSet<Invoice> inputInvoiceSet = new TreeSet<>();
    private TreeSet<Invoice> outputInvoiceSet = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {
        init(key, values);

        AnalyzeUtils.test(enterprise, inputInvoiceSet, outputInvoiceSet);

        // 向文件输出
        if (enterprise.isRetain()) {
            context.write(enterprise, NullWritable.get());
        }

        // 数据初始化
        clean();
        this.inputInvoiceSet.clear();
        this.outputInvoiceSet.clear();
    }



    private void clean() {
        enterprise.setNsr_id("null");
        enterprise.setRetain(false);
        enterprise.setAbnormal("0");

        enterprise.setFp_nid("null");
        enterprise.setXf_id("null");
        enterprise.setGf_id("null");
        enterprise.setJshj('0');
        enterprise.setSe('0');
        enterprise.setJshj('0');
        enterprise.setKpyf("null");
        enterprise.setKprq(null);
        enterprise.setZfbz("null");

        enterprise.setInputInvoiceNum('0');
        enterprise.setOutputInvoiceNum('0');

        enterprise.setNoEntryInvoice("");
        enterprise.setNoOutputInvoice("");
        enterprise.setLongTimeNoEntry("");
        enterprise.setLongTimeNoOutput("");
        enterprise.setTaxChange("");
        enterprise.setInvoiceUsageChange("");
        enterprise.setInvoiceInvalidRate("");
        enterprise.setLossWarning("");
    }

    /**
     * 分离发票信息
     *
     * @param key
     * @param enterprises
     */
    private void init(Text key, Iterable<Enterprise> enterprises) {
        long inputInvoiceNum = 0;
        long outPutInvoiceNum = 0;

        Invoice invoice;

        for (Enterprise e : enterprises) {
            if (!e.isRetain()) {    // 若读取的数据是发票
                invoice = new Invoice();
                invoice.setFp_nid(e.getFp_nid());
                invoice.setXf_id(e.getXf_id());
                invoice.setGf_id(e.getGf_id());
                invoice.setJe(e.getJe());
                invoice.setSe(e.getSe());
                invoice.setJshj(e.getJshj());
                invoice.setKpyf(e.getKpyf());
                invoice.setKprq(e.getKprq());
                invoice.setZfbz(e.getZfbz());

                // 将发票信息给相应的企业
                // 对进销项进行分类
                if (e.getNsr_id().equals(invoice.getGf_id())) {
                    this.inputInvoiceSet.add(invoice);
                    inputInvoiceNum++;
                } else if (e.getNsr_id().equals(invoice.getXf_id())) {
                    this.outputInvoiceSet.add(invoice);
                    outPutInvoiceNum++;
                }
            } else {
                enterprise.setRetain(true);
                enterprise.setAbnormal(e.getAbnormal());
            }
        }
        enterprise.setNsr_id(key.toString());
        enterprise.setInputInvoiceNum(inputInvoiceNum);
        enterprise.setOutputInvoiceNum(outPutInvoiceNum);
    }
}
