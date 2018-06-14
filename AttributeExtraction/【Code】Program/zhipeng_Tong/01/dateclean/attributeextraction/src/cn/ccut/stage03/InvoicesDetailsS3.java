package cn.ccut.stage03;


import java.util.Calendar;

public class InvoicesDetailsS3  implements Comparable<InvoicesDetailsS3> {
    private String fp_nidMX;
    private Calendar date_keyMX;
    private String hwmcMX;
    private String ggxhMX;
    private String dwMX;
    private double slMX;
    private double djMX;
    private double jeMX;
    private double seMX;
    private String spbmMX;

    @Override
    public int compareTo(InvoicesDetailsS3 invoicesDetailsS3) {
        return this.date_keyMX.before(invoicesDetailsS3.getDate_keyMX()) ? 1 : -1;
    }

    public String getFp_nidMX() {
        return fp_nidMX;
    }

    public void setFp_nidMX(String fp_nidMX) {
        this.fp_nidMX = fp_nidMX;
    }

    public Calendar getDate_keyMX() {
        return date_keyMX;
    }

    public void setDate_keyMX(Calendar date_keyMX) {
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
}
