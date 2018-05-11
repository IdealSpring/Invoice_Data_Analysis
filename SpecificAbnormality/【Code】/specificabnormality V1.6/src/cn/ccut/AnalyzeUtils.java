package cn.ccut;

import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.*;

/**
 * 分析具体异常类
 *
 * @authorMr.Robot
 * @create2018-04-28 20:00
 */
public class AnalyzeUtils {
    public AnalyzeUtils() {
    }

    public static final String NO_INPUT_INVOICE = " 无进项发票";
    public static final String NO_OUTPUT_INVOICE = " 无销项发票";
    public static final String NO_INPUT_OUTPUT_INVOICE = " 进销项全无";
    public static final String LONG_TIME_NO_INPUT = " 超过一个月无进项发票";
    public static final String LONG_TIME_NO_OUTPUT = " 超过一个月无销项发票";
    public static final String LONG_TIME_NO_INPUT_OUTPUT = " 长时间无发票";
    public static final String INVOICE_NUMBER_ABNORMALITY = " 发票数目异常";

    public static final String TAX_CHANGE = " 税负波动大";

    public static final String INVOICE_USAGE_CHANGE = " 发票用量波动大";

    public static final String INVOICE_INVALID_RATE = " 发票作废率高";

    public static final String LOSS_WARNING_CONTINUOUS_QUARTER = " 连续季度零申报";
    public static final String LOSS_WARNING = " 零申报预警";
    public static final String LOSS_SERIOUS = " 严重亏损";
    public static final String HUGE_PROFIT = " 利润偏高";

    /**
     * 分析原因
     *
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void analyzeAbnormality(Enterprise enterprise,
                                          TreeSet<Invoice> inputInvoiceSet,
                                          TreeSet<Invoice> outputInvoiceSet) {
        enterprise.setInvoiceUsage(analyzeInvoiceUsage(inputInvoiceSet, outputInvoiceSet));
        enterprise.setTaxChange(analyzeTaxChange(inputInvoiceSet, outputInvoiceSet));
        enterprise.setInvoiceUsageChange(analyzeInvoiceUsageChange(inputInvoiceSet, outputInvoiceSet));
        enterprise.setInvoiceInvalidRate(analyzeInvoiceInvalidRate(inputInvoiceSet, outputInvoiceSet));
        enterprise.setLossWarning(analyzeEnterpriseLoss(inputInvoiceSet, outputInvoiceSet));
    }

    /**
     * 查询企业的行业及登记注册类型
     *
     * @param enterprise
     */
    public static void industryAndType(Enterprise enterprise) {
        String industry = enterprise.getHydm();
        String type = enterprise.getDjzclx_dm();

        // 声明Connection对象
        Connection con;
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名invoice
        String url = "jdbc:mysql://localhost:3306/invoice";
        // Mysql配置的用户名
        String user = "root";
        // Mysql配置的密码
        String password = "root";
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连接数据库
            con = DriverManager.getConnection(url, user, password);
            // 创建statement类对象, 用来执行SQL语句
            Statement statement = con.createStatement();
            if (industry != null) {
                industry = industry.substring(0, 2);
                String sql =  "SELECT industry FROM enterprise_industrycode WHERE industrycode=" + industry;

                // ResultSet类, 用来存放结果集
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    enterprise.setHy(rs.getString(1));
                }
                rs.close();
            }

