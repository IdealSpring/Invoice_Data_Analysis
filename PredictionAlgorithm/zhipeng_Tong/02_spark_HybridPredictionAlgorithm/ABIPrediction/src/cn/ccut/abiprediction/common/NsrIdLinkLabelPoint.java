package cn.ccut.abiprediction.common;

import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * 自定义数据类型
 */
public class NsrIdLinkLabelPoint {
    private String nsrId;
    private LabeledPoint labeledPoint;

    public NsrIdLinkLabelPoint() {
    }

    public NsrIdLinkLabelPoint(String nsrId, LabeledPoint labeledPoint) {
        this.nsrId = nsrId;
        this.labeledPoint = labeledPoint;
    }

    @Override
    public String toString() {
        return nsrId + "," + labeledPoint.label();
    }

    public String getNsrId() {
        return nsrId;
    }

    public void setNsrId(String nsrId) {
        this.nsrId = nsrId;
    }

    public LabeledPoint getLabeledPoint() {
        return labeledPoint;
    }

    public void setLabeledPoint(LabeledPoint labeledPoint) {
        this.labeledPoint = labeledPoint;
    }
}
