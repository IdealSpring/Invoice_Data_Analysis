package cn.ccut;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;

import org.apache.hadoop.fs.Path;
/**
 * hdfs常用操作工具类
 * @author jianting.zhao
 *
 */
public class HdfsUtil {
    public static boolean rename(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("error params!");
        }
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path src = new Path(args[0]); // 旧的文件名
        Path dst = new Path(args[1]); // 新的文件名
        boolean isRename = hdfs.rename(src, dst);
        // hdfs.close();
        return isRename;
        // String result=isRename?"成功":"失败";
        // System.out.println("文件重命名结果为："+result);
    }

    public static boolean copyfile(String[] args) {
        if (args.length != 2) {
            System.out.println("error params!");
        }
        Configuration conf = new Configuration();
        FileSystem hdfs;
        try {
            hdfs = FileSystem.get(conf);

            // 本地文件
            Path src = new Path(args[0]);
            // HDFS为止
            Path dst = new Path(args[1]);
            hdfs.copyFromLocalFile(src, dst);
//      System.out.println("Upload to" + conf.get("fs.default.name"));
//      FileStatus files[] = hdfs.listStatus(dst);
//      for (FileStatus file : files) {
//          System.out.println(file.getPath());
//      }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deletefile(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("error params!");
        }
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        boolean isDeleted= true;
        for (int i = 0; i < args.length; i++) {
            Path delef = new Path(args[i]);
            if(!hdfs.delete(delef, true)){
                isDeleted= false;
            }
        }
        // hdfs.close();
        // boolean isDeleted=hdfs.delete(delef,false);
        // 递归删除
        return isDeleted;
    }
    public static boolean mkdir(String[] args) throws Exception{
        if (args.length == 0) {
            System.out.println("error params!");
        }
        Configuration conf=new Configuration();
        FileSystem hdfs=FileSystem.get(conf);
        boolean isCreated= true;
        for (int i = 0; i < args.length; i++) {
            Path cteatef = new Path(args[i]);
            if(!hdfs.mkdirs(cteatef)){
                isCreated= false;
            }
        }
        // hdfs.close();
        // boolean isDeleted=hdfs.delete(delef,false);
        // 递归删除
        return isCreated;
    }
    public static boolean filedirExist(String[] args) throws Exception{
        if (args.length == 0) {
            System.out.println("error params!");
        }
        Configuration conf=new Configuration();
        FileSystem hdfs=FileSystem.get(conf);
        boolean isCreated= true;
        for (int i = 0; i < args.length; i++) {
            Path cteatef = new Path(args[i]);
            if(!hdfs.exists(cteatef)){
                isCreated= false;
            }
        }
        // hdfs.close();
        // boolean isDeleted=hdfs.delete(delef,false);
        // 递归删除
        return isCreated;
    }
    public static boolean filedirStart(String[] args) throws Exception{
        if (args.length == 0) {
            System.out.println("error params!");
        }
        Configuration conf=new Configuration();
        FileSystem hdfs=FileSystem.get(conf);
        boolean isCreated= false;
        Path cteatef = new Path(args[0]);
        FileStatus stats[]=hdfs.listStatus(cteatef);
        int dirLen = args[0].length();
        if(!args[0].endsWith("\\") && !args[0].endsWith("/"))
        {
            dirLen = dirLen + 1;
        }
//        String input = args[0].replace("\\", "/");
        String input = args[0];
        for (int i = 0; i < stats.length; i++) {
//          String dir = stats[i].getPath().toString().replace("\\", "/");
            String dir = stats[i].getPath().toString();
            int fileindex = dir.indexOf(input) + dirLen;
            if(dir.substring(fileindex).startsWith(args[1]))
                isCreated= true;
        }
        // boolean isDeleted=hdfs.delete(delef,false);
        // 递归删除
        // hdfs.close();
        return isCreated;
    }

    public static boolean copyfileOnHdfs(String[] args) {
        if (args.length != 2) {
            System.out.println("error params!");
        }
        Configuration conf = new Configuration();
        FileSystem hdfs;
        try {
            hdfs = FileSystem.get(conf);

            // 本地文件
            Path src = new Path(args[0]);
            // HDFS为止
            Path dst = new Path(args[1]);
            if(filedirExist(new String[] {args[0]})){
                hdfs.copyFromLocalFile(src, dst);
            }

//      System.out.println("Upload to" + conf.get("fs.default.name"));
//      FileStatus files[] = hdfs.listStatus(dst);
//      for (FileStatus file : files) {
//          System.out.println(file.getPath());
//      }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
