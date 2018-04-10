package cn.ccut;

import java.util.*;

public class CARTTool {
    // 类标号的值类型
    private final String YES = "Yes";
    private final String NO = "No";
    //根节点标识计数器
    private int countRootNode = 0;

    // 初始源数据，用一个二维字符数组存放模仿表格数据
    private String[][] data;
    // 数据的属性行的名字
    private String[] attrNames;
    // 所有属性的类型总数,在这里就是data源数据的列数
    private int attrNum;
    // 每个属性的值所有类型
    private HashMap<String, ArrayList<String>> attrValue;

    public CARTTool(ArrayList<String[]> dataArray) {
        this.data = new String[dataArray.size()][];
        dataArray.toArray(data);
        this.attrNames = data[0];
        this.attrNum = data[0].length;

        this.attrValue = new HashMap<>();
        initAttrValue();
    }

    /**
     * 首先初始化每种属性的值的所有类型，用于后面的子类熵的计算时用
     */
    private void initAttrValue() {
        ArrayList<String> tempValues;

        // 按照列的方式，从左往右找
        for (int j = 1; j < attrNum; j++) {
            // 从一列中的上往下开始寻找值
            tempValues = new ArrayList<>();
            for (int i = 1; i < data.length; i++) {
                if (!tempValues.contains(data[i][j])) {
                    // 如果这个属性的值没有添加过，则添加
                    tempValues.add(data[i][j]);
                }
            }

            // 一列属性的值已经遍历完毕，复制到map属性表中
            attrValue.put(data[0][j], tempValues);
            System.out.println(data[0][j] + "---" + tempValues);
        }

        /**
         * 测试
         */
        for (int i = 0; i < data.length; i++) {
            StringBuffer buffer = new StringBuffer();
            for(int j = 0; j < data[i].length; j++) {
                buffer.append(data[i][j]);
                buffer.append(" ");
            }
            System.out.println(buffer.toString());
        }
        StringBuffer buffer = new StringBuffer();
        for(String s : attrNames) {
            buffer.append(s);
            buffer.append(" ");
        }
        System.out.println(buffer.toString());
        System.out.println("attrNum:" + attrNum);



    }

    /**
     * 构造分类回归树，并返回根节点
     * @return
     */
    public TreeNode startBuildingTree() {
        ArrayList<String> remainAttr = new ArrayList<>();
        // 添加属性，除了最后一个类标号属性
        for (int i = 1; i < attrNames.length - 1; i++) {
            remainAttr.add(attrNames[i]);
        }

        TreeNode rootNode = new TreeNode();
        buildDecisionTree(rootNode, "", data, remainAttr, false);
        setIndex(rootNode, 0);
        showDecisionTree(rootNode, 1);

        return rootNode;
    }

    /**
     * 显示决策树
     *
     * @param node
     *            待显示的节点
     * @param blankNum
     *            行空格符，用于显示树型结构
     */
    private void showDecisionTree(TreeNode node, int blankNum) {
        System.out.println();
        for (int i = 0; i < blankNum; i++) {
            System.out.print("    ");
        }

        System.out.print("--");
        // 显示分类的属性值
        if (node.getParentAttrValue() != null
                && node.getParentAttrValue().length() > 0) {
            System.out.print(node.getParentAttrValue());
        } else {
            System.out.print("--");
        }
        System.out.print("--");

        if (node.getDataIndex() != null && node.getDataIndex().size() > 0) {
            String i = node.getDataIndex().get(0);
            System.out.print("【" + node.getNodeIndex() + "】类别:"
                    + data[Integer.parseInt(i)][attrNames.length - 1]);
            System.out.print("[");
            for (String index : node.getDataIndex()) {
                System.out.print(index + ", ");
            }
            System.out.println("]");
        } else {
            // 递归显示子节点
            System.out.print("【" + node.getNodeIndex() + ":"
                    + node.getAttrName() + "】");
            if (node.getChildAttrNode() != null && node.getChildAttrNode().length > 0) {
                for (TreeNode childNode : node.getChildAttrNode()) {
                    showDecisionTree(childNode, 2 * blankNum);
                }
            } else {
                System.out.print("【  Child Null】");
            }
        }
    }

