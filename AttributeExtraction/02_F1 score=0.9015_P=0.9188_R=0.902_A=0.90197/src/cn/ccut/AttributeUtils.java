package cn.ccut;

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
            inputList.add(invoice);
        }
        for(Invoice tempInvoice : outputInvoiceSet) {
            Invoice invoice = tempInvoice;
            outputList.add(invoice);
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
                    /*DecimalFormat format = new DecimalFormat("0.00000");
                    String result = format.format(change);
                    if(result.equals("0.00000") || result.equals("-0.00000")){
                        result = "0";
                    }*/
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
                    //enterprise.setTaxChangeRate("0");
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
                    /*DecimalFormat format = new DecimalFormat("0.00000");
                    String result = format.format(change);
                    if(result.equals("0.00000") || result.equals("-0.00000")){
                        result = "0";
                    }*/
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
                    //enterprise.setTaxChangeRate("0");
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
                    /*DecimalFormat format = new DecimalFormat("0.00000");
                    String result = format.format(change);
                    if(result.equals("0.00000") || result.equals("-0.00000")){
                        result = "0";
                    }*/
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("none");
                    //enterprise.setTaxChangeRate("0");
                }
            }
        }

        if(inputIfo.size() == 0 && outputIfo.size() == 0) {
            enterprise.setTaxChangeRate("none");
        }

    }


}
