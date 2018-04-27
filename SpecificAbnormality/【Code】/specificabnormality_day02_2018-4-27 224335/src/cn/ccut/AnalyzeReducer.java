package cn.ccut;

import com.sun.org.apache.xml.internal.security.Init;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.TreeSet;

/**
 * @authorMr.Robot
 * @create2018-04-27 19:51
 */
public class AnalyzeReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    private Enterprise enterprise = new Enterprise();
    private TreeSet<Invoice> inputInvoiceSet = new TreeSet<>();
    private TreeSet<Invoice> outputInvoiceSet = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {

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
            if (!e.isRetain()) {
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
    }
}
