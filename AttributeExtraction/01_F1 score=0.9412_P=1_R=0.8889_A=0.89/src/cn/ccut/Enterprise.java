package cn.ccut;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 企业信息
 */
public class Enterprise implements Writable{
    //企业唯一id
    private String nsr_id = "Null";
    //如果type变为以，label变1
    private int type = 0;

    /**
     * 发票信息
     */
    //发票id
    private String  fp_nid = "Null";
    private String xf_id = "Null";
    private String gf_id = "Null";
    private double je = 0;
    private double se = 0;
    private double jshj = 0;
    private String kpyf = "Null";
    private String kprq = "Null";
    private String zfbz = "Null";

    //进项发票数量
    private long inputInvoiceNum = 0;
    //销项发票数量
    private long outputInvoiceNum = 0;

    /**
     * 提取属性
     */
    //只有进项
    private String inputInvoice = "Null";
    //只有销项
    private String outputInvoice = "Null";
    //最近两次进项开票时间
    private String inputInterval = "Null";
    //最近两次销项开票时间
    private String outputInterval = "Null";
    //税负变动率异常±30% == 0.3
    private String taxChangeRate = "Null";

    //企业分类
    private String label = "0";

    @Override
    public String toString() {
        return "nsr_id=" + nsr_id + ",inputInvoiceNum=" + inputInvoiceNum +
                ",outputInvoiceNum=" + outputInvoiceNum + ",inputInvoice=" + inputInvoice +
                ",outputInvoice=" + outputInvoice + ",inputInterval=" + inputInterval +
                ",outputInterval=" + outputInterval + ",taxChangeRate=" + taxChangeRate +
                ",label=" + label;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.nsr_id);
        dataOutput.writeInt(this.type);

        dataOutput.writeUTF(this.fp_nid);
        dataOutput.writeUTF(this.xf_id);
        dataOutput.writeUTF(this.gf_id);
        dataOutput.writeDouble(this.je);
        dataOutput.writeDouble(this.se);
        dataOutput.writeDouble(this.jshj);
        dataOutput.writeUTF(this.kpyf);
        dataOutput.writeUTF(this.kprq);
        dataOutput.writeUTF(this.zfbz);

        dataOutput.writeLong(this.inputInvoiceNum);
        dataOutput.writeLong(this.outputInvoiceNum);

        dataOutput.writeUTF(this.inputInvoice);
        dataOutput.writeUTF(this.outputInvoice);
        dataOutput.writeUTF(this.inputInterval);
        dataOutput.writeUTF(this.outputInterval);
        dataOutput.writeUTF(this.taxChangeRate);

        dataOutput.writeUTF(this.label);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.nsr_id = dataInput.readUTF();
        this.type = dataInput.readInt();

        this.fp_nid = dataInput.readUTF();
        this.xf_id = dataInput.readUTF();
        this.gf_id = dataInput.readUTF();
        this.je = dataInput.readDouble();
        this.se = dataInput.readDouble();
        this.jshj = dataInput.readDouble();
        this.kpyf = dataInput.readUTF();
        this.kprq = dataInput.readUTF();
        this.zfbz = dataInput.readUTF();

        this.inputInvoiceNum = dataInput.readLong();
        this.inputInvoiceNum = dataInput.readLong();

        this.inputInvoice = dataInput.readUTF();
        this.outputInvoice = dataInput.readUTF();
        this.inputInterval = dataInput.readUTF();
        this.outputInterval = dataInput.readUTF();
        this.taxChangeRate = dataInput.readUTF();

        this.label = dataInput.readUTF();
    }

    public String getNsr_id() {
        return nsr_id;
    }

    public void setNsr_id(String nsr_id) {
        this.nsr_id = nsr_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getInputInvoice() {
        return inputInvoice;
    }

    public void setInputInvoice(String inputInvoice) {
        this.inputInvoice = inputInvoice;
    }

    public String getOutputInvoice() {
        return outputInvoice;
    }

    public void setOutputInvoice(String outputInvoice) {
        this.outputInvoice = outputInvoice;
    }

    public String getInputInterval() {
        return inputInterval;
    }

    public void setInputInterval(String inputInterval) {
        this.inputInterval = inputInterval;
    }

    public String getOutputInterval() {
        return outputInterval;
    }

    public void setOutputInterval(String outputInterval) {
        this.outputInterval = outputInterval;
    }

    public String getTaxChangeRate() {
        return taxChangeRate;
    }

    public void setTaxChangeRate(String taxChangeRate) {
        this.taxChangeRate = taxChangeRate;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
