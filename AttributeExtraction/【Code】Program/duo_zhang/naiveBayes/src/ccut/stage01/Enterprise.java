package ccut.stage01;

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
    //是否保留
    private boolean retain = false;

    /**
     * 发票信息
     */
    //发票id
    private String fp_nid = "Null";
    private String xf_id = "Null";
    private String gf_id = "Null";
    private double je = 0;
    private double se = 0;
    private double jshj = 0;
    private String kpyf = "Null";
    private String kprq = "Null";
    private String zfbz = "Null";

    /**
     * 发票明细
     */
    private String fp_nidMX = "Null";
    private String date_keyMX = "Null";
    private String hwmcMX = "Null";
    private String ggxhMX = "Null";
    private String dwMX = "Null";
    private double slMX = 0.0;
    private double djMX = 0.0;
    private double jeMX = 0.0;
    private double seMX = 0.0;
    private String spbmMX = "Null";

    //进项发票数量
    private long inputInvoiceNum = 0;
    //销项发票数量
    private long outputInvoiceNum = 0;

    /**
     * 提取属性
     */
    //3.只有进项
    private String inputInvoice = "Null";
    //4.只有销项
    private String outputInvoice = "Null";
    //5.最近两次进项开票时间
    private String inputInterval = "Null";
    //6.最近两次销项开票时间
    private String outputInterval = "Null";
    //7.税负变动率异常±30% == 0.3
    private String taxChangeRate = "Null";
    //8.发票用量变动
    private String invoiceUsageChange = "Null";
    //9.进项税额变动率高于销项税额变动率
    private String inputTaxAndOutputTaxRatio = "Null";
    //10.发票作废率
    private String invoiceInvalidRatio = "Null";
    //11.发票显示连续亏损
    private String continuousLoss = "Null";
    //12.进销项差额巨大
    private String invoiceBalance = "Null";
    //13.进项发票作废
    private String inputInvoiceInvalid = "Null";
    //14.销项发票作废
    private String outputInvoiceInvalid = "Null";
    //15.连续亏损仍然加大进货
    private String lossAddStock = "Null";

    //企业分类
    private String label = "0";

    @Override
    public String toString() {
        return nsr_id +
                "," + inputInvoice + "," + outputInvoice +
                "," + inputInterval + "," + outputInterval +
                "," + taxChangeRate + "," + invoiceUsageChange +
                "," + inputTaxAndOutputTaxRatio + "," + invoiceInvalidRatio +
                "," + continuousLoss + "," + invoiceBalance +
                "," + inputInvoiceInvalid + "," + outputInvoiceInvalid +
                "," + lossAddStock +
                "," + label;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.nsr_id);
        dataOutput.writeBoolean(this.retain);

        dataOutput.writeUTF(this.fp_nid);
        dataOutput.writeUTF(this.xf_id);
        dataOutput.writeUTF(this.gf_id);
        dataOutput.writeDouble(this.je);
        dataOutput.writeDouble(this.se);
        dataOutput.writeDouble(this.jshj);
        dataOutput.writeUTF(this.kpyf);
        dataOutput.writeUTF(this.kprq);
        dataOutput.writeUTF(this.zfbz);

        //发票明细
        dataOutput.writeUTF(this.fp_nidMX);
        dataOutput.writeUTF(this.date_keyMX);
        dataOutput.writeUTF(this.hwmcMX);
        dataOutput.writeUTF(this.ggxhMX);
        dataOutput.writeUTF(this.dwMX);
        dataOutput.writeDouble(this.slMX);
        dataOutput.writeDouble(this.djMX);
        dataOutput.writeDouble(this.jeMX);
        dataOutput.writeDouble(this.seMX);
        dataOutput.writeUTF(this.spbmMX);

        dataOutput.writeLong(this.inputInvoiceNum);
        dataOutput.writeLong(this.outputInvoiceNum);

        //属性列表
        dataOutput.writeUTF(this.inputInvoice);
        dataOutput.writeUTF(this.outputInvoice);
        dataOutput.writeUTF(this.inputInterval);
        dataOutput.writeUTF(this.outputInterval);
        dataOutput.writeUTF(this.taxChangeRate);
        dataOutput.writeUTF(this.invoiceUsageChange);
        dataOutput.writeUTF(this.inputTaxAndOutputTaxRatio);
        dataOutput.writeUTF(this.invoiceInvalidRatio);
        dataOutput.writeUTF(this.continuousLoss);
        dataOutput.writeUTF(this.invoiceBalance);
        dataOutput.writeUTF(this.inputInvoiceInvalid);
        dataOutput.writeUTF(this.outputInvoiceInvalid);
        dataOutput.writeUTF(this.lossAddStock);

        dataOutput.writeUTF(this.label);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.nsr_id = dataInput.readUTF();
        this.retain = dataInput.readBoolean();

        this.fp_nid = dataInput.readUTF();
        this.xf_id = dataInput.readUTF();
        this.gf_id = dataInput.readUTF();
        this.je = dataInput.readDouble();
        this.se = dataInput.readDouble();
        this.jshj = dataInput.readDouble();
        this.kpyf = dataInput.readUTF();
        this.kprq = dataInput.readUTF();
        this.zfbz = dataInput.readUTF();

        //发票明细
        this.fp_nidMX = dataInput.readUTF();
        this.date_keyMX = dataInput.readUTF();
        this.hwmcMX = dataInput.readUTF();
        this.ggxhMX = dataInput.readUTF();
        this.dwMX = dataInput.readUTF();
        this.slMX = dataInput.readDouble();
        this.djMX = dataInput.readDouble();
        this.jeMX = dataInput.readDouble();
        this.seMX = dataInput.readDouble();
        this.spbmMX = dataInput.readUTF();

        this.inputInvoiceNum = dataInput.readLong();
        this.inputInvoiceNum = dataInput.readLong();

        //属性列表
        this.inputInvoice = dataInput.readUTF();
        this.outputInvoice = dataInput.readUTF();
        this.inputInterval = dataInput.readUTF();
        this.outputInterval = dataInput.readUTF();
        this.taxChangeRate = dataInput.readUTF();
        this.invoiceUsageChange = dataInput.readUTF();
        this.inputTaxAndOutputTaxRatio = dataInput.readUTF();
        this.invoiceInvalidRatio = dataInput.readUTF();
        this.continuousLoss = dataInput.readUTF();
        this.invoiceBalance = dataInput.readUTF();
        this.inputInvoiceInvalid = dataInput.readUTF();
        this.outputInvoiceInvalid = dataInput.readUTF();
        this.lossAddStock = dataInput.readUTF();

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

    public boolean isRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    public String getInvoiceUsageChange() {
        return invoiceUsageChange;
    }

    public void setInvoiceUsageChange(String invoiceUsageChange) {
        this.invoiceUsageChange = invoiceUsageChange;
    }

    public String getInputTaxAndOutputTaxRatio() {
        return inputTaxAndOutputTaxRatio;
    }

    public void setInputTaxAndOutputTaxRatio(String inputTaxAndOutputTaxRatio) {
        this.inputTaxAndOutputTaxRatio = inputTaxAndOutputTaxRatio;
    }

    public String getInvoiceInvalidRatio() {
        return invoiceInvalidRatio;
    }

    public void setInvoiceInvalidRatio(String invoiceInvalidRatio) {
        this.invoiceInvalidRatio = invoiceInvalidRatio;
    }

    public String getContinuousLoss() {
        return continuousLoss;
    }

    public void setContinuousLoss(String continuousLoss) {
        this.continuousLoss = continuousLoss;
    }

    public String getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(String invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    public String getInputInvoiceInvalid() {
        return inputInvoiceInvalid;
    }

    public void setInputInvoiceInvalid(String inputInvoiceInvalid) {
        this.inputInvoiceInvalid = inputInvoiceInvalid;
    }

    public String getOutputInvoiceInvalid() {
        return outputInvoiceInvalid;
    }

    public void setOutputInvoiceInvalid(String outputInvoiceInvalid) {
        this.outputInvoiceInvalid = outputInvoiceInvalid;
    }

    public String getFp_nidMX() {
        return fp_nidMX;
    }

    public void setFp_nidMX(String fp_nidMX) {
        this.fp_nidMX = fp_nidMX;
    }

    public String getDate_keyMX() {
        return date_keyMX;
    }

    public void setDate_keyMX(String date_keyMX) {
        this.date_keyMX = date_keyMX;
    }

    public String getHwmcMX() {
        return hwmcMX;
    }

    public void setHwmcMX(String hwmcMX) {
        this.hwmcMX = hwmcMX;
    }

    public String getGgxhMX() {
        return ggxhMX;
    }

    public void setGgxhMX(String ggxhMX) {
        this.ggxhMX = ggxhMX;
    }

    public String getDwMX() {
        return dwMX;
    }

    public void setDwMX(String dwMX) {
        this.dwMX = dwMX;
    }

    public double getSlMX() {
        return slMX;
    }

    public void setSlMX(double slMX) {
        this.slMX = slMX;
    }

    public double getDjMX() {
        return djMX;
    }

    public void setDjMX(double djMX) {
        this.djMX = djMX;
    }

    public double getJeMX() {
        return jeMX;
    }

    public void setJeMX(double jeMX) {
        this.jeMX = jeMX;
    }

    public double getSeMX() {
        return seMX;
    }

    public void setSeMX(double seMX) {
        this.seMX = seMX;
    }

    public String getSpbmMX() {
        return spbmMX;
    }

    public void setSpbmMX(String spbmMX) {
        this.spbmMX = spbmMX;
    }

    public String getLossAddStock() {
        return lossAddStock;
    }

    public void setLossAddStock(String lossAddStock) {
        this.lossAddStock = lossAddStock;
    }
}
