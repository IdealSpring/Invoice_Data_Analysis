package cn.demo;

import java.awt.*;
import java.io.*;

/**
 * @authorMr.Robot
 * @create2018-05-08 19:04
 */
public class StatisticalQuantityDemo {

    public static void main(String[] args) throws IOException {

/*        FileOutputStream fos;
        BufferedReader bufferedReader;
        Long l = 0L;

        bufferedReader = new BufferedReader(new FileReader("F:\\Desktop\\sprcificabnormality\\abnormalClassification\\无进项发票"));
        if (bufferedReader.readLine() != null) {
            l ++;
            System.out.println(l);
        }
        fos = new FileOutputStream("F:\\Desktop\\sprcificabnormality\\abnormalClassification\\无进项发票", true);
        String s = "该类异常数量共计: " + l.toString();
        fos.write(s.getBytes());

        fos.close();
        bufferedReader.close();*/

        LineNumberReader  lnr = new LineNumberReader(new FileReader(new File("F:\\Desktop\\sprcificabnormality\\abnormalClassification\\无进项发票")));
        lnr.skip(Long.MAX_VALUE);
        System.out.println(lnr.getLineNumber() + 1);
        lnr.close();
    }
}
