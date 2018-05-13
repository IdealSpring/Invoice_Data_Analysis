package cn.demo;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 字节流读取文件
 *
 * @authorMr.Robot
 * @create2018-05-10 19:35
 */
public class FIleRead03 {
    private static final String ABNORMAL_6 = "[异常6]";
    private static final String ABNORMAL_5 = "[异常5]";
    private static final String ABNORMAL_4 = "[异常4]";
    private static final String ABNORMAL_3 = "[异常3]";
    private static final String ABNORMAL_2 = "[异常2]";
    private static final String ABNORMAL_1 = "[异常1]";
    private static final String ABNORMAL = "[异常]";
    private static final String CLASSIFICATION_PATH = "F:\\Desktop\\sprcificabnormality\\abnormalClassification\\";

    public static void main(String[] args) throws IOException {

/*        // 字节流拷贝
        FileInputStream in = new FileInputStream(CLASSIFICATION_PATH + ABNORMAL_5);
        FileOutputStream out = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL);
        byte[] buf = new byte[10240];
        int len;

        // 写入文件流
        FileOutputStream fos = new FileOutputStream(CLASSIFICATION_PATH + ABNORMAL, true);

        while ((len = in.read(buf)) != -1) {
            //out.write(buf, 0, len);
            fos.write(buf);
        }
        in.close();
        out.close();*/

        /*try {
            FileReader fr1=new FileReader(CLASSIFICATION_PATH + ABNORMAL_5);//读取newFile.txt的内容
            FileReader fr2=new FileReader(CLASSIFICATION_PATH + ABNORMAL_4);//读取newFile1.txt的内容
            BufferedReader br1=new BufferedReader(fr1);
            BufferedReader br2=new BufferedReader(fr2);
            BufferedWriter bw3=new BufferedWriter(new FileWriter(CLASSIFICATION_PATH + ABNORMAL));
            String s,s1;
            s=br1.readLine();
            s1=br2.readLine();
            while(s!=null)
            {
                bw3.write(s);//写入targetFile000.txt文件
                s=br1.readLine();
                bw3.newLine();//换行
            }
            while(s1!=null)
            {
                bw3.write(s1);
                s1=br1.readLine();
                bw3.newLine();
            }
            br1.close();
            br2.close();
            bw3.close();
            System.out.println("文件合并成功...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/


        String[] iF = new String[] {CLASSIFICATION_PATH + ABNORMAL_6, CLASSIFICATION_PATH + ABNORMAL_4, CLASSIFICATION_PATH + ABNORMAL_3};
        String oF = CLASSIFICATION_PATH + ABNORMAL;

        FileOutputStream output = new FileOutputStream(new File(oF));
        WritableByteChannel targetChannel = output.getChannel();

        for(int i =0; i<iF.length; i++){
            FileInputStream input = new FileInputStream(iF[i]);
            FileChannel inputChannel = input.getChannel();

            inputChannel.transferTo(0, inputChannel.size(), targetChannel);

            inputChannel.close();
            input.close();
        }
        targetChannel.close();
        output.close();
        System.out.println("All jobs done...");
    }
}
