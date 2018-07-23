/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.classifier.df;

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.DataUtils;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;
import org.apache.mahout.classifier.df.node.CategoricalNode;
import org.apache.mahout.classifier.df.node.Leaf;
import org.apache.mahout.classifier.df.node.Node;
import org.apache.mahout.classifier.df.node.NumericalNode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a forest of decision trees.
 */
public class DecisionForest implements Writable {

    private final List<Node> trees;
    private int flog = 1;
    private int index = 0;

    private DecisionForest() {
        trees = new ArrayList<>();
    }

    public DecisionForest(List<Node> trees) {
        Preconditions.checkArgument(trees != null && !trees.isEmpty(), "trees argument must not be null or empty");

        this.trees = trees;
    }

    public List<Node> getTrees() {
        return trees;
    }

    /**
     * Classifies the data and calls callback for each classification
     */
    public void classify(Data data, double[][] predictions) {
        Preconditions.checkArgument(data.size() == predictions.length, "predictions.length must be equal to data.size()");

        if (data.isEmpty()) {
            return; // nothing to classify
        }

        int treeId = 0;
        for (Node tree : trees) {
            for (int index = 0; index < data.size(); index++) {
                if (predictions[index] == null) {
                    predictions[index] = new double[trees.size()];
                }
                predictions[index][treeId] = tree.classify(data.get(index));
            }
            treeId++;
        }
    }

    /**
     * predicts the label for the instance
     *
     * @param rng
     *          Random number generator, used to break ties randomly
     * @return NaN if the label cannot be predicted
     */
    public double classify(Dataset dataset, Random rng, Instance instance) {
        if (dataset.isNumerical(dataset.getLabelId())) {
            double sum = 0;
            int cnt = 0;
            for (Node tree : trees) {
                double prediction = tree.classify(instance);
                if (!Double.isNaN(prediction)) {
                    sum += prediction;
                    cnt++;
                }
            }

            if (cnt > 0) {
                return sum / cnt;
            } else {
                return Double.NaN;
            }
        } else {
            int[] predictions = new int[dataset.nblabels()];

            for (Node tree : trees) {
                double prediction = tree.classify(instance);

                if (!Double.isNaN(prediction)) {
                    predictions[(int) prediction]++;
                }
            }

            if (DataUtils.sum(predictions) == 0) {
                return Double.NaN; // no prediction available
            }

            return DataUtils.maxindex(rng, predictions);
        }
    }

    public void showForest() {
        int count = 1;

        for(Node tree : this.trees) {
            System.out.println("第" + count + "棵树，最大深度：" + tree.maxDepth() + ", 节点数量：" + tree.nbNodes());
            count ++;

            showDecisionTree(tree, 1);

            ArrayList<String> list = new ArrayList<>();

            list.add("var tree_structure = {\n" +
                    "    chart: {\n" +
                    "        container: \"#OrganiseChart6\",\n" +
                    "        levelSeparation:    20,\n" +
                    "        siblingSeparation:  15,\n" +
                    "        subTeeSeparation:   15,\n" +
                    "        rootOrientation: \"EAST\",\n" +
                    "\n" +
                    "        node: {\n" +
                    "            HTMLclass: \"tennis-draw\",\n" +
                    "            drawLineThrough: true\n" +
                    "        },\n" +
                    "        connectors: {\n" +
                    "            type: \"straight\",\n" +
                    "            style: {\n" +
                    "                \"stroke-width\": 2,\n" +
                    "                \"stroke\": \"#ccc\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    },");

            //printFileToJavaScript(tree, list, 0);

            list.add("};");

            /*try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:/Users/zhipeng-Tong/Desktop/treant-js-master/examples/tennis-draw/example7.js"));
                for(String line : list) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void printFileToJavaScript(Node node, ArrayList<String> list, int length) {
        if(node.getType() == Node.Type.CATEGORICAL) {
            CategoricalNode categoricalNode = (CategoricalNode) node;

            if(flog == 1) {
                list.add("nodeStructure: {" + "text: {" + "name: {val: \"" + categoricalNode.getAttr() + "\"}},HTMLclass: \"winner\"");

                flog ++;
                index ++;

                list.add("}");
            } else {
                if(length != 0) {
                    list.add(++index, ",");
                }

                list.add(++index,"{text: {" + "name: \"属性名:" + categoricalNode.getAttr() + "\"," + "desc: \"判别值:" + length + "\"}");


            }

            for(int i = 0; i < categoricalNode.getChilds().length; i++) {
                if(i == 0) {
                    list.add(++index, ",children: [");

                    list.add(++index, "]");
                }
                printFileToJavaScript(categoricalNode.getChilds()[i], list, i);
            }

        } else if(node.getType() == Node.Type.NUMERICAL) {
            /*NumericalNode numericalNode = (NumericalNode) node;

            if(flog == 1) {
                list.add("nodeStructure: {\n" +
                        "        text: {\n" +
                        "            name: {val: \"");
                list.add(numericalNode.getAttr() + "\"}\n" +
                        "        },\n" +
                        "        HTMLclass: \"winner\",");

                flog ++;
            }

            System.out.print("【属性名:" + numericalNode.getAttr() + "】");


            if(numericalNode.getLoChild() != null) {
                System.out.println();
                for (int j = 0; j < blankNum + 3; j++) {
                    System.out.print("    ");
                }
                System.out.print("--");
                System.out.print("判别值:小于" + numericalNode.getSplit());
                System.out.print("--");
                showDecisionTree(numericalNode.getLoChild(), blankNum + 4);
            }

            if(numericalNode.getHiChild() != null) {
                System.out.println();
                for (int j = 0; j < blankNum + 3; j++) {
                    System.out.print("    ");
                }
                System.out.print("--");
                System.out.print("判别值:大于" + numericalNode.getSplit());
                System.out.print("--");
                showDecisionTree(numericalNode.getHiChild(), blankNum + 4);
            }*/

        } else {
            Leaf leaf = (Leaf) node;

            if(length != 0) {
                list.add(++index, ",");
            }

            list.add(++index,"{text: {" + "name: \"类型:" + leaf.getLabel() + "\"," + "desc: \"判别值:" + length + "\"}}");
        }
    }


