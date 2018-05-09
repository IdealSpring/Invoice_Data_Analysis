package cn.ccut.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.mahout.common.HadoopUtil;

import java.io.File;
import java.net.URI;

public class MDemo {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://111.116.20.110:9000/"), conf, "hadoop");

<<<<<<< HEAD
        FileStatus[] fileStatuses = HadoopUtil.listStatus(fileSystem,
                new Path("/user/hadoop/mahout"), new PathFilter() {
                    @Override
                    public boolean accept(Path path) {
                        String filePath = path.toString();
                        if(filePath.contains("-CV_train.dat")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });


        System.out.println("====================");
        for(FileStatus file : fileStatuses) {
            Path path = file.getPath();
            System.out.println(path.toString());
=======
        String outFile = "C:/Users/zhipeng-Tong/Desktop/异常企业资料/预测名单/";
        File file = new File(outFile);
        for(File s : file.listFiles()) {
            File filePath = new File(s.getPath());
            filePath.delete();
>>>>>>> d42f1edf526bd9890ff3a70092b187b7e798cbfc
        }

        fileSystem.copyToLocalFile(new Path("/user/hadoop/mahout_IdealSpring/predictions"), new Path("C:/Users/zhipeng-Tong/Desktop/异常企业资料/预测名单"));

    }
}
