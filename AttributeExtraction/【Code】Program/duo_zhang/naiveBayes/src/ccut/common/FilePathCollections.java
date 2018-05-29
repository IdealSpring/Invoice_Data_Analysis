package ccut.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.HadoopUtil;

import java.io.File;
import java.net.URI;

/**
 * 文件路径工具类
 *
 *      【重要:】此文件只需改两个路径：
 *          1.basePath
 *          2.stage06HDFSTrainAndTestPath
 */
public class FilePathCollections {
    /**
     * 基本路径
     */
    public static final String basePath = "D://视频诗词/异常企业资料/";
    //public static final String basePath = "hdfs://111.116.20.110:9000/user/hadoop/异常企业资料/";

    /**
     * HDFS文件系统路径
     */
    public static final String HDFSFileSystemPath = "hdfs://111.116.20.110:9000/";
    public static FileSystem fileSystem;

    /**
     * 企业发票信息路径
     */
    //指定输入文件中nsrxx文件路径
    public static final String nsrxxFilePath = basePath + "信息4/nsrxx_V2.0.dat";
    //指定输入文件中zzsfp文件路径
    public static final String zzsfpFilePath = basePath + "信息4/zzsfp_V2.0.dat";
    //指定输入文件中hwmx文件路径
    public static final String hwmxFilePath = basePath + "信息4/hwmx_V2.0.dat";
    //指定输入文件中hydm_link_spbm文件路径
    public static final String hydm_link_spbmFilePath = basePath + "信息4/hydm_link_spbm_V2.0.dat";

    /**
     * 第一阶段mapreduce(stage01包中)文件路径
     */
    //结果输出路径
    public static final String stage01ResultOutputPath = basePath + "out";

    /**
     * 文件存在则删除
     *
     * @throws Exception
     */
    public static void clearUpresultOutputPathFile(String path) throws Exception {
        //判断win还是linux
        if(basePath.contains("hdfs://")) {
            HadoopUtil.delete(new Configuration(), new Path(path));
        } else {
            File file = new File(path);
            file.delete();
            if(file.exists()) {
                File[] files = file.listFiles();
                for(File f : files) {
                    if(f.isDirectory()) {
                        //递归删除
                        clearUpresultOutputPathFile(f.toString());
                    }
                    f.delete();
                }
                file.delete();
            }
        }
    }

}
