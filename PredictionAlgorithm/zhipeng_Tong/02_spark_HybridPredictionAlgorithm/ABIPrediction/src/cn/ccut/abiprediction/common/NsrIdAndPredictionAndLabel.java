package cn.ccut.abiprediction.common;

/**
 * 文件格式:
 *      纳税人id,预测结果,标签
 */
public class NsrIdAndPredictionAndLabel {
    // 纳税人id
    private String nsrId;
    // 预测结果
    private double prediction;
    // 标签
    private double label;

    public NsrIdAndPredictionAndLabel() {}

    public NsrIdAndPredictionAndLabel(String nsrId, double prediction, double label) {
        this.nsrId = nsrId;
        this.prediction = prediction;
        this.label = label;
    }

    @Override
    public String toString() {
        return nsrId + "," + prediction + "," + label;
    }

    public String getNsrId() {
        return nsrId;
    }

    public void setNsrId(String nsrId) {
        this.nsrId = nsrId;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getLabel() {
        return label;
    }

    public void setLabel(double label) {
        this.label = label;
    }
}
