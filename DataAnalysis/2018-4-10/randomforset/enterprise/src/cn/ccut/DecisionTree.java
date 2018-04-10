package cn.ccut;

import java.util.ArrayList;

public class DecisionTree {
    // 树的根节点
    TreeNode rootNode;
    // 数据的属性列名称
    String[] featureNames;
    // 这棵树所包含的数据
    ArrayList<String[]> datas;
    // 决策树构造的的工具类
    CARTTool tool;

    public DecisionTree(ArrayList<String[]> datas) {
        this.datas = datas;
        this.featureNames = datas.get(0);

        /**
         * 测试
         */
        /*for(String[] s :datas) {
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < s.length; i++){
                buffer.append(s[i]);
                buffer.append(" ");
            }
            System.out.println(buffer.toString());
        }
        StringBuffer buffer = new StringBuffer();
        for(String s :featureNames) {
            buffer.append(s);
            buffer.append(" ");
        }
        System.out.println(buffer.toString());*/



        tool = new CARTTool(datas);
        // 通过CART工具类进行决策树的构建，并返回树的根节点
        rootNode = tool.startBuildingTree();
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public String[] getFeatureNames() {
        return featureNames;
    }

    public void setFeatureNames(String[] featureNames) {
        this.featureNames = featureNames;
    }

    public ArrayList<String[]> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<String[]> datas) {
        this.datas = datas;
    }

    public CARTTool getTool() {
        return tool;
    }

    public void setTool(CARTTool tool) {
        this.tool = tool;
    }
}
