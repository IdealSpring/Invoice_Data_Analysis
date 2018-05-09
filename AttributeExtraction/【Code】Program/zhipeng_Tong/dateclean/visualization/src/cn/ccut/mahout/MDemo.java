package cn.ccut.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.mahout.common.HadoopUtil;

import java.net.URI;

public class MDemo {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://111.116.20.110:9000/"), conf, "hadoop");

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
        }

    }
}
