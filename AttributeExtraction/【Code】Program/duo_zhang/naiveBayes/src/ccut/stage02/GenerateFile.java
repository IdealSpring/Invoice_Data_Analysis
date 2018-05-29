package ccut.stage02;

import java.io.*;

/**
 * Description : 文件信息整合
 * Created by 10851 on 2018/5/25.
 */
public class GenerateFile {
    public static void main(String args[]) {

        String str = "D:\\视频诗词\\异常企业资料\\bayes\\resOut\\result.txt";
        File writename = new File(str);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(writename, true));
            for (int i = 0; i < 10000; i++) {
                String pathnameNsr = "D://视频诗词/异常企业资料/bayes/out6/nsr-" + i;
                File filenameNsr = new File(pathnameNsr);
                if (!filenameNsr.exists())
                    break;
                InputStreamReader readerNsr = new InputStreamReader(new FileInputStream(filenameNsr));
                BufferedReader brNsr = new BufferedReader(readerNsr);

                String pathnameLable = "D://视频诗词/异常企业资料/bayes/out7/lable-" + i;
                File filenameLable = new File(pathnameLable);
                InputStreamReader readerLable = new InputStreamReader(new FileInputStream(filenameLable));
                BufferedReader brLable = new BufferedReader(readerLable);

                String pathnameRes = "D://视频诗词/异常企业资料/bayes/out5/res-" + i;
                File filenameRes = new File(pathnameRes);
                InputStreamReader readerRes = new InputStreamReader(new FileInputStream(filenameRes));
                BufferedReader brRes = new BufferedReader(readerRes);

                int ch = -1;

                while ((ch = brNsr.read()) != -1) {
                    out.write((char) ch);
                }
                out.write("\t:");
                while ((ch = brLable.read()) != -1) {
                    out.write((char) ch);
                }
                out.write("\t");
                while ((ch = brRes.read()) != -1) {
                    out.write((char) ch);
                }
                out.write("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
