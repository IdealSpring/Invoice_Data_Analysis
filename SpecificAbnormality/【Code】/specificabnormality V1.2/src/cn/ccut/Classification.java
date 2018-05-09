package cn.ccut;

import cn.demo.FileRead;

import java.io.*;

/**
 * 异常企业的分类
 *
 * @authorMr.Robot
 * @create2018-05-07 17:57
 */
public class Classification {
    public Classification() {
    }

    // 异常类型
    public static final String NO_INPUT_INVOICE = " 无进项发票";
    public static final String NO_OUTPUT_INVOICE = " 无销项发票";
    public static final String NO_INPUT_OUTPUT_INVOICE = " 进销项全无";
    public static final String LONG_TIME_NO_INPUT = " 超过一个月无进项发票";
    public static final String LONG_TIME_NO_OUTPUT = " 超过一个月无销项发票";
    public static final String LONG_TIME_NO_INPUT_OUTPUT = "长时间无发票";
    public static final String INVOICE_NUMBER_ABNORMALITY = "发票数目异常";
    public static final String TAX_CHANGE = " 税负波动大";
    public static final String INVOICE_USAGE_CHANGE = " 发票用量波动大";
    public static final String INVOICE_INVALID_RATE = " 发票作废率高";
    public static final String LOSS_WARNING_CONTINUOUS_QUARTER = " 连续季度零申报";
    public static final String LOSS_WARNING = " 零申报预警";
    public static final String LOSS_SERIOUS = " 严重亏损";
    public static final String HUGE_PROFIT = " 利润偏高";

    // 异常分类文件存储路径
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalClassification\\";

    public static void abnormalClassification(Enterprise enterprise) throws IOException {
        aboutInvoiceUsage(enterprise);
        aboutTaxChange(enterprise);
        aboutInvoiceUsageChange(enterprise);
        aboutInvoiceInvalidRate(enterprise);
        aboutLossWarning(enterprise);
    }

    /**
     * 判断发票用量情况
     *
     * @param enterprise
     * @throws IOException
     */
    private static void aboutInvoiceUsage(Enterprise enterprise) throws IOException {
        FileOutputStream fos;
        // 获取异常类型
        String abnormal = enterprise.getInvoiceUsage();

        if (abnormal.equals(NO_INPUT_INVOICE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "无进项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(NO_OUTPUT_INVOICE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "无销项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(NO_INPUT_OUTPUT_INVOICE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "进销项全无", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LONG_TIME_NO_INPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "超过一个月无进项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LONG_TIME_NO_OUTPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "超过一个月无销项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LONG_TIME_NO_INPUT_OUTPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "长时间无发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(INVOICE_NUMBER_ABNORMALITY)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "发票数目异常", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else {
            return;
        }

        if (fos != null) {
            fos.close();
        }
    }

    /**
     * 税负波动情况
     *
     * @param enterprise
     * @throws IOException
     */
    private static void aboutTaxChange(Enterprise enterprise) throws IOException {
        FileOutputStream fos;
        // 获取异常类型
        String abnornal = enterprise.getTaxChange();

        if (abnornal.equals(TAX_CHANGE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "税负波动大", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else {
            return;
        }

        if (fos != null) {
            fos.close();
        }
    }

    /**
     * 专票用量波动情况
     *
     * @param enterprise
     * @throws IOException
     */
    private static void aboutInvoiceUsageChange(Enterprise enterprise) throws IOException {
        FileOutputStream fos;
        String abnornal = enterprise.getInvoiceUsageChange();

        if (abnornal.equals(INVOICE_USAGE_CHANGE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "发票用量波动大", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else {
            return;
        }

        if (fos != null) {
            fos.close();
        }
    }

    /**
     * 发票作废率情况
     *
     * @param enterprise
     * @throws IOException
     */
    private static void aboutInvoiceInvalidRate(Enterprise enterprise) throws IOException {
        FileOutputStream fos;
        String abnormal = enterprise.getInvoiceInvalidRate();

        if (abnormal.equals(INVOICE_INVALID_RATE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "发票作废率高", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else {
            return;
        }

        if (fos != null) {
            fos.close();
        }
    }

    /**
     * 企业盈利情况
     *
     * @param enterprise
     * @throws IOException
     */
    private static void aboutLossWarning(Enterprise enterprise) throws IOException {
        FileOutputStream fos1;
        FileOutputStream fos2 = null;
        String abnormal = enterprise.getLossWarning();

        if (abnormal.equals(LOSS_WARNING_CONTINUOUS_QUARTER)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "连续季度零申报", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LOSS_WARNING)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "零申报预警", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LOSS_SERIOUS)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(HUGE_PROFIT)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "利润偏高", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LOSS_SERIOUS + LOSS_WARNING_CONTINUOUS_QUARTER)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos2 = new FileOutputStream(CLASSIFICATION_PATH + "连续季度零申报", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            fos2.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else if (abnormal.equals(LOSS_SERIOUS + LOSS_WARNING)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos2 = new FileOutputStream(CLASSIFICATION_PATH + "零申报预警", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            fos2.write((enterprise.getNsr_id() + "\r\n").getBytes());
        } else {
            return;
        }

        if (fos1 != null) {
            fos1.close();
        }
        if (fos2 != null) {
            fos2.close();
        }
    }

    /**
     * 统计异常数量
     */
    public static void statisticalAbnormalQuantity() throws IOException {
        FileOutputStream fos;
        BufferedReader bufferedReader;
        Long l = 0L;

        bufferedReader = new BufferedReader(new FileReader(CLASSIFICATION_PATH + "无进项发票"));
        if (bufferedReader.readLine() != null) {
            l ++;
            System.out.println(l);
        }
        fos = new FileOutputStream(CLASSIFICATION_PATH + "无进项发票", true);
        String s = "该类异常数量共计: " + l.toString();
        fos.write(s.getBytes());

        fos.close();
        bufferedReader.close();
    }
}
