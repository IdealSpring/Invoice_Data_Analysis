package cn.ccut;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 企业信息
 *
 * @authorMr.Robot
 * @create2018-04-25 21:16
 */
public class Enterprise extends LongWritable implements Writable{
    private String hydm = "null";
    private String nsr_id = "null";
    private String djzclx_dm = "null";
    private String kydjrq = "null";
    private String xgrq = "null";
    // 是否保留
    private boolean retain = false;
    // 是否异常, 1为异常, 0为正常
    private String abnormal = "0";

    private String hy = "null";
    private String djzclx = "null";

    // 异常个数
    private int abnormalNum;

    // 企业发票信息
    private String fp_nid = "null";
    private String xf_id = "null";
    private String gf_id = "null";
    private double je = 0;
    private double se = 0;
    private double jshj = 0;
    private String kpyf = "null";
    private String kprq = "null";
    private String zfbz = "null";

    // 异常信息
    // 发票用量
    String invoiceUsage = "";
    // 税负波动
    String taxChange = "";
    // 专票用量波动
    String invoiceUsageChange = "";
    // 发票作废率
    String invoiceInvalidRate = "";
    // 企业盈利
    String lossWarning = "";

    @Override
    public String toString() {
        return "纳税人id为:" + nsr_id +
                "的企业存在的异常有" + "<"
                + invoiceUsage
                + taxChange
                + invoiceUsageChange
                + invoiceInvalidRate
                + lossWarning
                + " >\r\n"
                + " 所属行业:"
                + hy
                + " 登记注册类型:"
                + djzclx
                +"\r\n";
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(hydm);
        dataOutput.writeUTF(nsr_id);
        dataOutput.writeUTF(djzclx_dm);
        dataOutput.writeUTF(kydjrq);
        dataOutput.writeUTF(xgrq);
        dataOutput.writeBoolean(retain);
        dataOutput.writeUTF(abnormal);
        dataOutput.writeUTF(hy);
        dataOutput.writeUTF(djzclx);
        dataOutput.writeInt(abnormalNum);

        dataOutput.writeUTF(this.fp_nid);
        dataOutput.writeUTF(this.xf_id);
        dataOutput.writeUTF(this.gf_id);
        dataOutput.writeDouble(this.je);
        dataOutput.writeDouble(this.se);
        dataOutput.writeDouble(this.jshj);
        dataOutput.writeUTF(this.kpyf);
        dataOutput.writeUTF(this.kprq);
        dataOutput.writeUTF(this.zfbz);

        dataOutput.writeUTF(invoiceUsage);
        dataOutput.writeUTF(taxChange);
        dataOutput.writeUTF(invoiceUsageChange);
        dataOutput.writeUTF(invoiceInvalidRate);
        dataOutput.writeUTF(lossWarning);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.hydm = dataInput.readUTF();
        this.nsr_id = dataInput.readUTF();
        this.djzclx_dm = dataInput.readUTF();
        this.kydjrq = dataInput.readUTF();
        this.xgrq = dataInput.readUTF();
        this.retain = dataInput.readBoolean();
        this.abnormal = dataInput.readUTF();
        this.hy = dataInput.readUTF();
        this.djzclx = dataInput.readUTF();
        this.abnormalNum = dataInput.readInt();

        this.fp_nid = dataInput.readUTF();
        this.xf_id = dataInput.readUTF();
        this.gf_id = dataInput.readUTF();
        this.je = dataInput.readDouble();
        this.se = dataInput.readDouble();
        this.jshj = dataInput.readDouble();
        this.kpyf = dataInput.readUTF();
        this.kprq = dataInput.readUTF();
        this.zfbz = dataInput.readUTF();

        this.invoiceUsage = dataInput.readUTF();
        this.taxChange = dataInput.readUTF();
        this.invoiceUsageChange = dataInput.readUTF();
        this.invoiceInvalidRate = dataInput.readUTF();
        this.lossWarning = dataInput.readUTF();
    }

    public int getAbnormalNum() {
        return abnormalNum;
    }

    public void setAbnormalNum(int abnormalNum) {
        this.abnormalNum = abnormalNum;
    }

    public String getHy() {
        return hy;
    }

    public void setHy(String hy) {
        this.hy = hy;
    }

    public String getDjzclx() {
        return djzclx;
    }

    public void setDjzclx(String djzclx) {
        this.djzclx = djzclx;
    }

    public String getHydm() {
        return hydm;
    }

    public void setHydm(String hydm) {
        this.hydm = hydm;
    }

    public String getDjzclx_dm() {
        return djzclx_dm;
    }

    public void setDjzclx_dm(String djzclx_dm) {
        this.djzclx_dm = djzclx_dm;
    }

    public String getKydjrq() {
        return kydjrq;
    }

    public void setKydjrq(String kydjrq) {
        this.kydjrq = kydjrq;
    }

    public String getXgrq() {
        return xgrq;
    }

    public void setXgrq(String xgrq) {
        this.xgrq = xgrq;
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

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getZfbz() {
        return zfbz;
    }

    public void setZfbz(String zfbz) {
        this.zfbz = zfbz;
    }

    public String getInvoiceUsage() {
        return invoiceUsage;
    }

    public void setInvoiceUsage(String invoiceUsage) {
        this.invoiceUsage = invoiceUsage;
    }

    public String getTaxChange() {
        return taxChange;
    }

    public void setTaxChange(String taxChange) {
        this.taxChange = taxChange;
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
