package ccut.stage02;

import ccut.common.FilePathCollections;

import java.io.*;

/**
 * Description :  文件切割工具类
 * Created by 10851 on 2018/5/22.
 */
public class CutFile {
    public static void main(String args[]) {
//        cutArrr();

//        cutRes();

//        cutNsr1();

//        cutNsr2();

        cutLable();
    }

    /**
     * 属性文件切割成mahout朴素贝叶斯可读小文件
     */
    private static void cutArrr() {
        try {
            //正常企业属性信息
            String pathname = "D://视频诗词/异常企业资料/bayes/out1/part-r-00000";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            int count = 0;
            while ((line = br.readLine()) != null) {
                String str = "D:\\视频诗词\\异常企业资料\\bayes\\out3\\train-" + count++;
                FilePathCollections.clearUpresultOutputPathFile(str);
                File writename = new File(str);
                BufferedWriter out = new BufferedWriter(new FileWriter(writename, true));
                out.write(line + "");
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结果集大文件切割单条记录
     */
    private static void cutRes() {
        try {
            //mahout测试结果
            String pathname = "C:\\Users\\10851\\Desktop\\res.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            int ch = -1;
            int count = 0;

            while ((ch = br.read()) != -1) {
                if ((char) ch == 'K') {
                    String str = "D:\\视频诗词\\异常企业资料\\bayes\\out5\\res-" + count++;
                    File writename = new File(str);
                    BufferedWriter out = new BufferedWriter(new FileWriter(writename, true));
                    out.write((char) ch);
                    while ((ch = br.read()) != '}') {
                        out.write((char) ch);
                    }
                    out.write((char) ch);
                    out.flush();
                    out.close();
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地测试文件纳税人idqiege
     */
    private static void cutNsr1() {
        try {
            for (int i = 0; i < 10000; i++) {
                String pathname = "D://视频诗词/异常企业资料/bayes/out3/train-" + i;
                File filename = new File(pathname);
                if (!filename.exists())
                    break;
                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
                BufferedReader br = new BufferedReader(reader);
                int ch = -1;

                String str = "D:\\视频诗词\\异常企业资料\\bayes\\out6\\nsr-" + i;
                FilePathCollections.clearUpresultOutputPathFile(str);
                File writename = new File(str);
                BufferedWriter out = new BufferedWriter(new FileWriter(writename, true));

                while ((ch = br.read()) != -1) {
                    if ((char) ch != ',') {
                        out.write((char) ch);
                    } else {
                        break;
                    }
                }
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * mahout自动生成测试文件纳税人id切割
     */
    private static void cutNsr2() {
        try {
            String pathname = "C:\\Users\\10851\\Desktop\\tes.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            int ch = -1;
            int count = 0;
            boolean flag = true;

            while ((ch = br.read()) != -1) {
                if ((char) ch == '-') {
                    if (flag == true) {
                        String str = "D:\\视频诗词\\异常企业资料\\bayes\\out6\\nsr-" + count++;
                        FilePathCollections.clearUpresultOutputPathFile(str);
                        File writename = new File(str);
                        BufferedWriter out = new BufferedWriter(new FileWriter(writename, true));
                        while ((ch = br.read()) != ':') {
                            out.write((char) ch);
                        }
                        out.flush();
                        out.close();
                        flag = false;
                    } else {
                        flag = true;
                        continue;
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据结果集进行标签生成
     */
    private static void cutLable() {
        try {
            for (int i = 0; i < 10000; i++) {
                double yes = 0, no = 0;
                String string = null;
                String pathname = "D://视频诗词/异常企业资料/bayes/out5/res-" + i;
                File filename = new File(pathname);
                if (!filename.exists())
                    break;
                InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
                BufferedReader br = new BufferedReader(reader);
                int ch = -1;
                int count = 0;

                while ((ch = br.read()) != -1) {
                    if (((char) ch) != ':') {
                        continue;
                    } else {
                        count++;
                        if (count < 4)
                            continue;
                        else {
                            if (count == 4) {
                                string = "";
                                while ((ch = br.read()) != ',') {
                                    string += (char) ch;
                                }
                                no = Double.parseDouble(string);
                            }
                            if (count == 5) {
                                string = "";
                                while ((ch = br.read()) != '}') {
                                    string += (char) ch;
                                }
                                yes = Double.parseDouble(string);
                            }
                        }
                    }
                }

                String str = "D:\\视频诗词\\异常企业资料\\bayes\\out7\\lable-" + i;
                File writename = new File(str);
                BufferedWriter out = new BufferedWriter(new FileWriter(writename, true));
                if (no > yes) {
                    out.write("0");
                    out.flush();
                    out.close();
                } else {
                    out.write("1");
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
