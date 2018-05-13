package cn.ccut.handle;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 异常统计类
 *
 * @authorMr.Robot
 * @create2018-05-13 16:17
 */
public class StatisticsAbnormal {
    public StatisticsAbnormal() {
    }

    // 所有异常总个数
    public static int totalNum;

    // 异常分类文件存储路径
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalResult\\abnormalClassification\\";

    // 文件名称
    public static final String NO_INPUT_INVOICE = "无进项发票";
    public static final String NO_OUTPUT_INVOICE = "无销项发票";
    public static final String NO_INPUT_OUTPUT_INVOICE = "进销项全无";
    public static final String LONG_TIME_NO_INPUT = "超过一个月无进项发票";
    public static final String LONG_TIME_NO_OUTPUT = "超过一个月无销项发票";
    public static final String LONG_TIME_NO_INPUT_OUTPUT = "长时间无发票";
    public static final String INVOICE_NUMBER_ABNORMALITY = "发票数目异常";
    public static final String TAX_CHANGE = "税负波动大";
    public static final String INVOICE_USAGE_CHANGE = "发票用量波动大";
    public static final String INVOICE_INVALID_RATE = "发票作废率高";
    public static final String LOSS_WARNING_CONTINUOUS_QUARTER = "连续季度零申报";
    public static final String LOSS_WARNING = "零申报预警";
    public static final String LOSS_SERIOUS = "严重亏损";
    public static final String HUGE_PROFIT = "利润偏高";

    /**
     * 统计异常数量
     */
    public static void statisticsAbnormalQuantity() throws IOException {
        // 先统计所有企业异常数量总数
        statisticsAbnormalNum();

        // 分别统计各类异常数及百分比
        statisticsAndOutput(NO_INPUT_INVOICE);
        statisticsAndOutput(NO_OUTPUT_INVOICE);
        statisticsAndOutput(NO_INPUT_OUTPUT_INVOICE);
        statisticsAndOutput(LONG_TIME_NO_INPUT);
        statisticsAndOutput(LONG_TIME_NO_OUTPUT);
        statisticsAndOutput(LONG_TIME_NO_INPUT_OUTPUT);
        statisticsAndOutput(INVOICE_NUMBER_ABNORMALITY);
        statisticsAndOutput(TAX_CHANGE);
        statisticsAndOutput(INVOICE_USAGE_CHANGE);
        statisticsAndOutput(INVOICE_INVALID_RATE);
        statisticsAndOutput(LOSS_WARNING_CONTINUOUS_QUARTER);
        statisticsAndOutput(LOSS_WARNING);
        statisticsAndOutput(LOSS_SERIOUS);
        statisticsAndOutput(HUGE_PROFIT);
    }

    /**
     * 统计数量并输出
     *
     * @param string
     * @throws IOException
     */
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

    /**
     * 所有企业异常数量总数
     */
    private static void statisticsAbnormalNum () {
        totalNum = 0;
        LineNumberReader lnr = null;

        ArrayList<String> abnormalFileList = new ArrayList<String>();
        abnormalFileList.add(NO_INPUT_INVOICE);
        abnormalFileList.add(NO_OUTPUT_INVOICE);
        abnormalFileList.add(NO_INPUT_OUTPUT_INVOICE);
        abnormalFileList.add(LONG_TIME_NO_INPUT);
        abnormalFileList.add(LONG_TIME_NO_OUTPUT);
        abnormalFileList.add(LONG_TIME_NO_INPUT_OUTPUT);
        abnormalFileList.add(INVOICE_NUMBER_ABNORMALITY);
        abnormalFileList.add(TAX_CHANGE);
        abnormalFileList.add(INVOICE_USAGE_CHANGE);
        abnormalFileList.add(INVOICE_INVALID_RATE);
        abnormalFileList.add(LOSS_WARNING_CONTINUOUS_QUARTER);
        abnormalFileList.add(LOSS_WARNING);
        abnormalFileList.add(LOSS_SERIOUS);
        abnormalFileList.add(HUGE_PROFIT);

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
    }
}
