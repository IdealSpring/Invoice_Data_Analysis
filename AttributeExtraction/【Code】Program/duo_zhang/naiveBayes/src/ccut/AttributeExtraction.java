package ccut;

import ccut.stage01.Stage01Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MapReduce程序,属性提取主类
 */
public class AttributeExtraction {
    private static final Logger log = LoggerFactory.getLogger(AttributeExtraction.class);

    public static void main(String[] args) throws Exception {
        //第一阶段MapReduce程序，提取前15个属性
        Stage01Main.main(args);
    }
}
