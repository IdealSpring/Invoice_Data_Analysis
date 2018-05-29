package ccut.stage01;

import java.util.Calendar;

/**
 * 发票实体类
 */

public class Invoice implements Comparable<Invoice> {
    //发票概要
    private String fp_nid;
    private String xf_id;
    private String gf_id;
    private double je;
    private double se;
    private double jshj;
    private String kpyf;
    private Calendar kprq;
    private String zfbz;

    public void setParas(String fp_nid, String xf_id, String gf_id,
                         double je, double se, double jshj,
                         String kpyf, Calendar kprq, String zfbz) {
        this.fp_nid = fp_nid;
        this.xf_id = xf_id;
        this.gf_id = gf_id;
        this.je = je;
        this.se = se;
        this.jshj = jshj;
        this.kpyf = kpyf;
        this.kprq = kprq;
        this.zfbz = zfbz;
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

    @Override
    public String toString() {
        int year = kprq.get(Calendar.YEAR);
        int month = kprq.get(Calendar.MONTH) + 1;
        int day = kprq.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        return "fp_nid='" + fp_nid + '\'' +
                ", xf_id='" + xf_id + '\'' +
                ", gf_id='" + gf_id + '\'' +
                ", je=" + je +
                ", se=" + se +
                ", jshj=" + jshj +
                ", kpyf='" + kpyf + '\'' +
                ", kprq=" + date +
                ", zfbz='" + zfbz + '\'';
    }

    @Override
    public int compareTo(Invoice invoice) {
        return this.kprq.before(invoice.getKprq()) ? -1 : 1;
    }
}
