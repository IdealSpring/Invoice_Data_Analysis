package cn.ccut;

import java.io.File;

/**
 * 清空文件
 *
 * @authorMr.Robot
 * @create2018-05-28 16:03
 */
public class CleanFiles {
    private static final String PATH1 = MultilayerPerceptronClassifierCus.DATA_PATH + "tempOut";
    private static final String PATH2 = MultilayerPerceptronClassifierCus.DATA_PATH + "result";

    public static void clean() {
        delAllFile(PATH1);
        delAllFile(PATH2);
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path
     * @return
     */
    private static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath    文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
