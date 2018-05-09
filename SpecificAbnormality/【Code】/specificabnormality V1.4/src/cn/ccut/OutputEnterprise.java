package cn.ccut;

import java.io.FileOutputStream;
import java.io.IOException;
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

    // 异常分类文件存储路径
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalClassification\\";

    public static void outputEnterpriseByAbnormalNum(List<Enterprise> el6, List<Enterprise> el5,
                                                     List<Enterprise> el4, List<Enterprise> el3,
                                                     List<Enterprise> el2, List<Enterprise> el1) throws IOException {
        FileOutputStream fos;

        if (el6 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el6) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }

        if (el5 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el5) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }

        if (el4 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el4) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }

        if (el3 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el3) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }

        if (el2 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el2) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }

        if (el1 != null) {
            fos = new FileOutputStream(CLASSIFICATION_PATH + "[异常分析结果]", true);
            for (Enterprise enterprise : el1) {
                fos.write((enterprise + "\r\n").getBytes());
            }
            fos.close();
        }
    }
}
