package cn.ccut.stage03;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DetailsExtraction implements Writable {
    //企业唯一id
    private String nsrId = "Null";
    //该企业商品编码范围
    private String hydmLinkSpbm = "Null";

    /**
     * 发票明细
     */
    private String fp_nidMX = "Null";
    private String xf_id = "Null";
    private String gf_id = "Null";
    private String date_keyMX = "Null";
    private String hwmcMX = "Null";
    private String ggxhMX = "Null";
    private String dwMX = "Null";
    private double slMX = 0.0;
    private double djMX = 0.0;
    private double jeMX = 0.0;
    private double seMX = 0.0;
    private String spbmMX = "Null";

    /**
     * 属性列表
     */
    //16.进销项偏离指数
    private String inputAndOutputDeviation = "Null";
    //17.进项经营范围
    private String inputInvoiceBusinessScope = "Null";
    //18.销项经营范围
    private String outputInvoiceBusinessScope = "Null";

    public DetailsExtraction() {}

    public DetailsExtraction(String fp_nidMX, String date_keyMX, String hwmcMX, String ggxhMX, String dwMX, double slMX, double djMX, double jeMX, double seMX, String spbmMX) {
        this.fp_nidMX = fp_nidMX;
        this.date_keyMX = date_keyMX;
        this.hwmcMX = hwmcMX;
        this.ggxhMX = ggxhMX;
        this.dwMX = dwMX;
        this.slMX = slMX;
        this.djMX = djMX;
        this.jeMX = jeMX;
        this.seMX = seMX;
        this.spbmMX = spbmMX;
    }

    @Override
    public String toString() {
        return nsrId +
                "," + inputAndOutputDeviation + "," + inputInvoiceBusinessScope +
                "," + outputInvoiceBusinessScope;
    }

    /*@Override
    public String toString() {
        return "DetailsExtraction{" +
                "nsrId='" + nsrId + '\'' +
                ", hydmLinkSpbm='" + hydmLinkSpbm + '\'' +
                ", fp_nidMX='" + fp_nidMX + '\'' +
                ", xf_id='" + xf_id + '\'' +
                ", gf_id='" + gf_id + '\'' +
                ", date_keyMX='" + date_keyMX + '\'' +
                ", hwmcMX='" + hwmcMX + '\'' +
                ", ggxhMX='" + ggxhMX + '\'' +
                ", dwMX='" + dwMX + '\'' +
                ", slMX=" + slMX +
                ", djMX=" + djMX +
                ", jeMX=" + jeMX +
                ", seMX=" + seMX +
                ", spbmMX='" + spbmMX + '\'' +
                ", inputAndOutputDeviation='" + inputAndOutputDeviation + '\'' +
                ", inputInvoiceBusinessScope='" + inputInvoiceBusinessScope + '\'' +
                ", outputInvoiceBusinessScope='" + outputInvoiceBusinessScope + '\'' +
                '}';
    }*/

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.nsrId);
        dataOutput.writeUTF(this.hydmLinkSpbm);

        //发票明细
        dataOutput.writeUTF(this.fp_nidMX);
        dataOutput.writeUTF(this.xf_id);
        dataOutput.writeUTF(this.gf_id);
        dataOutput.writeUTF(this.date_keyMX);
        dataOutput.writeUTF(this.hwmcMX);
        dataOutput.writeUTF(this.ggxhMX);
        dataOutput.writeUTF(this.dwMX);
        dataOutput.writeDouble(this.slMX);
        dataOutput.writeDouble(this.djMX);
        dataOutput.writeDouble(this.jeMX);
        dataOutput.writeDouble(this.seMX);
        dataOutput.writeUTF(this.spbmMX);
        dataOutput.writeUTF(this.inputAndOutputDeviation);
        dataOutput.writeUTF(this.inputInvoiceBusinessScope);
        dataOutput.writeUTF(this.outputInvoiceBusinessScope);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.nsrId = dataInput.readUTF();
        this.hydmLinkSpbm = dataInput.readUTF();

        //发票明细
        this.fp_nidMX = dataInput.readUTF();
        this.xf_id = dataInput.readUTF();
        this.gf_id = dataInput.readUTF();
        this.date_keyMX = dataInput.readUTF();
        this.hwmcMX = dataInput.readUTF();
        this.ggxhMX = dataInput.readUTF();
        this.dwMX = dataInput.readUTF();
        this.slMX = dataInput.readDouble();
        this.djMX = dataInput.readDouble();
        this.jeMX = dataInput.readDouble();
        this.seMX = dataInput.readDouble();
        this.spbmMX = dataInput.readUTF();
        this.inputAndOutputDeviation = dataInput.readUTF();
        this.inputInvoiceBusinessScope = dataInput.readUTF();
        this.outputInvoiceBusinessScope = dataInput.readUTF();
    }

    public String getNsrId() {
        return nsrId;
    }

    public void setNsrId(String nsrId) {
        this.nsrId = nsrId;
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

    public String getInputAndOutputDeviation() {
        return inputAndOutputDeviation;
    }

    public void setInputAndOutputDeviation(String inputAndOutputDeviation) {
        this.inputAndOutputDeviation = inputAndOutputDeviation;
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

    public String getHydmLinkSpbm() {
        return hydmLinkSpbm;
    }

    public void setHydmLinkSpbm(String hydmLinkSpbm) {
        this.hydmLinkSpbm = hydmLinkSpbm;
    }

    public String getInputInvoiceBusinessScope() {
        return inputInvoiceBusinessScope;
    }

    public void setInputInvoiceBusinessScope(String inputInvoiceBusinessScope) {
        this.inputInvoiceBusinessScope = inputInvoiceBusinessScope;
    }

    public String getOutputInvoiceBusinessScope() {
        return outputInvoiceBusinessScope;
    }

    public void setOutputInvoiceBusinessScope(String outputInvoiceBusinessScope) {
        this.outputInvoiceBusinessScope = outputInvoiceBusinessScope;
    }
}
