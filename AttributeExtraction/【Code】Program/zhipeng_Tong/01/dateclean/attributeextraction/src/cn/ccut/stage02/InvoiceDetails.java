package cn.ccut.stage02;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 处理hwmx，添加购方和销方id
 */
public class InvoiceDetails implements Writable {
    //企业唯一id
    private String Fp_Id = "Null";
    //类型
    private boolean details = false;

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

    public InvoiceDetails() {
    }

    public InvoiceDetails(String fp_nidMX, String date_keyMX, String hwmcMX, String ggxhMX, String dwMX, double slMX, double djMX, double jeMX, double seMX, String spbmMX) {
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
        return fp_nidMX +
                "," + xf_id + "," + gf_id +
                "," + date_keyMX + "," + hwmcMX +
                "," + ggxhMX + "," + dwMX +
                "," + slMX + "," + djMX +
                "," + jeMX + "," + seMX +
                "," + spbmMX ;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.Fp_Id);
        dataOutput.writeBoolean(this.details);

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

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.Fp_Id = dataInput.readUTF();
        this.details = dataInput.readBoolean();

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
    }

    public String getFp_Id() {
        return Fp_Id;
    }

    public void setFp_Id(String nsr_id) {
        this.Fp_Id = nsr_id;
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

    public boolean isDetails() {
        return details;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }
}
