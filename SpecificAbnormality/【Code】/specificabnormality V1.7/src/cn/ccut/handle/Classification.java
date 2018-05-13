package cn.ccut.handle;

import cn.ccut.pojo.Enterprise;

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
    private static final String NO_INPUT_INVOICE = " 无进项发票";
    private static final String NO_OUTPUT_INVOICE = " 无销项发票";
    private static final String NO_INPUT_OUTPUT_INVOICE = " 进销项全无";
    private static final String LONG_TIME_NO_INPUT = " 超过一个月无进项发票";
    private static final String LONG_TIME_NO_OUTPUT = " 超过一个月无销项发票";
    private static final String LONG_TIME_NO_INPUT_OUTPUT = " 长时间无发票";
    private static final String INVOICE_NUMBER_ABNORMALITY = " 发票数目异常";
    private static final String TAX_CHANGE = " 税负波动大";
    private static final String INVOICE_USAGE_CHANGE = " 发票用量波动大";
    private static final String INVOICE_INVALID_RATE = " 发票作废率高";
    private static final String LOSS_WARNING_CONTINUOUS_QUARTER = " 连续季度零申报";
    private static final String LOSS_WARNING = " 零申报预警";
    private static final String LOSS_SERIOUS = " 严重亏损";
    private static final String HUGE_PROFIT = " 利润偏高";

    // 异常分类文件存储路径
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalResult\\abnormalClassification\\";

    // 异常个数
    private static int num;

    // 所有异常总个数
    private static int totalNum;

    public static int abnormalClassification(Enterprise enterprise) throws IOException {
        num = 0;

        aboutInvoiceUsage(enterprise);
        aboutTaxChange(enterprise);
        aboutInvoiceUsageChange(enterprise);
        aboutInvoiceInvalidRate(enterprise);
        aboutLossWarning(enterprise);

        return num;
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
            num ++;
        } else if (abnormal.equals(NO_OUTPUT_INVOICE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "无销项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(NO_INPUT_OUTPUT_INVOICE)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "进销项全无", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(LONG_TIME_NO_INPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "超过一个月无进项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(LONG_TIME_NO_OUTPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "超过一个月无销项发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(LONG_TIME_NO_INPUT_OUTPUT)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "长时间无发票", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(INVOICE_NUMBER_ABNORMALITY)) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "发票数目异常", true);
            fos.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
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
            num ++;
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
            num ++;
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
            num ++;
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
            num ++;
        } else if (abnormal.equals(LOSS_WARNING)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "零申报预警", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(LOSS_SERIOUS)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(HUGE_PROFIT)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "利润偏高", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num ++;
        } else if (abnormal.equals(LOSS_SERIOUS + LOSS_WARNING_CONTINUOUS_QUARTER)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos2 = new FileOutputStream(CLASSIFICATION_PATH + "连续季度零申报", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            fos2.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num = num + 2;
        } else if (abnormal.equals(LOSS_SERIOUS + LOSS_WARNING)) {
            fos1 = new FileOutputStream(CLASSIFICATION_PATH + "严重亏损", true);
            fos2 = new FileOutputStream(CLASSIFICATION_PATH + "零申报预警", true);
            fos1.write((enterprise.getNsr_id() + "\r\n").getBytes());
            fos2.write((enterprise.getNsr_id() + "\r\n").getBytes());
            num = num + 2;
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
     *//*
    public static void statisticalAbnormalQuantity() throws IOException {
        // 先统计所有企业异常数量总数
        statisticsAbnormalNum();

        // 分别统计各类异常数及百分比
        statisticsAndOutput("无进项发票");
        statisticsAndOutput("无销项发票");
        statisticsAndOutput("进销项全无");
        statisticsAndOutput("超过一个月无进项发票");
        statisticsAndOutput("超过一个月无销项发票");
        statisticsAndOutput("长时间无发票");
        statisticsAndOutput("发票数目异常");
        statisticsAndOutput("税负波动大");
        statisticsAndOutput("发票用量波动大");
        statisticsAndOutput("发票作废率高");
        statisticsAndOutput("连续季度零申报");
        statisticsAndOutput("零申报预警");
        statisticsAndOutput("严重亏损");
        statisticsAndOutput("利润偏高");
    }

    *//**
     * 统计数量并输出
     *
     * @param string
     * @throws IOException
     *//*
    private static void statisticsAndOutput(String string)  throws IOException {
        FileOutputStream fos = null;
        LineNumberReader  lnr = null;

        NumberFormat num = NumberFormat.getPercentInstance();
        num.setMaximumIntegerDigits(3);
        num.setMaximumFractionDigits(2);
        try {
            // 统计行数
            lnr = new LineNumberReader(new FileReader(new File(CLASSIFICATION_PATH + string)));
            lnr.skip(Long.MAX_VALUE);

            if (lnr != null) {
                // 输出统计
                fos = new FileOutputStream(CLASSIFICATION_PATH + string, true);
                int i = lnr.getLineNumber();
                String s = "该类异常数量共计: " + i;

                String s1 = "\r\n" + "占总异常数的: " + num.format((double) i / totalNum);
                fos.write(s.getBytes());
                fos.write(s1.getBytes());

                fos.close();
                lnr.close();
            }
        } catch (IOException e) {
            System.out.println("不存在" + string + "的异常类型");
        }
    }

    *//**
     * 所有企业异常数量总数
     *//*
    private static void statisticsAbnormalNum () {
        totalNum = 0;
        LineNumberReader lnr = null;

        ArrayList<String> abnormalFileList = new ArrayList<String>();
        abnormalFileList.add("无进项发票");
        abnormalFileList.add("无销项发票");
        abnormalFileList.add("进销项全无");
        abnormalFileList.add("超过一个月无进项发票");
        abnormalFileList.add("超过一个月无销项发票");
        abnormalFileList.add("长时间无发票");
        abnormalFileList.add("发票数目异常");
        abnormalFileList.add("税负波动大");
        abnormalFileList.add("税负波动大");
        abnormalFileList.add("发票作废率高");
        abnormalFileList.add("连续季度零申报");
        abnormalFileList.add("零申报预警");
        abnormalFileList.add("严重亏损");
        abnormalFileList.add("利润偏高");

        for (String abnormalFile : abnormalFileList) {
            try {
                // 统计行数
                lnr = new LineNumberReader(new FileReader(new File(CLASSIFICATION_PATH + abnormalFile)));
                lnr.skip(Long.MAX_VALUE);

                totalNum += lnr.getLineNumber();

                lnr.close();
            } catch (IOException e) {
                System.out.println(abnormalFile + "的异常类型个数为0");
            }
        }
    }*/
}
