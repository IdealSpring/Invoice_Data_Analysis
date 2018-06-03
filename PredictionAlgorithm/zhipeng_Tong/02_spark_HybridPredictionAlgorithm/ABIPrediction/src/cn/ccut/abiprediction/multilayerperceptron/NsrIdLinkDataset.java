package cn.ccut.abiprediction.multilayerperceptron;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;

/**
 * 纳税人Id和数据集合类型类
 */
public class NsrIdLinkDataset {
    private ArrayList<String> nsrIdList;
    private Dataset<Row> testDataFrame;

    public ArrayList<String> getNsrIdList() {
        return nsrIdList;
    }

    public void setNsrIdList(ArrayList<String> nsrIdList) {
        this.nsrIdList = nsrIdList;
    }

    public Dataset<Row> getTestDataFrame() {
        return testDataFrame;
    }

    public void setTestDataFrame(Dataset<Row> testDataFrame) {
        this.testDataFrame = testDataFrame;
    }
}