    /**
     * 为节点设置序列号，并计算每个节点的误差率，用于后面剪枝
     *
     * @param node
     *            开始的时候传入的是根节点
     * @param index
     *            开始的索引号，从1开始
     */
    private void setIndex(TreeNode node, int index) {
        TreeNode tempNode;
        Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();

        nodeQueue.add(node);
        while (nodeQueue.size() > 0) {
            index++;
            // 从队列头部获取首个节点
            tempNode = nodeQueue.poll();
            tempNode.setNodeIndex(index);
            if (tempNode.getChildAttrNode() != null) {
                for (TreeNode childNode : tempNode.getChildAttrNode()) {
                    nodeQueue.add(childNode);
                }
            }
        }
    }

    /**
     * 属性划分完毕，进行数据的移除
     *
     * @param srcData
     *            源数据
     * @param attrName
     *            划分的属性名称
     * @param valueType
     *            属性的值类型
     * @parame beLongValue 分类是否属于此值类型
     */
    private String[][] removeData(String[][] srcData, String attrName,
                                  String valueType, boolean beLongValue) {

        String[][] desDataArray;
        ArrayList<String[]> desData = new ArrayList<>();
        // 待删除数据
        ArrayList<String[]> selectData = new ArrayList<>();
        selectData.add(attrNames);

        // 数组数据转化到列表中，方便移除
        for (int i = 0; i < srcData.length; i++) {
            desData.add(srcData[i]);
        }

        // 还是从左往右一列列的查找
        for (int j = 1; j < attrNames.length; j++) {
            if (attrNames[j].equals(attrName)) {
                for (int i = 1; i < desData.size(); i++) {
                    if (desData.get(i)[j].equals(valueType)) {
                        // 如果匹配这个数据，则移除其他的数据
                        selectData.add(desData.get(i));
                    }
                }
            }
        }

        if (beLongValue) {
            desDataArray = new String[selectData.size()][];
            selectData.toArray(desDataArray);
        } else {
            // 属性名称行不移除
            selectData.remove(attrNames);
            // 如果是划分不属于此类型的数据时，进行移除
            desData.removeAll(selectData);
            desDataArray = new String[desData.size()][];
            desData.toArray(desDataArray);
        }

        return desDataArray;
    }

