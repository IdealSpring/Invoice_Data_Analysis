package cn.ccut;

import java.io.*;

/**
 * 合并纳税人id和预测结果
 *
 * @authorMr.Robot
 * @create2018-05-28 15:40
 */
public class MergeIdAndPredition {

    public static void merge() throws IOException {
        final String path = MultilayerPerceptronClassifierCus.DATA_PATH;

        String oF = path + "result/result.txt";
        FileOutputStream output = new FileOutputStream(new File(oF));

        FileReader readerId = new FileReader(path  + "sourceData/id");
        BufferedReader brId = new BufferedReader(readerId);

        FileReader readerResult = new FileReader(path + "tempOut/predictionAndLabels/part-00000");
        BufferedReader brResult = new BufferedReader(readerResult);

        String s = "";
        String sid = "";
        String sre = "";
        while ((sid = brId.readLine())!= null && (sre = brResult.readLine()) != null) {
            // 标签
            String label = sre.substring(1, 2);

            // 结果
            int i = sre.indexOf(",")  + 1;
            String pre = sre.substring(i, i + 1);

            s = sid + "," + pre + "," + label;

            try {
                output.write((s + "\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            brResult.close();
            readerResult.close();
            brId.close();
            readerId.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
