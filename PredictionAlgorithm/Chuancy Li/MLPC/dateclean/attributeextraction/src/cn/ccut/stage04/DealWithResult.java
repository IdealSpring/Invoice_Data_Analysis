package cn.ccut.stage04;

import cn.ccut.common.FilePathCollections;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @authorMr.Robot
 * @create2018-05-26 19:19
 */
public class DealWithResult {
    public static String changeResult(String s) {
        String[] s1 = s.split(",");
        int j = s1.length;

        String nsr_id = s1[0];
        saveId(nsr_id);
        String lable = s1[s1.length - 1];


        String ss = "";
        for (int i = 1; i <= j - 2; i ++) {
            ss += " " + i + ":" + s1[i];
        }

        String sss = lable + " " + ss;
        return sss;
    }

    private static void saveId(String id) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FilePathCollections.IdOutputPath + "id", true);
            fos.write((id + "\r\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
