package cn.demo;

import java.io.*;

/**
 * @authorMr.Robot
 * @create2018-04-26 17:58
 */
public class FileRead {
    public static void main(String[] args) {
        try {
            StringBuffer sb = new StringBuffer("");

            FileReader reader1 = new FileReader("F:\\Desktop\\test.dat");
            BufferedReader br1 = new BufferedReader(reader1);

            FileReader reader2 = new FileReader("F:\\Desktop\\predictions");
            BufferedReader br2 = new BufferedReader(reader2);

            String str1 = null;
            String str2 = null;
            String s = null;
            while ((str1 = br1.readLine()) != null && (str2 = br2.readLine()) != null) {
                s = str1.substring(0, str1.indexOf(',') + 1);
                s = s.concat(str2.substring(1, str2.indexOf('.') - 1));
                System.out.println(str2.substring(1, str2.indexOf('.') - 1).length());
                System.out.println(s);
                sb.append(s + "\n");
            }

            FileWriter writer = new FileWriter("F:\\Desktop\\abnormalEnterprise");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
            br2.close();
            reader2.close();
            br1.close();
            reader1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
