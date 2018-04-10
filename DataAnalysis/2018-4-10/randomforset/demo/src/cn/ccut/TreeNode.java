package cn.ccut;

import java.util.ArrayList;

public class TreeNode {
    // 节点属性名字
    private String attrName;
    // 节点索引标号
    private int nodeIndex;
    // 父亲分类属性值
    private String parentAttrValue;
    // 孩子节点
    private TreeNode[] childAttrNode;
    // 数据记录索引
    private ArrayList<String> dataIndex;

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public String getParentAttrValue() {
        return parentAttrValue;
    }

    public void setParentAttrValue(String parentAttrValue) {
        this.parentAttrValue = parentAttrValue;
    }

    public TreeNode[] getChildAttrNode() {
        return childAttrNode;
    }

    public void setChildAttrNode(TreeNode[] childAttrNode) {
        this.childAttrNode = childAttrNode;
    }

    public ArrayList<String> getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(ArrayList<String> dataIndex) {
        this.dataIndex = dataIndex;
    }
}
