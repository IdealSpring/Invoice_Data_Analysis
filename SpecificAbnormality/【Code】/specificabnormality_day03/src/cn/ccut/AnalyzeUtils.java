package cn.ccut;

import java.util.TreeSet;

/**
 * 分析具体异常类
 *
 * @authorMr.Robot
 * @create2018-04-28 20:00
 */
public class AnalyzeUtils {
    public AnalyzeUtils() {
    }

    public static final String NO_ENTER_INVOICE = " 无进项发票";
    public static final String NO_OUTPUT_INVOICE = " 无销项发票";
    public static final String LONG_TIME_NO_ENTRY = " 长时间无进项发票";
    public static final String LONG_TIME_NO_OUTPUT = " 长时间无销项发票";
    public static final String TAX_CHANGE = " 税负波动大";
    public static final String INVOICE_USAGE_CHANGE = " 发票用量波动大";
    public static final String INVOICE_INVALID_RATE = " 发票作废率高";
    public static final String LOSS_WARNING = " 企业常年亏损";

    public static void test(Enterprise enterprise,
                            TreeSet<Invoice> inputInvoiceSet,
                            TreeSet<Invoice> OutputInvoiceSet) {
        enterprise.setNoEntryInvoice(NO_ENTER_INVOICE);
        enterprise.setLongTimeNoEntry(LONG_TIME_NO_ENTRY);
    }
}
