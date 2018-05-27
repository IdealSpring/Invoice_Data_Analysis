package cn.ccut;

import cn.ccut.common.CrossValidationUtils;
import cn.ccut.mahout.forest.Step123;
import cn.ccut.stage01.Stage01Main;
import cn.ccut.stage02.Stage02Main;
import cn.ccut.stage03.Stage03Main;
import cn.ccut.stage04.Stage04Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MapReduce程序,属性提取主类
 */
public class AttributeExtraction {
    private static final Logger log = LoggerFactory.getLogger(AttributeExtraction.class);

    public static void main(String[] args) throws Exception {
        //第一阶段MapReduce程序，提取前15个属性
        //Stage01Main.main(args);

        //第二阶段MapReduce程序，将hwmx表连接上xf还有gf的ID
        //Stage02Main.main(args);

        //第三阶段MapReduce程序，提取出hwmx中的属性
        //Stage03Main.main(args);

        //第二阶段MapReduce程序，将第一阶段属性和第三阶段属性进行连接
        //Stage04Main.main(args);

        //交叉验证，分出训练集和测试集
        CrossValidationUtils.upDateTrainAndTest(5);

        //上传HDFS文件系统
        CrossValidationUtils.uploadFileToHDFS();

        //测试
        Step123.main(new String[]{});
    }
}