            if (type != null) {
                String sql =  "SELECT registrationtype FROM enterprise_typecode WHERE typecode=" + type;

                // ResultSet类, 用来存放结果集
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    enterprise.setDjzclx(rs.getString(1));
                }
                rs.close();
            }

            statement.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
            } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
            }catch (Exception e) {
            e.printStackTrace();
        }finally{
            //System.out.println("数据库数据成功获取！！");
        }
    }

    /**
     * 分析企业亏损情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeEnterpriseLoss(TreeSet<Invoice> inputInvoiceSet,
                                                TreeSet<Invoice> outputInvoiceSet) {
        // 发票进项金额
        double inputAmount = 0;
        double outputAmount = 0;

        // 将TreeSet的数据放入List中
        List<Invoice> inputList = new ArrayList<Invoice>();
        List<Invoice> outputList = new ArrayList<Invoice>();

        // 将发票按日期从远到近放入list中
        for (Invoice invoice : inputInvoiceSet) {
            inputList.add(0, invoice);
        }
        for (Invoice invoice : outputInvoiceSet) {
            outputList.add(0, invoice);
        }

        // 进销项发票的数目
        long inum = inputInvoiceSet.size();
        long onum = outputInvoiceSet.size();

        // 若进销项有一项不存在
        if ( inum <= 0 || onum <= 0 || (inum <= 0 && onum <= 0)) {
            return "";
        }

        // 若进销项都存在
        if (inum > 0 && onum > 0) {
            // 获取最近一次的进项发票日期
            Calendar inkprq1 = inputList.get(0).getKprq();
            // 获取最早一次的进项发票日期
            Calendar inkprq2 = inputList.get(inputList.size() - 1).getKprq();
            // 获取最近一次的进项发票日期
            Calendar outkprq1 = outputList.get(0).getKprq();
            // 获取最早一次的进项发票日期
            Calendar outkprq2 = outputList.get(outputList.size() - 1).getKprq();
            // 最近一次的发票日期
            Calendar kprq1;
            // 最早一次的发票日期
            Calendar kprq2;

            if (inkprq1.compareTo(outkprq1) > 0) {
                kprq1 = inkprq1;
            } else {
                kprq1 = outkprq2;
            }

            if (inkprq2.compareTo(outkprq2) < 0) {
                kprq2 = inkprq2;
            } else {
                kprq2 = outkprq2;
            }

            // 计算发票时间跨度,单位为天
            long time1 = (kprq1.getTimeInMillis() - kprq2.getTimeInMillis()) / 1000 / 60 / 60 / 24;

            // 若发票时间跨度大于或等于一个季度
            if (time1 >= 90) {
                // 记录最近一个季度最早的发票在inputList中的位置
                int i = 0;
                inkprq1.add(Calendar.MONTH, -3);
                Calendar inkprq3 = inkprq1;
                for (Invoice invoice : inputList) {
                    if (invoice.getKprq().compareTo(inkprq3) < 0) {
                        i = inputList.indexOf(invoice);
                    }
                }
                // 计算近一个季度的进项金额之和
                for (Invoice invoice : inputList) {
                    int k = 0;
                    if (k < i) {
                        inputAmount += invoice.getJe();
                        k++;
                    } else {
                        break;
                    }
                }

                // 记录最近一个季度最早的发票在outputList中的位置
                int j = 0;
                outkprq1.add(Calendar.MONTH, -3);
                Calendar outkprq3 = outkprq1;
                for (Invoice invoice : outputList) {
                    if (invoice.getKprq().compareTo(outkprq3) < 0) {
                        j = outputList.indexOf(invoice);
                    }
                }
                // 计算近一个季度的销项金额之和
                for (Invoice invoice : outputList) {
                    int k = 0;
                    if (k < j) {
                        outputAmount += invoice.getJe();
                        k++;
                    } else {
                        break;
                    }
                }

                // 近一个季度若进项大于等于销项
                if (inputAmount > outputAmount) {
                    if (inputAmount * 0.5 >= outputAmount) {
                        return LOSS_SERIOUS + LOSS_WARNING_CONTINUOUS_QUARTER;
                    } else {
                        return LOSS_WARNING_CONTINUOUS_QUARTER;
                    }
                } else if (outputAmount >= inputAmount) {
                    if (outputAmount * 0.3 >= inputAmount) {
                        return HUGE_PROFIT;
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } else {
                // 计算总进项金额
                for (Invoice invoice : inputList) {
                    inputAmount += invoice.getJe();
                }
                // 计算总销项金额
                for (Invoice invoice : outputList) {
                    outputAmount += invoice.getJe();
                }

                // 若总体进项大于销项
                if (inputAmount > outputAmount) {
                    if (inputAmount * 0.5 >= outputAmount) {
                        return LOSS_SERIOUS + LOSS_WARNING;
                    } else {
                        return LOSS_WARNING;
                    }
                } else if (outputAmount >= inputAmount) {
                    if (outputAmount * 0.3 >= inputAmount) {
                        return HUGE_PROFIT;
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
        }
        return "";
    }

    /**
     * 分析发票作废情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeInvoiceInvalidRate(TreeSet<Invoice> inputInvoiceSet,
                                                    TreeSet<Invoice> outputInvoiceSet) {
        TreeSet<Invoice> invoices = new TreeSet<>();
        invoices.addAll(inputInvoiceSet);
        invoices.addAll(outputInvoiceSet);

        //将所有发票按从大到小放到List中,方便操作
        List<Invoice> invoiceList = new ArrayList<>();
        for(Invoice invoice : invoices) {
            invoiceList.add(0, invoice);
        }

        if(invoiceList.size() != 0) {
            Invoice invoice = invoiceList.get(0);
            Calendar kprq = invoice.getKprq();
            kprq.add(Calendar.MONTH, -1);

            //发票总数
            double sum = 1;
            //作废发票数量
            double invalidSum = 0;

            if("Y".equals(invoice.getZfbz())) {
                invalidSum ++;
            }

            if(invoiceList.size() > 1) {
                for(int i = 1; i < invoiceList.size(); i++) {
                    Invoice invoice2 = invoiceList.get(i);
                    Calendar kprq2 = invoice2.getKprq();

                    if(kprq.before(kprq2)) {
                        sum ++;
                        if("Y".equals(invoice2.getZfbz())) {
                            invalidSum ++;
                        }
                    }
                }

                Double ratio = 0.0;
                ratio = invalidSum / sum;
                if(ratio >= 0.1) {
                    return INVOICE_INVALID_RATE;
                } else {
                    return "";
                }

            } else {
                if("Y".equals(invoice.getZfbz())) {
                    return INVOICE_INVALID_RATE;
                } else {
                    return "";
                }

            }
        } else {
            return "";
        }
    }

    /**
     * 分析发票用量变动情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeInvoiceUsageChange(TreeSet<Invoice> inputInvoiceSet,
                                                    TreeSet<Invoice> outputInvoiceSet) {

        TreeSet<Invoice> invoices = new TreeSet<>();

        invoices.addAll(inputInvoiceSet);
        invoices.addAll(outputInvoiceSet);

        if(invoices.size() != 0) {
            //将数据放入到list中，方便操作
            ArrayList<Invoice> list = new ArrayList<>();
            for(Invoice invoice : invoices) {
                list.add(0, invoice);
            }

            HashMap<String, Integer> invoiceMap = new HashMap<>();

            for(int i = 0; i < list.size(); i++) {
                Invoice invoice = list.get(i);
                String kpyf = invoice.getKpyf();
                int sum = 1;

                for(int j = i + 1; j < list.size(); j++) {
                    Invoice invoice2 = list.get(j);
                    if(kpyf.equals(invoice2.getKpyf())) {
                        sum ++;
                        list.remove(j);
                        j --;
                    }
                }

                invoiceMap.put(kpyf, sum);
            }//计数器
            int count = 0;
            Set<String> set = invoiceMap.keySet();
            String dataNow = null;
            String dataUp = null;

            for(String s :set) {
                if(count == 0) {
                    dataNow = s;
                    count ++;
                    continue;
                }
                if(count == 1) {
                    dataUp = s;
                    break;
                }
            }

            if(dataUp != null) {
                Calendar calendarNow = Calendar.getInstance();
                Calendar calendarUp = Calendar.getInstance();

                String yearNow = dataNow.substring(0, 4);
                String monthNow = dataNow.substring(4);
                String yearUp = dataUp.substring(0, 4);
                String monthUp = dataUp.substring(4);

                calendarNow.set(Calendar.YEAR, Integer.parseInt(yearNow));
                calendarNow.set(Calendar.MONTH, Integer.parseInt(monthNow) - 1);
                calendarUp.set(Calendar.YEAR, Integer.parseInt(yearUp));
                calendarUp.set(Calendar.MONTH, Integer.parseInt(monthUp) - 1);
                calendarNow.add(Calendar.MONTH, -1);

                if(calendarNow.get(Calendar.YEAR) == calendarUp.get(Calendar.YEAR) && calendarNow.get(Calendar.MONTH) == calendarUp.get(Calendar.MONTH)) {
                    double rate = 0;
                    double countNow = invoiceMap.get(dataNow);
                    double countUp = invoiceMap.get(dataUp);

                    double change = countNow - countUp;
                    rate = change/countNow;
                    if(Math.abs(rate) >= 0.3 && change >= 10) {
                        return INVOICE_USAGE_CHANGE;
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }

            } else {
                return "";
            }
        } else {
            return "";
        }
    }



    /**
     * 分析税负波动情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeTaxChange(TreeSet<Invoice> inputInvoiceSet,
                                           TreeSet<Invoice> outputInvoiceSet) {
        //key年月，value对应月应纳税额、应税销售收入
        List<Invoice> inputList = new ArrayList<>();
        List<Invoice> outputList = new ArrayList<>();
        Map<String, Double[]> inputIfo = new HashMap<>();
        Map<String, Double[]> outputIfo = new HashMap<>();
        Double[] value;

        // 将发票按日期从远到近放入list中
        for(Invoice invoice : inputInvoiceSet) {
            inputList.add(0, invoice);
        }
        for(Invoice invoice : outputInvoiceSet) {
            outputList.add(0, invoice);
        }

        if(inputInvoiceSet.size() > 0) {
            if(inputInvoiceSet.size() == 1) {
                Invoice key = inputInvoiceSet.first();
                value = new Double[2];
                value[0] = key.getSe();
                value[1] = key.getJe();
                inputIfo.put(key.getKpyf(), value);
            } else {
                for(int i= 0; i < inputList.size(); i++) {
                    Invoice invoice = inputList.get(i);
                    value = new Double[2];
                    double tempSe = 0;
                    double tempJe = 0;
                    String key = invoice.getKpyf();
                    tempSe += invoice.getSe();
                    tempJe += invoice.getJe();

                    for(int j = i + 1; j < inputList.size(); j++) {
                        Invoice invoice2 = inputList.get(j);
                        if(invoice.getKpyf().equals(invoice2.getKpyf())) {
                            tempSe += invoice2.getSe();
                            tempJe += invoice2.getJe();
                            inputList.remove(j--);
                        }
                    }

                    value[0] = tempSe;
                    value[1] = tempJe;
                    inputIfo.put(key, value);
                }
            }
        }

        if(outputList.size() > 0) {
            if(outputList.size() == 1) {
                Invoice key = outputList.get(0);
                value = new Double[2];
                value[0] = key.getSe();
                value[1] = key.getJe();
                outputIfo.put(key.getKpyf(), value);
            } else {
                for(int i= 0; i < outputList.size(); i++) {
                    Invoice invoice = outputList.get(i);
                    value = new Double[2];
                    double tempSe = 0;
                    double tempJe = 0;
                    String key = invoice.getKpyf();
                    tempSe += invoice.getSe();
                    tempJe += invoice.getJe();

                    for(int j = i + 1; j < outputList.size(); j++) {
                        Invoice invoice2 = outputList.get(j);
                        if(invoice.getKpyf().equals(invoice2.getKpyf())) {
                            tempSe += invoice2.getSe();
                            tempJe += invoice2.getJe();
                            outputList.remove(j--);
                        }
                    }

                    value[0] = tempSe;
                    value[1] = tempJe;
                    outputIfo.put(key, value);
                }
            }
        }

        return computeChangeTax(inputIfo, outputIfo);
    }

    /**
     * 计算税负变动率
     *
     * @param inputIfo
     * @param outputIfo
     * @return
     */
    private static String computeChangeTax(Map<String, Double[]> inputIfo,
                                           Map<String, Double[]> outputIfo) {

        if(inputIfo.size() != 0 && outputIfo.size() != 0) {
            Set<String> inputKeys = inputIfo.keySet();
            Set<String> outputKeys = outputIfo.keySet();
            String inputKeyNow = null;
            String inputKeyUp = null;
            String outputKeyNow = null;
            String outputKeyUp = null;
            //计算器
            int count = 1;
            Double[] inputDoubles = new Double[2];
            Double[] outputDoubles = new Double[2];

            for(String key : inputKeys) {
                if(count == 1) {
                    inputKeyNow = key;
                    count ++;
                    continue;
                }
                if(count == 2) {
                    inputKeyUp = key;
                    count ++;
                }
            }

            count = 1;
            for(String key : outputKeys) {
                if(count == 1) {
                    outputKeyNow = key;
                    count ++;
                    continue;
                }
                if(count == 2) {
                    outputKeyUp = key;
                }
            }

            //计算
            double now = 0;
            double up = 0;
            double tempSeNow = 0;
            double tempJeNow = 0;
            double tempSeUp = 0;
            double tempJeUp = 0;
            String dateNow = null;
            String dateUp = null;

            if(inputKeyNow.equals(outputKeyNow)) {
                tempSeNow = outputIfo.get(outputKeyNow)[0] - inputIfo.get(inputKeyNow)[0];
                tempJeNow = outputIfo.get(outputKeyNow)[1] - inputIfo.get(inputKeyNow)[1];
                dateNow = inputKeyNow;
                if(tempJeNow != 0) {
                    now = tempSeNow/tempJeNow;
                }

            }
            if(inputKeyNow.compareTo(outputKeyNow) < 0) {
                tempSeNow = outputIfo.get(outputKeyNow)[0];
                tempJeNow = outputIfo.get(outputKeyNow)[1];
                dateNow = outputKeyNow;
                if(tempJeNow != 0) {
                    now = tempSeNow/tempJeNow;
                }

            }
            if(inputKeyNow.compareTo(outputKeyNow) > 0) {
                tempSeNow = 0 - inputIfo.get(inputKeyNow)[0];
                tempJeNow = 0 - inputIfo.get(inputKeyNow)[1];
                dateNow = inputKeyNow;
                if(tempJeNow != 0) {
                    now = tempSeNow/tempJeNow;
                }

            }

            if(inputKeyUp != null && outputKeyUp != null) {
                if(inputKeyUp.equals(outputKeyUp)) {
                    tempSeUp = outputIfo.get(outputKeyUp)[0] - inputIfo.get(inputKeyUp)[0];
                    tempJeUp = outputIfo.get(outputKeyUp)[1] - inputIfo.get(inputKeyUp)[1];
                    dateUp = inputKeyUp;
                    if(tempJeUp != 0) {
                        up = tempSeUp/tempJeUp;
                    }

                }
                if(inputKeyUp.compareTo(outputKeyUp) < 0) {
                    tempSeUp = outputIfo.get(outputKeyUp)[0];
                    tempJeUp = outputIfo.get(outputKeyUp)[1];
                    dateUp = outputKeyUp;
                    if(tempJeUp != 0) {
                        up = tempSeUp/tempJeUp;
                    }

                }
                if(inputKeyUp.compareTo(outputKeyUp) > 0) {
                    tempSeUp = 0 - inputIfo.get(inputKeyUp)[0];
                    tempJeUp = 0 - inputIfo.get(inputKeyUp)[1];
                    dateUp = inputKeyUp;
                    if(tempJeUp != 0) {
                        up = tempSeUp/tempJeUp;
                    }

                }
            }
            if(inputKeyUp != null && outputKeyUp == null) {
                tempSeUp = 0 - inputIfo.get(inputKeyUp)[0];
                tempJeUp = 0 - inputIfo.get(inputKeyUp)[1];
                dateUp = inputKeyUp;
                if(tempJeUp != 0) {
                    up = tempSeUp/tempJeUp;
                }

            }
            if(inputKeyUp == null && outputKeyUp != null) {
                tempSeUp = outputIfo.get(outputKeyUp)[0];
                tempJeUp = outputIfo.get(outputKeyUp)[1];
                dateUp = outputKeyUp;
                if(tempJeUp != 0) {
                    up = tempSeUp/tempJeUp;
                }

            }

            if(up == 0) {
                return "";
            } else {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                String dateNowYear = dateNow.substring(0, 4);
                String dateNowMonth = dateNow.substring(4);

                String dateUpYear = dateUp.substring(0, 4);
                String dateUpMonth = dateUp.substring(4);

                c1.set(Calendar.YEAR, Integer.parseInt(dateNowYear));
                c1.set(Calendar.MONTH, Integer.parseInt(dateNowMonth));
                c2.set(Calendar.YEAR, Integer.parseInt(dateUpYear));
                c2.set(Calendar.MONTH, Integer.parseInt(dateUpMonth));
                c1.add(Calendar.MONTH, -1);

                if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                    double change = (now - up)/up;
                    String result = Math.abs(change) > 0.3 ? TAX_CHANGE : "";
                    return result;
                } else {
                    return "";
                }

            }

        }

        if(inputIfo.size() != 0 && outputIfo.size() == 0) {
            Set<String> inputKeys = inputIfo.keySet();
            String inputKeyNow = null;
            String inputKeyUp = null;
            //计算器
            int count = 1;

            for(String key : inputKeys) {
                if(count == 1) {
                    inputKeyNow = key;
                    count ++;
                    continue;
                }
                if(count == 2) {
                    inputKeyUp = key;
                    count ++;
                }
            }

            //计算
            double now = 0;
            double up = 0;
            double tempSeNow = 0;
            double tempJeNow = 0;
            double tempSeUp = 0;
            double tempJeUp = 0;
            String dateNow = null;
            String dateUp = null;

            tempSeNow += inputIfo.get(inputKeyNow)[0];
            tempJeNow += inputIfo.get(inputKeyNow)[1];
            if(tempJeNow != 0) {
                now = tempSeNow/tempJeNow;
            }
            dateNow = inputKeyNow;

            if(inputKeyUp != null) {
                tempSeUp = 0 - inputIfo.get(inputKeyUp)[0];
                tempJeUp = 0 - inputIfo.get(inputKeyUp)[1];
                if(tempJeUp != 0) {
                    up = tempSeUp/tempJeUp;
                }
                dateUp = inputKeyUp;
            }

            if(up == 0) {
                return "";
            } else {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                String dateNowYear = dateNow.substring(0, 4);
                String dateNowMonth = dateNow.substring(4);

                String dateUpYear = dateUp.substring(0, 4);
                String dateUpMonth = dateUp.substring(4);

                c1.set(Calendar.YEAR, Integer.parseInt(dateNowYear));
                c1.set(Calendar.MONTH, Integer.parseInt(dateNowMonth));
                c2.set(Calendar.YEAR, Integer.parseInt(dateUpYear));
                c2.set(Calendar.MONTH, Integer.parseInt(dateUpMonth));
                c1.add(Calendar.MONTH, -1);

                if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                    double change = (now - up)/up;
                    String result = Math.abs(change) > 0.3 ? TAX_CHANGE : "";
                    return result;
                } else {
                    return "";
                }
            }
        }

        if(inputIfo.size() == 0 && outputIfo.size() != 0) {
            Set<String> outputKeys = outputIfo.keySet();
            String outputKeyNow = null;
            String outputKeyUp = null;
            //计算器
            int count = 1;

            for(String key : outputKeys) {
                if(count == 1) {
                    outputKeyNow = key;
                    count ++;
                    continue;
                }
                if(count == 2) {
                    outputKeyUp = key;
                    count ++;
                }
            }

            //计算
            double now = 0;
            double up = 0;
            double tempSeNow = 0;
            double tempJeNow = 0;
            double tempSeUp = 0;
            double tempJeUp = 0;
            String dateNow = null;
            String dateUp = null;

            tempSeNow += outputIfo.get(outputKeyNow)[0];
            tempJeNow += outputIfo.get(outputKeyNow)[1];
            if(tempJeNow != 0) {
                now = tempSeNow/tempJeNow;
            }
            dateNow = outputKeyNow;

            if(outputKeyUp != null) {
                tempSeUp += outputIfo.get(outputKeyUp)[0];
                tempJeUp += outputIfo.get(outputKeyUp)[1];
                if(tempJeUp != 0) {
                    up = tempSeUp/tempJeUp;
                }
                dateUp = outputKeyUp;
            }

            if(up == 0) {
                return "";
            } else {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                String dateNowYear = dateNow.substring(0, 4);
                String dateNowMonth = dateNow.substring(4);

                String dateUpYear = dateUp.substring(0, 4);
                String dateUpMonth = dateUp.substring(4);

                c1.set(Calendar.YEAR, Integer.parseInt(dateNowYear));
                c1.set(Calendar.MONTH, Integer.parseInt(dateNowMonth) - 1);
                c2.set(Calendar.YEAR, Integer.parseInt(dateUpYear));
                c2.set(Calendar.MONTH, Integer.parseInt(dateUpMonth) - 1);
                c1.add(Calendar.MONTH, -1);

                if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                    double change = (now - up)/up;
                    String result = Math.abs(change) > 0.3 ? TAX_CHANGE : "";
                    return result;
                } else {
                    return "";
                }
            }
        }

        if(inputIfo.size() == 0 && outputIfo.size() == 0) {
            return "";
        }

        return "";
    }

    /**
     * 分析发票用量情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeInvoiceUsage(TreeSet<Invoice> inputInvoiceSet,
                                              TreeSet<Invoice> outputInvoiceSet) {
        long inputInvoiceNum = inputInvoiceSet.size();
        long outputInvoiceNum = outputInvoiceSet.size();

        if (inputInvoiceNum == 0 && outputInvoiceNum == 0) {
            return NO_INPUT_OUTPUT_INVOICE;
        }else if (inputInvoiceNum == 0) {
            return NO_INPUT_INVOICE;
        } else if (outputInvoiceNum == 0) {
            return NO_OUTPUT_INVOICE;
        } else {
            return analyzeLongTimeInvoiceInput(inputInvoiceSet, outputInvoiceSet);
        }
    }

    /**
     * 分析进项发票数量情况
     *
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeLongTimeInvoiceInput(TreeSet<Invoice> inputInvoiceSet,
                                                      TreeSet<Invoice> outputInvoiceSet) {
        if (inputInvoiceSet.size() > 1) {
            // 获取最远时间发票
            Invoice first = inputInvoiceSet.first();
            // 获取最近时间的发票
            Invoice last = inputInvoiceSet.last();

            if (first.getKprq().compareTo(last.getKprq()) != 0) {
                for (Invoice invoice1 : inputInvoiceSet) {
                    Invoice invoice2 = inputInvoiceSet.higher(invoice1);

                    if (invoice2 == null) {
                        continue;
                    } else {
                        Calendar kprq1 = invoice1.getKprq();
                        Calendar kprq2 = invoice2.getKprq();

                        long time1 = kprq1.getTimeInMillis();
                        long time2 = kprq2.getTimeInMillis();
                        long time3 = Math.abs(time1 - time2) / 1000 / 60 / 60 / 24;

                        // 得到销项发票信息
                        String outputMessage = analyzeLongTimeInvoiceOutput(outputInvoiceSet);
                        if (time3 >= 30) {
                            if (outputMessage.equals(LONG_TIME_NO_OUTPUT)) {
                                return LONG_TIME_NO_INPUT_OUTPUT;
                            } else if (outputMessage.equals(INVOICE_NUMBER_ABNORMALITY)) {
                                return INVOICE_NUMBER_ABNORMALITY;
                            } else {
                                return LONG_TIME_NO_INPUT;
                            }
                        } else {
                            if (outputMessage.equals(LONG_TIME_NO_OUTPUT)) {
                                return  LONG_TIME_NO_OUTPUT;
                            } else if (outputMessage.equals(INVOICE_NUMBER_ABNORMALITY)) {
                                return INVOICE_NUMBER_ABNORMALITY;
                            } else {
                                return "";
                            }
                        }
                    }
                }
            }
        } else {
            return INVOICE_NUMBER_ABNORMALITY;
        }
        return INVOICE_NUMBER_ABNORMALITY;
    }

    /**
     * 分析销项发票数量情况
     *
     * @param outputInvoiceSet
     * @return
     */
    private static String analyzeLongTimeInvoiceOutput(TreeSet<Invoice> outputInvoiceSet) {
        if (outputInvoiceSet.size() > 1) {
            // 获取最远时间发票
            Invoice first = outputInvoiceSet.first();
            // 获取最近时间的发票
            Invoice last = outputInvoiceSet.last();

            if (first.getKprq().compareTo(last.getKprq()) != 0) {
                for (Invoice invoice1 : outputInvoiceSet) {
                    Invoice invoice2 = outputInvoiceSet.higher(invoice1);

                    if (invoice2 == null) {
                        continue;
                    } else {
                        Calendar kprq1 = invoice1.getKprq();
                        Calendar kprq2 = invoice2.getKprq();

                        long time1 = kprq1.getTimeInMillis();
                        long time2 = kprq2.getTimeInMillis();
                        long time3 = Math.abs(time1 - time2) / 1000 / 60 / 60 / 24;

                        if (time3 >= 30) {
                            return LONG_TIME_NO_OUTPUT;
                        } else {
                            return "";
                        }
                    }
                }

            } else {
                return "";
            }
        } else {
            return "INVOICE_NUMBER_ABNORMALITY";
        }
        return "INVOICE_NUMBER_ABNORMALITY";
    }

}