    /**
     * 建树
     * @param node
     * @param parentAttrValue
     * @param remainData
     * @param remainAttr
     * @param beLongParentValue
     */
    public void buildDecisionTree(TreeNode node, String parentAttrValue,
                                  String[][] remainData, ArrayList<String> remainAttr,
                                  boolean beLongParentValue) {
        // 划分属性名称
        String spiltAttrName = "";
        // 属性划分值
        String valueType = "";
        //最小基尼指数
        double minGini = 100.0;
        double tempGini = 0;
        // 基尼指数数组，保存了基尼指数和此基尼指数的划分属性值
        //[划分属性名称, 基尼指数]
        String[] giniArray;

        if(countRootNode == 0) {
            node.setParentAttrValue("root");
            countRootNode ++;
        } else {
            if (beLongParentValue) {
                node.setParentAttrValue(parentAttrValue);
            } else {
                node.setParentAttrValue("!" + parentAttrValue);
            }
        }

        //递归调用的结束条件
        if (remainAttr.size() == 0) {
            if (remainData.length > 1) {
                ArrayList<String> indexArray = new ArrayList<>();
                for (int i = 1; i < remainData.length; i++) {
                    indexArray.add(remainData[i][0]);
                }
                node.setDataIndex(indexArray);
            }

            //	System.out.println("attr remain null");
            return;
        }

        for (String attrName : remainAttr) {
            System.out.println("计算" + attrName + "的基尼指数：");
            giniArray = computeAttrGini(remainData, attrName);
            tempGini = Double.parseDouble(giniArray[1]);
            System.out.println("giniArray[0]:" + giniArray[0] + ",giniArray[1]:" + giniArray[1]);

            if (tempGini < minGini) {
                spiltAttrName = attrName;
                valueType = giniArray[0];
                minGini = tempGini;
                System.out.println("spiltAttrName:" + spiltAttrName + ",valueType:" + valueType + ",minGini:" + minGini);
            }
        }

        // 移除划分属性
        System.out.println("移除属性：" + spiltAttrName);
        remainAttr.remove(spiltAttrName);
        node.setAttrName(spiltAttrName);

        // 孩子节点,分类回归树中，每次二元划分，分出2个孩子节点
        TreeNode[] childNode = new TreeNode[2];
        String[][] rData;

        boolean[] bArray = new boolean[] { true, false };
        for (int i = 0; i < bArray.length; i++) {
            // 二元划分属于属性值的划分
            rData = removeData(remainData, spiltAttrName, valueType, bArray[i]);
            remainAttr = setAttrAndCheck(rData, remainAttr);

            boolean sameClass = true;
            ArrayList<String> indexArray = new ArrayList<>();
            for (int k = 1; k < rData.length; k++) {
                indexArray.add(rData[k][0]);
                // 判断是否为同一类的
                if (!rData[k][attrNames.length - 1]
                        .equals(rData[1][attrNames.length - 1])) {
                    // 只要有1个不相等，就不是同类型的
                    sameClass = false;
                    break;
                }
            }

            childNode[i] = new TreeNode();
            if (!sameClass) {
                // 创建新的对象属性，对象的同个引用会出错
                ArrayList<String> rAttr = new ArrayList<>();
                for (String str : remainAttr) {
                    rAttr.add(str);
                }
                buildDecisionTree(childNode[i], valueType, rData, rAttr,
                        bArray[i]);
            } else {
                String pAtr = (bArray[i] ? valueType : "!" + valueType);
                childNode[i].setParentAttrValue(pAtr);
                childNode[i].setDataIndex(indexArray);
            }
        }

        node.setChildAttrNode(childNode);
    }

    private ArrayList<String> setAttrAndCheck(String[][] rData, ArrayList<String> remainAttr) {
        HashMap<String, ArrayList<String>> checkAttr = new HashMap<>();
        ArrayList<String> checkRemainAttr = new ArrayList<>();

        ArrayList<String> tempValues;
        String[] tempAttrName = rData[0];

        for(String attrName : remainAttr) {
            tempValues = new ArrayList<>();
            for(int j = 1; j < rData.length; j++) {
                if(tempAttrName[j].equals(attrName)) {
                    // 从一列中的上往下开始寻找值
                    for (int i = 1; i < rData.length; i++) {
                        if (!tempValues.contains(rData[i][j])) {
                            // 如果这个属性的值没有添加过，则添加
                            tempValues.add(rData[i][j]);
                        }
                    }
                }
            }

            checkAttr.put(attrName, tempValues);
        }

        Set<String> set = checkAttr.keySet();
        for(String s : set) {
            ArrayList<String> list = checkAttr.get(s);
            if(list.size() == 1) {
                checkRemainAttr.remove(s);
            }
        }

        return checkRemainAttr;
    }

