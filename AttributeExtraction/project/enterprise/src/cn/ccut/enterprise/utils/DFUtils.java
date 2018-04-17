package cn.ccut.enterprise.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class DFUtils {
    private DFUtils() {
    }

    /**
     * 数据属性集写入
     * @param conf
     * @param path
     * @param string
     * @throws IOException
     */
    public static void storeString(Configuration conf, Path path, String string) throws IOException {
        DataOutputStream out = path.getFileSystem(conf).create(path);
        Throwable var4 = null;

        try {
            out.write(string.getBytes(Charset.defaultCharset()));
        } catch (Throwable var13) {
            var4 = var13;
            throw var13;
        } finally {
            if (out != null) {
                if (var4 != null) {
                    try {
                        out.close();
                    } catch (Throwable var12) {
                        var4.addSuppressed(var12);
                    }
                } else {
                    out.close();
                }
            }

        }

    }
}
