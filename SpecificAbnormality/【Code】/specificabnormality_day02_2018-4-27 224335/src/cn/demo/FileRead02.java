package cn.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @authorMr.Robot
 * @create2018-04-26 18:46
 */
public class FileRead02 {
    public static void main(String[] args) {
        FileReader reader = null;
        try {
            reader = new FileReader("F:\\Desktop\\abnormalEnterprise");
            BufferedReader br = new BufferedReader(reader);

            String s = null;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                String s1 = s.substring(s.indexOf(',') + 1, s.length());
                System.out.println(s1);
                System.out.println(s1.length());
                if (s1.equals("1")) {
                    System.out.println("yyyyyyyyyyyyyyyyyy");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
