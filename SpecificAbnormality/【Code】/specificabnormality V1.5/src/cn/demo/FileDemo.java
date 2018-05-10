package cn.demo;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @authorMr.Robot
 * @create2018-05-10 18:14
 */
public class FileDemo {
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalClassification\\";

    public static void main(String[] args) {
        File f = new File(CLASSIFICATION_PATH);

/*        File file[] = f.listFiles();

        for (int i = 0; i < file.length; i++) {
            File fs = file[i];
            System.out.println(fs.getName());
        }

        System.out.println("----------------------");
        for (File fs : file) {
            System.out.println(fs.getName());
        }*/

        String  file[] = f.list();
        List fileList = Arrays.asList(file);
        if (fileList.contains("[异常1]")) {
            System.out.println("存在[异常1]");
        }
    }
}
