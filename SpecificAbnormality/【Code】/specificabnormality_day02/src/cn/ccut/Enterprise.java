package cn.ccut;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;

/**
 * 企业信息
 *
 * @authorMr.Robot
 * @create2018-04-25 21:16
 */
public class Enterprise extends LongWritable implements Writable{
    // 纳税人id
    private String nsr_id = "Null";
    // 是否保留
    private  boolean retain = false;
    // 是否异常, 1为异常, 0为正常
    private String abnormal = "0";

    // 企业发票信息
    private String fp_nid = "Null";
    private String xf_id = "Null";
    private String gf_id = "Null";
    private double je = 0;
    private double se = 0;
    private double jshj = 0;
    private String kpyf = "Null";
    private Calendar kprq;
    private String zfbz = "Null";

    // 进项发票数量
    private long inputInvoiceNum = 0;
    // 销项发票数量
    private long outputInvoiceNum = 0;

    // 异常信息
    // 不存在进项发票
    String noEntryInvoice = " ";
    // 不存在销项发票
    String noOutputInvoice = " ";
    // 长时间无进项
    String longTimeNoEntry = " ";
    // 长时间无销项
    String longTimeNoOutput = " ";
    // 税负波动大
    String taxChanges = " ";
    // 专票用量波动大
    String invoiceUsageChange = " ";
    // 发票作废率高
    String invoiceInvalidRate = " ";
    // 企业亏损预警
    String lossWarning = " ";

    @Override
    public String toString() {
        return nsr_id + "可能存在的异常有: " +
                "  >. " + noEntryInvoice
                + noOutputInvoice
                + longTimeNoEntry
                + longTimeNoOutput
                + taxChanges
                + invoiceUsageChange
                + invoiceInvalidRate
                + lossWarning;
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }

    public String getNsr_id() {
        return nsr_id;
    }

    public void setNsr_id(String nsr_id) {
        this.nsr_id = nsr_id;
    }

    public boolean isRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    public String getFp_nid() {
        return fp_nid;
    }

    public void setFp_nid(String fp_nid) {
        this.fp_nid = fp_nid;
    }

    public String getXf_id() {
        return xf_id;
    }

    public void setXf_id(String xf_id) {
        this.xf_id = xf_id;
    }

    public String getGf_id() {
        return gf_id;
    }

    public void setGf_id(String gf_id) {
        this.gf_id = gf_id;
    }

    public double getJe() {
        return je;
    }

    public void setJe(double je) {
        this.je = je;
    }

    public double getSe() {
        return se;
    }

    public void setSe(double se) {
        this.se = se;
    }

    public double getJshj() {
        return jshj;
    }

    public void setJshj(double jshj) {
        this.jshj = jshj;
    }

    public String getKpyf() {
        return kpyf;
    }

    public void setKpyf(String kpyf) {
        this.kpyf = kpyf;
    }

    public Calendar getKprq() {
        return kprq;
    }

    public void setKprq(Calendar kprq) {
        this.kprq = kprq;
    }

    public String getZfbz() {
        return zfbz;
    }

    public void setZfbz(String zfbz) {
        this.zfbz = zfbz;
    }

    public long getInputInvoiceNum() {
        return inputInvoiceNum;
    }

    public void setInputInvoiceNum(long inputInvoiceNum) {
        this.inputInvoiceNum = inputInvoiceNum;
    }

    public long getOutputInvoiceNum() {
        return outputInvoiceNum;
    }

    public void setOutputInvoiceNum(long outputInvoiceNum) {
        this.outputInvoiceNum = outputInvoiceNum;
    }

    public String getNoEntryInvoice() {
        return noEntryInvoice;
    }

    public void setNoEntryInvoice(String noEntryInvoice) {
        this.noEntryInvoice = noEntryInvoice;
    }

    public String getNoOutputInvoice() {
        return noOutputInvoice;
    }

    public void setNoOutputInvoice(String noOutputInvoice) {
        this.noOutputInvoice = noOutputInvoice;
    }

    public String getLongTimeNoEntry() {
        return longTimeNoEntry;
    }

    public void setLongTimeNoEntry(String longTimeNoEntry) {
        this.longTimeNoEntry = longTimeNoEntry;
    }

    public String getLongTimeNoOutput() {
        return longTimeNoOutput;
    }

    public void setLongTimeNoOutput(String longTimeNoOutput) {
        this.longTimeNoOutput = longTimeNoOutput;
    }

    public String getTaxChanges() {
        return taxChanges;
    }

    public void setTaxChanges(String taxChanges) {
        this.taxChanges = taxChanges;
    }

    public String getInvoiceUsageChange() {
        return invoiceUsageChange;
    }

    public void setInvoiceUsageChange(String invoiceUsageChange) {
        this.invoiceUsageChange = invoiceUsageChange;
    }

    public String getInvoiceInvalidRate() {
        return invoiceInvalidRate;
    }

    public void setInvoiceInvalidRate(String invoiceInvalidRate) {
        this.invoiceInvalidRate = invoiceInvalidRate;
    }

    public String getLossWarning() {
        return lossWarning;
    }

    public void setLossWarning(String lossWarning) {
        this.lossWarning = lossWarning;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }
}
