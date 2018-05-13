package cn.ccut;

import cn.ccut.clear.CleanFiles;
import cn.ccut.handle.OutputEnterprise;
import cn.ccut.handle.StatisticsAbnormal;
import cn.ccut.mapreduce.AnalyzeMain;

import java.io.IOException;


public class SpecificAbnormality {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 清理文件
        CleanFiles.clean();

        // 分析企业存在的异常
        AnalyzeMain.analyzeMapReduce();

        // 统计各类异常的数量
        StatisticsAbnormal.statisticsAbnormalQuantity();

        // 异常排名
        OutputEnterprise.outputEnterpriseFromMoreToLess();
    }
}