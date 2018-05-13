package cn.ccut.clear;

import java.io.File;

/**
 * 清理文件
 *
 * @authorMr.Robot
 * @create2018-05-13 16:26
 */
public class CleanFiles {
    public CleanFiles() {
    }

    // 异常分类文件存储路径
    private static final String PATH = "F:\\Desktop\\sprcificabnormality\\abnormalResult";


    public static void clean() {
        delAllFile(PATH);
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
                flag = true;
            }
        }
        return flag;
    }
}
