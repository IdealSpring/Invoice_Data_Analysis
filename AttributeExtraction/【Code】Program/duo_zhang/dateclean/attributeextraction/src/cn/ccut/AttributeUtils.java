package cn.ccut;

import com.sun.jersey.api.representation.Form;

import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 计算属性工具类
 */
public class AttributeUtils {
    private AttributeUtils() {}

    /**
     * 1.inputInvoice属性
     * 2.outputInvoice属性
     * 提取
     *
     * @param enterprise
     */
    public static void setInputAndOutputInoive(Enterprise enterprise) {
        long inputInvoiceNum = enterprise.getInputInvoiceNum();
        long outputInvoiceNum = enterprise.getOutputInvoiceNum();

        if(inputInvoiceNum == 0) {
            enterprise.setInputInvoice("none");
        } else {
            enterprise.setInputInvoice("exist");
        }

        if(outputInvoiceNum == 0) {
            enterprise.setOutputInvoice("none");
        } else {
            enterprise.setOutputInvoice("exist");
        }
    }

    /**
     * 计算:
     *      3.inputInterval 	--最近两次进项开票时间
     *
     * @param inputInvoiceSet   进项发票
     */
    public static void setInputInterval(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet) {
        if(inputInvoiceSet.size() > 0) {
            if(inputInvoiceSet.size() > 1) {
                //获取最远时间的发票
                Invoice first = inputInvoiceSet.first();
                //获取最近时间的发票
                Invoice last = inputInvoiceSet.last();

                if(first.getKprq().compareTo(last.getKprq()) != 0) {
                    for(Invoice invoice1 : inputInvoiceSet) {
                        Invoice invoice2 = inputInvoiceSet.higher(invoice1);

                        if(invoice2 == null) {
                            continue;
                        } else {
                            Calendar kprq1 = invoice1.getKprq();
                            Calendar kprq2 = invoice2.getKprq();

                            //获取月份
                            long time1 = kprq1.getTimeInMillis();
                            long time2 = kprq2.getTimeInMillis();

                            long time3 = Math.abs(time1 - time2)/1000/60/60/24;

                            if(time3 <= 30) {
                                enterprise.setInputInterval("withinOneMonth");
                            } else if(time3 <= 60) {
                                enterprise.setInputInterval("withinTwoMonth");
                            } else {
                                enterprise.setInputInterval("withoutMonth");
                            }
                        }
                    }
                } else {
                    enterprise.setInputInterval("withinOneMonth");
                }
            } else {
                enterprise.setInputInterval("withinOneMonth");
            }
        } else {
            enterprise.setInputInterval("withoutInvoice");
        }
    }

    /**
     * 4.outputInterval 	--最近两次销项开票时间
     *
     * @param enterprise
     * @param outputInvoiceSet
     */
    public static void setOutputInterval(Enterprise enterprise, TreeSet<Invoice> outputInvoiceSet) {
        if(outputInvoiceSet.size() > 0) {
            if(outputInvoiceSet.size() > 1) {
                //获取最远时间的发票
                Invoice first = outputInvoiceSet.first();
                //获取最近时间的发票
                Invoice last = outputInvoiceSet.last();

                if(first.getKprq().compareTo(last.getKprq()) != 0) {
                    for(Invoice invoice1 : outputInvoiceSet) {
                        Invoice invoice2 = outputInvoiceSet.higher(invoice1);

                        if(invoice2 == null) {
                            continue;
                        } else {
                            Calendar kprq1 = invoice1.getKprq();
                            Calendar kprq2 = invoice2.getKprq();

                            //获取月份
                            long time1 = kprq1.getTimeInMillis();
                            long time2 = kprq2.getTimeInMillis();

                            long time3 = Math.abs(time1 - time2)/1000/60/60/24;

                            if(time3 <= 30) {
                                enterprise.setOutputInterval("withinOneMonth");
                            } else if(time3 <= 60) {
                                enterprise.setOutputInterval("withinTwoMonth");
                            } else {
                                enterprise.setOutputInterval("withoutMonth");
                            }
                        }
                    }
                } else {
                    enterprise.setOutputInterval("withinOneMonth");
                }
            } else {
                enterprise.setOutputInterval("withinOneMonth");
            }
        } else {
            enterprise.setOutputInterval("withoutInvoice");
        }
    }