    /**
     * 计算属性划分的最小基尼指数，返回最小的属性值划分和最小的基尼指数，保存在一个数组中
     *
     * @param remainData
     *            剩余谁
     * @param attrName
     *            属性名称
     * @return
     */
    private String[] computeAttrGini(String[][] remainData, String attrName) {
        //private Map<String, Double> computeAttrGini(String[][] remainData, String attrName) {
        /**
         * 测试
         */
        for(int i = 0; i < remainData.length; i++){
            StringBuffer buffer = new StringBuffer();
            for(int j = 0; j <remainData[i].length; j++) {
                buffer.append(remainData[i][j]);
                buffer.append(" ");
            }
            System.out.println(buffer.toString());
        }

        String[] str = new String[2];
        //Map<String, Double> str = new HashMap<>();
        // 最终该属性的划分类型值
        String valueType = "";
        // 临时变量
        int tempNum = 0;
        // 保存属性的值划分时的最小的基尼指数
        double minGini = 100.0;
        ArrayList<String> valueTypes = attrValue.get(attrName);
        // 属于此属性值的实例数
        HashMap<String, Integer> belongNum = new HashMap<>();

        for (String string : valueTypes) {
            // 重新计数的时候，数字归0
            tempNum = 0;
            // 按列从左往右遍历属性
            for (int j = 1; j < attrNames.length; j++) {
                // 找到了指定的属性
                if (attrName.equals(attrNames[j])) {
                    for (int i = 1; i < remainData.length; i++) {
                        // 统计正负实例按照属于和不属于值类型进行划分
                        if (string.equals(remainData[i][j])) {
                            tempNum++;
                        }
                    }
                }
            }

            belongNum.put(string, tempNum);
        }


        double tempGini = 0;
        double posProbably = 1.0;
        double negProbably = 1.0;
        for (String string : valueTypes) {
            tempGini = 0;

            posProbably = 1.0 * belongNum.get(string) / (remainData.length - 1);
            negProbably = 1 - posProbably;

            tempGini += posProbably
                    * computeGini(remainData, attrName, string, true);
            tempGini += negProbably
                    * computeGini(remainData, attrName, string, false);

            if (tempGini < minGini) {
                valueType = string;
                minGini = tempGini;
            }
        }

        str[0] = valueType;
        str[1] = minGini + "";
        //str.put(valueType, minGini);

        return str;
    }

    /**
     * 计算机基尼指数
     *
     * @param remainData
     *            剩余数据
     * @param attrName
     *            属性名称
     * @param value
     *            属性值
     * @param beLongValue
     *            分类是否属于此属性值
     * @return
     */
    private double computeGini(String[][] remainData, String attrName,
                               String value, boolean beLongValue) {
        // 实例总数
        int total = 0;
        // 正实例数
        int posNum = 0;
        // 负实例数
        int negNum = 0;
        // 基尼指数
        double gini = 0;

        // 还是按列从左往右遍历属性
        for (int j = 1; j < attrNames.length; j++) {
            // 找到了指定的属性
            if (attrName.equals(attrNames[j])) {
                for (int i = 1; i < remainData.length; i++) {
                    // 统计正负实例按照属于和不属于值类型进行划分
                    if ((beLongValue && remainData[i][j].equals(value))
                            || (!beLongValue && !remainData[i][j].equals(value))) {
                        if (remainData[i][attrNames.length - 1].equals(YES)) {
                            // 判断此行数据是否为正实例
                            posNum++;
                        } else {
                            negNum++;
                        }
                    }
                }
            }
        }

        total = posNum + negNum;
        double posProbobly = (double) posNum / total;
        double negProbobly = (double) negNum / total;
        gini = 1 - posProbobly * posProbobly - negProbobly * negProbobly;

        // 返回计算基尼指数
        return gini;
    }

    public String getYES() {
        return YES;
    }

    public String getNO() {
        return NO;
    }

    public int getCountRootNode() {
        return countRootNode;
    }

    public void setCountRootNode(int countRootNode) {
        this.countRootNode = countRootNode;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public String[] getAttrNames() {
        return attrNames;
    }

    public void setAttrNames(String[] attrNames) {
        this.attrNames = attrNames;
    }

    public int getAttrNum() {
        return attrNum;
    }

    public void setAttrNum(int attrNum) {
        this.attrNum = attrNum;
    }

    public HashMap<String, ArrayList<String>> getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(HashMap<String, ArrayList<String>> attrValue) {
        this.attrValue = attrValue;
    }
}
