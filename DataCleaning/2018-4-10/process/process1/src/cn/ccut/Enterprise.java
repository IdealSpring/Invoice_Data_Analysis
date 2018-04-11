package cn.ccut;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 增值税发票
 */
public class Enterprise implements Writable{
    /**
     * 企业信息
     */
    private String nsr_id;
    private String label = "0";
    private long inputInvoiceNum;
    private long outputInvoiceNum;
    //只有进项
    private String onlyInputInvoice = "No";
    //只有销项
    private String onlyOutputInvoice = "No";
    //连续1个月无申报
    private String inputNoDeclareOverSix = "No";
    //连续1个月无申报
    private String outputNoDeclareOverSix = "No";
    //税负变动率异常±30% == 0.3
    private String taxChangeRate = "";

    /**
     * 发票信息
     */
    //重要信息
    private String  fp_nid = "Null";
    private String xf_id = "Null";
    private String gf_id = "Null";
    //发票所用信息
    private String inovice = "Null";

    public void init(String nsr_id, long inputInvoiceNum, long outputInvoiceNum) {
        this.nsr_id = nsr_id;
        this.inputInvoiceNum = inputInvoiceNum;
        this.outputInvoiceNum = outputInvoiceNum;

        if(inputInvoiceNum == 0L) {
            this.onlyInputInvoice = "Yes";
        } else {
            this.onlyInputInvoice = "No";
        }

        if(outputInvoiceNum == 0L) {
            this.onlyOutputInvoice = "Yes";
        } else {
            this.onlyOutputInvoice = "No";
        }
    }

    @Override
    public String toString() {
        return nsr_id +
                "," + onlyInputInvoice +
                "," + onlyOutputInvoice +
                "," + inputNoDeclareOverSix +
                "," + outputNoDeclareOverSix +
                "," + taxChangeRate;
        /*return "nsr_id=" + nsr_id +
                ",inputInvoiceNum=" + inputInvoiceNum +
                ",outputInvoiceNum=" + outputInvoiceNum +
                ",onlyInputInvoice=" + onlyInputInvoice +
                ",onlyOutputInvoice=" + onlyOutputInvoice +
                ",inputNoDeclareOverSix=" + inputNoDeclareOverSix +
                ",outputNoDeclareOverSix=" + outputNoDeclareOverSix +
                ",taxChangeRate=" + taxChangeRate +
                ",label=" + label;*/
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.nsr_id);
        dataOutput.writeUTF(this.label);
        dataOutput.writeLong(this.inputInvoiceNum);
        dataOutput.writeLong(this.outputInvoiceNum);
        dataOutput.writeUTF(this.onlyInputInvoice);
        dataOutput.writeUTF(this.onlyOutputInvoice);
        dataOutput.writeUTF(this.inputNoDeclareOverSix);
        dataOutput.writeUTF(this.outputNoDeclareOverSix);
        dataOutput.writeUTF(this.taxChangeRate);

        dataOutput.writeUTF(this.fp_nid);
        dataOutput.writeUTF(this.xf_id);
        dataOutput.writeUTF(this.gf_id);
        dataOutput.writeUTF(this.inovice);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.nsr_id = dataInput.readUTF();
        this.label = dataInput.readUTF();
        this.inputInvoiceNum = dataInput.readLong();
        this.outputInvoiceNum = dataInput.readLong();
        this.onlyInputInvoice = dataInput.readUTF();
        this.onlyOutputInvoice = dataInput.readUTF();
        this.inputNoDeclareOverSix = dataInput.readUTF();
        this.outputNoDeclareOverSix = dataInput.readUTF();
        this.taxChangeRate = dataInput.readUTF();

        this.fp_nid = dataInput.readUTF();
        this.xf_id = dataInput.readUTF();
        this.gf_id = dataInput.readUTF();
        this.inovice = dataInput.readUTF();
    }

    public String getNsr_id() {
        return nsr_id;
    }

    public void setNsr_id(String nsr_id) {
        this.nsr_id = nsr_id;
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

    public String getOnlyInputInvoice() {
        return onlyInputInvoice;
    }

    public void setOnlyInputInvoice(String onlyInputInvoice) {
        this.onlyInputInvoice = onlyInputInvoice;
    }

    public String getOnlyOutputInvoice() {
        return onlyOutputInvoice;
    }

    public void setOnlyOutputInvoice(String onlyOutputInvoice) {
        this.onlyOutputInvoice = onlyOutputInvoice;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInovice() {
        return inovice;
    }

    public void setInovice(String inovice) {
        this.inovice = inovice;
    }

    public String getInputNoDeclareOverSix() {
        return inputNoDeclareOverSix;
    }

    public void setInputNoDeclareOverSix(String inputNoDeclareOverSix) {
        this.inputNoDeclareOverSix = inputNoDeclareOverSix;
    }

    public String getOutputNoDeclareOverSix() {
        return outputNoDeclareOverSix;
    }

    public void setOutputNoDeclareOverSix(String outputNoDeclareOverSix) {
        this.outputNoDeclareOverSix = outputNoDeclareOverSix;
    }

    public String getTaxChangeRate() {
        return taxChangeRate;
    }

    public void setTaxChangeRate(String taxChangeRate) {
        this.taxChangeRate = taxChangeRate;
    }

}