    /**
     * 计算：
     *      5.taxChangeRate		--税负变动率
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void setTaxChangeRate(Enterprise enterprise,
                                        TreeSet<Invoice> inputInvoiceSet,
                                        TreeSet<Invoice> outputInvoiceSet) {
        //key年月，value对应月应纳税额、应税销售收入
        List<Invoice> inputList = new ArrayList<>();
        List<Invoice> outputList = new ArrayList<>();
        Map<String, Double[]> inputIfo = new HashMap<>();
        Map<String, Double[]> outputIfo = new HashMap<>();
        Double[] value;

        //将TreeSet中的元素放到list中，方便操作
        for(Invoice tempInvoice : inputInvoiceSet) {
            Invoice invoice = tempInvoice;
            inputList.add(0, invoice);
        }
        for(Invoice tempInvoice : outputInvoiceSet) {
            Invoice invoice = tempInvoice;
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

        //计算
        computeChangeTax(enterprise, inputIfo, outputIfo);
    }

    private static void computeChangeTax(Enterprise enterprise,
                                  Map<String, Double[]> inputIfo,
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
                enterprise.setTaxChangeRate("none");
                //enterprise.setTaxChangeRate("0");
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
                    String result = Math.abs(change) > 0.3 ? "high" : "low";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
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
                enterprise.setTaxChangeRate("none");
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
                    String result = Math.abs(change) > 0.3 ? "high" : "low";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
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
                enterprise.setTaxChangeRate("none");
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
                    String result = Math.abs(change) > 0.3 ? "high" : "low";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
                }
            }
        }

        if(inputIfo.size() == 0 && outputIfo.size() == 0) {
            enterprise.setTaxChangeRate("none");
        }

    }

    /**
     * 6.属性名:invoiceUsageChange	--发票用量变动
     *      属性值:none				--本月和上一个月均无发票
     *      low				        --低
     *      high				    --高
     *
     *      计算公式：指标值=一般纳税人专票使用量-一般纳税人专票上月使用量。
     *      预警值：纳税人开具增值税专用发票超过上月30%（含）并超过上月10份以上。
     *
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void setInvoiceUsageChange(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet, TreeSet<Invoice> outputInvoiceSet) {
        TreeSet<Invoice> invoices = new TreeSet<>();

        invoices.addAll(inputInvoiceSet);
        invoices.addAll(outputInvoiceSet);

        /*System.out.println(enterprise.getNsr_id() + "=");

        for(Invoice invoice : invoices) {
            System.out.println(invoice.getKpyf() + "---" + invoice.getKprq().toString());
        }

        System.out.println("结束");*/

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
            }

            //计数器
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
                        enterprise.setInvoiceUsageChange("high");
                    } else {
                        enterprise.setInvoiceUsageChange("low");
                    }
                } else {
                    enterprise.setInvoiceUsageChange("none");
                }

            } else {
                enterprise.setInvoiceUsageChange("none");
            }


        } else {
            enterprise.setInvoiceUsageChange("none");
        }

    }

    /**
     * 【7.】进项税额变动率高于销项税额变动率
     *      1.计算公式：指标值=（进项税额变动率-销项税额变动率）/销项税额变动率；
     *                  进项税额变动率额=(本期进项-上期进项)/上期进项；
     *                  销项税额变动率=(本期销项-上期销项)/上期销项。
     *
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void setInputTaxAndOutputTaxRatio(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet, TreeSet<Invoice> outputInvoiceSet) {
        //将TreeSet中的数据放入到List中，方便操作
        List<Invoice> inputList = new ArrayList<>();
        List<Invoice> outputList = new ArrayList<>();

        for(Invoice invoice : inputInvoiceSet) {
            inputList.add(0, invoice);
        }
        for(Invoice invoice : outputInvoiceSet) {
            outputList.add(0, invoice);
        }

        //进项税额
        HashMap<String, Double> inputTaxMap = new HashMap<>();
        //销项税额
        HashMap<String, Double> outputTaxMap = new HashMap<>();

        for(int i = 0; i < inputList.size(); i++) {
            Invoice invoice = inputList.get(i);
            String kpyf = invoice.getKpyf();
            double tempInput = 0;
            tempInput += invoice.getJshj();

            for(int j = i + 1; j < inputList.size(); j++) {
                Invoice invoice2 = inputList.get(j);
                String kpyf2 = invoice2.getKpyf();
                if(kpyf.equals(kpyf2)) {
                    tempInput += invoice2.getJshj();
                    inputList.remove(j);
                    j--;
                }
            }

            inputTaxMap.put(kpyf, tempInput);
        }

        for(int i = 0; i < outputList.size(); i++) {
            Invoice invoice = outputList.get(i);
            String kpyf = invoice.getKpyf();
            double tempOutput = 0;
            tempOutput += invoice.getJshj();

            for(int j = i + 1; j < outputList.size(); j++) {
                Invoice invoice2 = outputList.get(j);
                String kpyf2 = invoice2.getKpyf();
                if(kpyf.equals(kpyf2)) {
                    tempOutput += invoice2.getJshj();
                    outputList.remove(j);
                    j--;
                }
            }

            outputTaxMap.put(kpyf, tempOutput);
        }

        //计算
        computeInputTaxAndOutputTaxRatio(enterprise, inputTaxMap, outputTaxMap);
    }

    /**
     * 计算
     *
     * @param enterprise
     * @param inputTaxMap
     * @param outputTaxMap
     */
    private static void computeInputTaxAndOutputTaxRatio(Enterprise enterprise, HashMap<String, Double> inputTaxMap, HashMap<String, Double> outputTaxMap) {
        if(inputTaxMap.size() != 0 && outputTaxMap.size() != 0) {
            Set<String> inputSet = inputTaxMap.keySet();
            Set<String> outputSet = outputTaxMap.keySet();
            int count = 0;
            String inputNowKey = null;
            String outputNowKey = null;
            String inputUpKey = null;
            String outputUpKey = null;

            for(String key : inputSet) {
                if(count == 0) {
                    inputNowKey = key;
                    count ++;
                    continue;
                }
                if(count == 1) {
                    inputUpKey = key;
                    count = 0;
                    break;
                }
            }

            //置零
            count = 0;
            for(String key : outputSet) {
                if(count == 0) {
                    outputNowKey = key;
                    count ++;
                    continue;
                }
                if(count == 1) {
                    outputUpKey = key;
                    break;
                }
            }

            //现在时间
            String nowDate = null;
            //上期时间
            String upDate = null;

            //情况一：上期都存在
            if(inputUpKey != null && outputUpKey != null) {
                if(inputNowKey.compareTo(outputNowKey) == 0) {
                    nowDate = inputNowKey;
                } else if(inputNowKey.compareTo(outputNowKey) < 0) {
                    nowDate = outputNowKey;
                } else {
                    nowDate = inputNowKey;
                }

                if(inputUpKey.compareTo(outputUpKey) == 0) {
                    upDate = inputUpKey;
                } else if(inputUpKey.compareTo(outputUpKey) < 0) {
                    upDate = outputUpKey;
                } else {
                    upDate = inputUpKey;
                }

                Calendar now = Calendar.getInstance();
                Calendar up = Calendar.getInstance();
                String nowYear = nowDate.substring(0, 4);
                String nowMonth = nowDate.substring(4);
                String upYear = upDate.substring(0, 4);
                String upMonth = upDate.substring(4);
                now.set(Calendar.YEAR, Integer.parseInt(nowYear));
                now.set(Calendar.MONTH, Integer.parseInt(nowMonth) - 1);
                up.set(Calendar.YEAR, Integer.parseInt(upYear));
                up.set(Calendar.MONTH, Integer.parseInt(upMonth) - 1);
                now.add(Calendar.MONTH, -1);

                if(now.get(Calendar.YEAR) == up.get(Calendar.YEAR) && now.get(Calendar.MONTH) == up.get(Calendar.MONTH)) {
                    Double ratio = 0.0;
                    double inputRatio = 0;
                    double upRatio = 0;

                    inputRatio = (inputTaxMap.get(inputNowKey) - inputTaxMap.get(inputUpKey))/inputTaxMap.get(inputUpKey);
                    upRatio = (outputTaxMap.get(outputNowKey) - outputTaxMap.get(outputUpKey))/outputTaxMap.get(outputUpKey);
                    ratio = (inputRatio - upRatio)/upRatio;

                    if(!ratio.isNaN()) {
                        if(ratio > 0.1) {
                            enterprise.setInputTaxAndOutputTaxRatio("high");
                        } else {
                            enterprise.setInputTaxAndOutputTaxRatio("low");
                        }
                    } else {
                        enterprise.setInputTaxAndOutputTaxRatio("none");
                    }

                } else {
                    enterprise.setInputTaxAndOutputTaxRatio("none");
                }

                //情况二:上期进项存在，销项不存在
            } else if(inputUpKey != null && outputUpKey == null) {
                enterprise.setInputTaxAndOutputTaxRatio("none");

                //情况三:上期进项不存在,销项存在
            } else if(inputUpKey == null && outputUpKey != null) {
                enterprise.setInputTaxAndOutputTaxRatio("none");
            } else {
                enterprise.setInputTaxAndOutputTaxRatio("none");
            }

        } else if(inputTaxMap.size() == 0 && outputTaxMap.size() != 0) {
            enterprise.setInputTaxAndOutputTaxRatio("none");
        } else {
            //inputTaxMap.size() == 0 && outputTaxMap.size() == 0
            enterprise.setInputTaxAndOutputTaxRatio("none");
        }
    }

    /**
     * 发票作废率
     *
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void setInvoiceInvalidRatio(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet, TreeSet<Invoice> outputInvoiceSet) {
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
                ratio = invalidSum/sum;
                if(ratio >= 0.3) {
                    enterprise.setInvoiceInvalidRatio("high");
                } else if(ratio > 0) {
                    enterprise.setInvoiceInvalidRatio("low");
                } else {
                    enterprise.setInvoiceInvalidRatio("none");
                }

            } else {
                if("Y".equals(invoice.getZfbz())) {
                    enterprise.setInvoiceInvalidRatio("high");
                } else {
                    enterprise.setInvoiceInvalidRatio("none");
                }

            }

        } else {
            enterprise.setInvoiceInvalidRatio("none");
        }
    }


    /**
     * 9. --发票显示连续亏损
     *      none					-- 没有亏损
     *      continuousQuarter		-- 连续一个季度
     *      overallLoss				-- 总体亏损
     *
     * @param enterprise
     * @param inputInvoiceSet
     * @param outputInvoiceSet
     */
    public static void setContinuousLoss(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet, TreeSet<Invoice> outputInvoiceSet) {
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

        // 若进销项有一项不存在, 设值unknown
        if ( inum <= 0 || onum <= 0 || (inum <= 0 && onum <= 0)) {
            enterprise.setContinuousLoss("unknown");
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

            // 若发票时间跨度大于一个季度
            if (time1 > 30) {
                // 记录最近一个月最早的发票在inputList中的位置
                int i = 0;
                inkprq1.add(Calendar.MONTH, -1);
                Calendar inkprq3 = inkprq1;
                for (Invoice invoice : inputList) {
                    if (invoice.getKprq().compareTo(inkprq3) < 0) {
                        i = inputList.indexOf(invoice);
                    }
                }
                // 计算近一个月的进项金额之和
                for (Invoice invoice : inputList) {
                    int k = 0;
                    if (k < i) {
                        inputAmount += invoice.getJe();
                        k++;
                    } else {
                        break;
                    }
                }

                // 记录最近一个月最早的发票在outputList中的位置
                int j = 0;
                outkprq1.add(Calendar.MONTH, -1);
                Calendar outkprq3 = outkprq1;
                for (Invoice invoice : outputList) {
                    if (invoice.getKprq().compareTo(outkprq3) < 0) {
                        j = outputList.indexOf(invoice);
                    }
                }
                // 计算近一个月的销项金额之和
                for (Invoice invoice : outputList) {
                    int k = 0;
                    if (k < j) {
                        outputAmount += invoice.getJe();
                        k++;
                    } else {
                        break;
                    }
                }

                // 近一个月若进项大于等于销项
                if (inputAmount >= outputAmount) {
                    // 设值:continuousQuarter
                    enterprise.setContinuousLoss("continuousMonth");
                } else {
                    enterprise.setContinuousLoss("none");
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
                if (inputAmount >= outputAmount) {
                    enterprise.setContinuousLoss("overallLoss");
                } else {
                    enterprise.setContinuousLoss("none");
                }
            }
        }
    }

    public static void setInvoiceBalance(Enterprise enterprise, TreeSet<Invoice> inputInvoiceSet, TreeSet<Invoice> outputInvoiceSet){
        // 发票进销项金额
        double inputAmount = 0;
        double outputAmount = 0;

        // 将TreeSet的数据放入List中
        List<Invoice> inputList = new ArrayList<Invoice>();
        List<Invoice> outputList = new ArrayList<Invoice>();

        for (Invoice invoice : inputInvoiceSet) {
            inputList.add(0, invoice);
        }
        for (Invoice invoice : outputInvoiceSet) {
            outputList.add(0, invoice);
        }

        // 进销项发票的数目
        long inum = inputInvoiceSet.size();
        long onum = outputInvoiceSet.size();

        // 若进销项有一项不存在, 设值unknown
        if ( inum <= 0 || onum <= 0 || (inum <= 0 && onum <= 0)) {
            enterprise.setContinuousLoss("unknown");
        }

        //计算该公司进销项总金额
        for (Invoice invoice : inputList) {
            inputAmount += invoice.getJe();
        }
        for (Invoice invoice : outputList) {
            outputAmount += invoice.getJe();
        }

        //判断进销项差额是否巨大，销项权值为0.5，进项权值为0.3
        if (outputAmount <= inputAmount*0.5 || inputAmount <= outputAmount*0.3) {
            enterprise.setInvoiceBalance("balanceHuge");//差额巨大
        } else {
            enterprise.setInvoiceBalance("normal");//差额正常
        }
    }
}
