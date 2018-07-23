package cn.ccut;

import cn.ccut.common.CrossValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MapReduce程序,属性提取主类
 */
public class AttributeExtraction {
    private static final Logger log = LoggerFactory.getLogger(AttributeExtraction.class);

    public static void main(String[] args) throws Exception {
        // 提取属性
//        AttributeMain.main(args);

        // 交叉验证，分出训练集和测试集
        CrossValidationUtils.upDateTrainAndTest(50);

        // 上传HDFS文件系统
        CrossValidationUtils.uploadFileToHDFS();

        // 测试
//        Step123.main(new String[]{});
    }
}
