package cn.ccut.handle;

import cn.ccut.pojo.Enterprise;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.List;

/**
 * 输出企业异常信息类
 *
 * @authorMr.Robot
 * @create2018-05-09 19:07
 */
public class OutputEnterprise {
    public OutputEnterprise() {
    }

    // 异常文件名
    private static final String ABNORMAL_6 = "[异常6]";
    private static final String ABNORMAL_5 = "[异常5]";
    private static final String ABNORMAL_4 = "[异常4]";
    private static final String ABNORMAL_3 = "[异常3]";
    private static final String ABNORMAL_2 = "[异常2]";
    private static final String ABNORMAL_1 = "[异常1]";
    private static final String ABNORMAL_0 = "[异常0]";
    private static final String ABNORMAL = "[异常]";

    // 异常数量文件存储路径
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalResult\\";

    /**
     * 按异常个数的多少分类输出企业信息
     *
     * @param enterprise
     * @param abnormalNum
     * @throws IOException
     */
    public static void outputEnterpriseByAbnormalNum(Enterprise enterprise) throws IOException {
        FileOutputStream fos;
        int abnormalNum = enterprise.getAbnormalNum();

        if (abnormalNum == 6) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_6, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else if (abnormalNum == 5) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_5, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else if (abnormalNum == 4) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_4, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else if (abnormalNum == 3) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_3, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else if (abnormalNum == 2) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_2, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else if (abnormalNum == 1) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_1, true);
            fos.write((enterprise + "\r\n").getBytes());
            fos.close();
        } else {
            fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL_0, true);
            fos.write((enterprise.toStringForNone() + "\r\n").getBytes());
            fos.close();
        }
    }

    /**
     * 企业异常信息排名
     */
    public static void outputEnterpriseFromMoreToLess() throws IOException {
        // 文件输出路径
        String oF = CLASSIFICATION_PATH + ABNORMAL;

        // 获取文件夹下所有的文件名
        File f = new File(CLASSIFICATION_PATH);
        List fileList = Arrays.asList(f.list());

        // 存放存在的文件名
        int n = 20;
        int i = 0;
        String [] iF = new String[n];

        // 放入字符串数组中
        if (fileList.contains(ABNORMAL_6)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_6);
            i++;
        }

        if (fileList.contains(ABNORMAL_5)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_5);
            i++;
        }

        if (fileList.contains(ABNORMAL_4)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_4);
            i++;
        }

        if (fileList.contains(ABNORMAL_3)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_3);
            i++;
        }

        if (fileList.contains(ABNORMAL_2)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_2);
            i++;
        }

        if (fileList.contains(ABNORMAL_1)) {
            iF[i]=String.valueOf(CLASSIFICATION_PATH + ABNORMAL_1);
            i++;
        }

        FileOutputStream output = new FileOutputStream(new File(oF));
        WritableByteChannel targetChannel = output.getChannel();

        try {
            for (int j = 0; j < iF.length; j++) {
                FileInputStream input = new FileInputStream(iF[j]);
                FileChannel inputChannel = input.getChannel();

                inputChannel.transferTo(0, inputChannel.size(), targetChannel);

                inputChannel.close();
                input.close();
            }
        } catch (Exception e) {
            System.out.println();
        }

        targetChannel.close();
        output.close();
    }
}