    /**
     * 控制台展示决策树
     *
     * @param node
     * @param blankNum
     */
    public void showDecisionTree(Node node, int blankNum) {
        if(node.getType() == Node.Type.CATEGORICAL) {
            CategoricalNode categoricalNode = (CategoricalNode) node;

            System.out.println();
            for (int i = 0; i < blankNum; i++) {
                System.out.print("    ");
            }

            System.out.print("【属性名:" + categoricalNode.getAttr() + "】");

            for(int i = 0; i < categoricalNode.getChilds().length; i++) {
                System.out.println();
                for (int j = 0; j < blankNum + 3; j++) {
                    System.out.print("    ");
                }
                System.out.print("--");
                System.out.print("判别值:" + i);
                System.out.print("--");


                showDecisionTree(categoricalNode.getChilds()[i], blankNum + 4);
            }

        } else if(node.getType() == Node.Type.NUMERICAL) {
            NumericalNode numericalNode = (NumericalNode) node;

            System.out.println();
            for (int i = 0; i < blankNum; i++) {
                System.out.print("    ");
            }

            System.out.print("【属性名:" + numericalNode.getAttr() + "】");


            if(numericalNode.getLoChild() != null) {
                System.out.println();
                for (int j = 0; j < blankNum + 3; j++) {
                    System.out.print("    ");
                }
                System.out.print("--");
                System.out.print("判别值:小于" + numericalNode.getSplit());
                System.out.print("--");
                showDecisionTree(numericalNode.getLoChild(), blankNum + 4);
            }

            if(numericalNode.getHiChild() != null) {
                System.out.println();
                for (int j = 0; j < blankNum + 3; j++) {
                    System.out.print("    ");
                }
                System.out.print("--");
                System.out.print("判别值:大于" + numericalNode.getSplit());
                System.out.print("--");
                showDecisionTree(numericalNode.getHiChild(), blankNum + 4);
            }

        } else {
            Leaf leaf = (Leaf) node;

            System.out.println();
            for (int i = 0; i < blankNum; i++) {
                System.out.print("    ");
            }

            System.out.print("[类型:");
            System.out.print(leaf.getLabel());
            System.out.println("]");

        }
    }

    /**
     * @return Mean number of nodes per tree
     */
    public long meanNbNodes() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.nbNodes();
        }

        return sum / trees.size();
    }

    /**
     * @return Total number of nodes in all the trees
     */
    public long nbNodes() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.nbNodes();
        }

        return sum;
    }

    /**
     * @return Mean maximum depth per tree
     */
    public long meanMaxDepth() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.maxDepth();
        }

        return sum / trees.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DecisionForest)) {
            return false;
        }

        DecisionForest rf = (DecisionForest) obj;

        return trees.size() == rf.getTrees().size() && trees.containsAll(rf.getTrees());
    }

    @Override
    public int hashCode() {
        return trees.hashCode();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(trees.size());
        for (Node tree : trees) {
            tree.write(dataOutput);
        }
    }

    /**
     * Reads the trees from the input and adds them to the existing trees
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        int size = dataInput.readInt();
        for (int i = 0; i < size; i++) {
            trees.add(Node.read(dataInput));
        }
    }

    /**
     * Read the forest from inputStream
     * @param dataInput - input forest
     * @return {@link org.apache.mahout.classifier.df.DecisionForest}
     * @throws IOException
     */
    public static DecisionForest read(DataInput dataInput) throws IOException {
        DecisionForest forest = new DecisionForest();
        forest.readFields(dataInput);
        return forest;
    }

    /**
     * Load the forest from a single file or a directory of files
     * @throws IOException
     */
    public static DecisionForest load(Configuration conf, Path forestPath) throws IOException {
        FileSystem fs = forestPath.getFileSystem(conf);
        Path[] files;
        if (fs.getFileStatus(forestPath).isDir()) {
            files = DFUtils.listOutputFiles(fs, forestPath);
        } else {
            files = new Path[]{forestPath};
        }

        DecisionForest forest = null;
        for (Path path : files) {
            try (FSDataInputStream dataInput = new FSDataInputStream(fs.open(path))) {
                if (forest == null) {
                    forest = read(dataInput);
                } else {
                    forest.readFields(dataInput);
                }
            }
        }

        return forest;

    }

}
