package cn.ccut;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class EnterpriseReducer extends Reducer<Text, Enterprise, Enterprise, NullWritable> {
    private Enterprise enterprise = new Enterprise();
    //一个企业进项发票
    private TreeSet<Invoice> inputInvoice = new TreeSet<>();
    //一个企业销项发票
    private TreeSet<Invoice> outputInvoice = new TreeSet<>();

    @Override
    protected void reduce(Text key, Iterable<Enterprise> values, Context context) throws IOException, InterruptedException {
        //计算虚开和未开
        computeInputAndOutputInvoiceNum(key, values);
        //计算连续1个月无申报
        computeNoReportOverSix();
        //计算税负率
        computeTaxChangeRate();

        //向文件中输出
        context.write(enterprise, NullWritable.get());

        //初始化成员变量
        enterprise.setInputNoDeclareOverSix("No");
        enterprise.setOutputNoDeclareOverSix("No");
        inputInvoice.clear();
        outputInvoice.clear();
    }

    /**
     * 计算税负率
     */
    private void computeTaxChangeRate() {
        //key年月，value对应月应纳税额、应税销售收入
        List<Invoice> inputList = new ArrayList<>();
        List<Invoice> outputList = new ArrayList<>();
        Map<String, Double[]> inputIfo = new HashMap<>();
        Map<String, Double[]> outputIfo = new HashMap<>();
        Double[] value;

        //将TreeSet中的元素放到list中，方便操作
        for(Invoice tempInvoice : inputInvoice) {
            Invoice invoice = tempInvoice;
            inputList.add(invoice);
        }
        for(Invoice tempInvoice : outputInvoice) {
            Invoice invoice = tempInvoice;
            outputList.add(invoice);
        }

        if(inputInvoice.size() > 0) {
            if(inputInvoice.size() == 1) {
                Invoice key = inputInvoice.first();
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

        System.out.println("Nsr_id" + enterprise.getNsr_id());
        Set<String> set = inputIfo.keySet();
        for(String key : set) {
            Double[] doubles = inputIfo.get(key);
            System.out.println("进项：" + key + "-->" + doubles[0]+ "********" + doubles[1]);
        }

        Set<String> set2 = outputIfo.keySet();
        for(String key : set2) {
            Double[] doubles = outputIfo.get(key);
            System.out.println("销项：" + key + "-->" + doubles[0]+ "********" + doubles[1]);
        }

        //计算
        setChangeTax(inputIfo, outputIfo);

    }

    private void setChangeTax(Map<String, Double[]> inputIfo, Map<String, Double[]> outputIfo) {
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

            //System.out.println(inputKeyNow + "---input---" + inputIfo.get(inputKeyNow)[0] + "---" + inputIfo.get(inputKeyNow)[1]);
            //System.out.println(inputKeyUp + "---input---" + inputIfo.get(inputKeyUp)[0] + "---" + inputIfo.get(inputKeyUp)[1]);
            //System.out.println(outputKeyNow + "---output---" + outputIfo.get(outputKeyNow)[0] + "---" + outputIfo.get(outputKeyNow)[1]);
            //System.out.println(outputKeyUp + "---output---" + outputIfo.get(outputKeyUp)[0] + "---" + outputIfo.get(outputKeyUp)[1]);

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

            if(tempJeUp == 0) {
                enterprise.setTaxChangeRate("上月无税负");
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
                    String result = Math.abs(change) > 0.3 ? "异常" : "正常";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("上月无税负");
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

            //System.out.println(inputKeyNow + "---input---" + inputIfo.get(inputKeyNow)[0] + "---" + inputIfo.get(inputKeyNow)[1]);
            //System.out.println(inputKeyUp + "---input---" + inputIfo.get(inputKeyUp)[0] + "---" + inputIfo.get(inputKeyUp)[1]);
            //System.out.println(outputKeyNow + "---output---" + outputIfo.get(outputKeyNow)[0] + "---" + outputIfo.get(outputKeyNow)[1]);
            //System.out.println(outputKeyUp + "---output---" + outputIfo.get(outputKeyUp)[0] + "---" + outputIfo.get(outputKeyUp)[1]);

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
                tempSeUp += inputIfo.get(inputKeyUp)[0];
                tempJeUp += inputIfo.get(inputKeyUp)[1];
                if(tempJeUp != 0) {
                    up = tempSeUp/tempJeUp;
                }
                dateUp = inputKeyUp;
            }

            if(tempJeUp == 0) {
                enterprise.setTaxChangeRate("上月无税负");
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
                    String result = Math.abs(change) > 0.3 ? "异常" : "正常";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("上月无税负");
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

            //System.out.println(inputKeyNow + "---input---" + inputIfo.get(inputKeyNow)[0] + "---" + inputIfo.get(inputKeyNow)[1]);
            //System.out.println(inputKeyUp + "---input---" + inputIfo.get(inputKeyUp)[0] + "---" + inputIfo.get(inputKeyUp)[1]);
            //System.out.println(outputKeyNow + "---output---" + outputIfo.get(outputKeyNow)[0] + "---" + outputIfo.get(outputKeyNow)[1]);
            //System.out.println(outputKeyUp + "---output---" + outputIfo.get(outputKeyUp)[0] + "---" + outputIfo.get(outputKeyUp)[1]);

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

            if(tempJeUp == 0) {
                enterprise.setTaxChangeRate("上月无税负");
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
                    String result = Math.abs(change) > 0.3 ? "异常" : "正常";
                    enterprise.setTaxChangeRate(result);
                } else {
                    enterprise.setTaxChangeRate("上月无税负");
                }
            }
        }
    }

    /**
     * 计算一个企业连续1个月无申报
     */
    private void computeNoReportOverSix() {
        //进项6个月无发票
        setNoDeclareOverSix(inputInvoice, 1);
        //销项6个月无发票
        setNoDeclareOverSix(outputInvoice, -1);

    }

    private void setNoDeclareOverSix(TreeSet<Invoice> invoice, int type) {
        if(invoice.size() > 1) {
            //获取最远时间的发票
            Invoice first = invoice.first();
            //获取最近时间的发票
            Invoice last = invoice.last();

            //如果最远时间的发票和最近时间的发票相同，跳过
            if(first.getKprq().compareTo(last.getKprq()) != 0) {
                for(Invoice invoice1 :invoice) {
                    Invoice invoice2 = invoice.higher(invoice1);

                    if(type > 0) {
                        if(invoice2 == null) {
                            continue;
                        } else {
                            Calendar kprq1 = invoice1.getKprq();
                            Calendar kprq2 = invoice2.getKprq();

                            //获取月份
                            long time1 = kprq1.getTimeInMillis();
                            long time2 = kprq2.getTimeInMillis();

                            long time3 = Math.abs(time1 - time2)/1000/60/60/24;

                            if(time3 >= 30) {
                                enterprise.setInputNoDeclareOverSix("Yes");
                            }
                        }
                    } else {
                        if(invoice2 == null) {
                            continue;
                        } else {
                            Calendar kprq1 = invoice1.getKprq();
                            Calendar kprq2 = invoice2.getKprq();

                            //获取月份
                            long time1 = kprq1.getTimeInMillis();
                            long time2 = kprq2.getTimeInMillis();

                            long time3 = Math.abs(time1 - time2)/1000/60/60/24;

                            if(time3 >= 30) {
                                enterprise.setOutputNoDeclareOverSix("Yes");
                            }
                        }
                    }
                }
            }

        }

    }

    /**
     * 计算虚开和未开
     * @param key
     * @param values
     */
    private void computeInputAndOutputInvoiceNum(Text key, Iterable<Enterprise> values) {
        long inputInvoiceNum = 0;
        long outputInvoiceNum = 0;
        Invoice invoice;

        for(Enterprise value : values) {
            //分离发票信息
            invoice = new Invoice();
            String line = value.getInovice();
            String[] split = StringUtils.split(line, ",");

            String fp_nid = split[0];
            String xf_id = split[1];
            String gf_id = split[2];
            double je = Double.parseDouble(split[3]);
            double se = Double.parseDouble(split[4]);
            double jshj = Double.parseDouble(split[5]);
            String kpyf = split[6];
            Calendar kprq = Calendar.getInstance();
            String[] date = StringUtils.split(split[7], "-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]) - 1;
            int day = Integer.parseInt(date[2]);
            kprq.set(year, month, day);
            String zfbz = split[8];

            invoice.setParas(fp_nid, xf_id, gf_id, je, se, jshj, kpyf, kprq, zfbz);

            //对进销项发票进项分类
            if(value.getNsr_id().equals(invoice.getGf_id())) {
                this.inputInvoice.add(invoice);
            }
            if(value.getNsr_id().equals(invoice.getXf_id())) {
                this.outputInvoice.add(invoice);
            }

            if(value.getGf_id().equals(value.getNsr_id())) {
                inputInvoiceNum ++;
            }
            if(value.getXf_id().equals(value.getNsr_id())) {
                outputInvoiceNum ++;
            }
        }

        enterprise.init(key.toString(), inputInvoiceNum, outputInvoiceNum);
    }
}
