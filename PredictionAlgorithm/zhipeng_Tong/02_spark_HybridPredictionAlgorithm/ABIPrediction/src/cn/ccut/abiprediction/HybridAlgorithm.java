package cn.ccut.abiprediction;

import cn.ccut.abiprediction.common.CrossValidationUtils;
import cn.ccut.abiprediction.common.NsrIdAndPredictionAndLabel;
import cn.ccut.abiprediction.gbts.GradientBoostingTreeAlgorithm;
import cn.ccut.abiprediction.multilayerperceptron.MultilayerPerceptronAlgorithm;
import cn.ccut.abiprediction.naivebayes.NaiveBayesAlgorithm;
import cn.ccut.abiprediction.randomforest.RandomForestAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 进行算法的混合运算
 */
public class HybridAlgorithm {
    public static void main(String[] args) throws Exception {
        // 数据划分
        // divisionTrainAndTest();
        // 随机森林文件路径
        String RFTrainPath = "CVData/RandomForestData/0-CV_train.dat";
        String RFTestPath = "CVData/RandomForestData/0-CV_test.dat";
        // 多层感知器文件路径
        String MLPTrainPath = "CVData/MultilayerPerceptronData/0-CV_train.dat";
        String MLPTestPath = "CVData/MultilayerPerceptronData/0-CV_test.dat";
        // 贝叶斯文件路径
        String BYSTrainPath = "CVData/NaiveBayesData/0-CV_train.dat";
        String BYSTestPath = "CVData/NaiveBayesData/0-CV_test.dat";
        // 梯度增强树文件路径
        String GBTTrainPath = "CVData/GradientBoostingTreesData/0-CV_train.dat";
        String GBTTestPath = "CVData/GradientBoostingTreesData/0-CV_test.dat";

        // 运行算法
        // 0.9327296248382924 ++++++0
        // 0.9275549805950841
        // 0.9172056921086675
        // 0.9366106080206986
        // 0.9197930142302717
        List<NsrIdAndPredictionAndLabel> RFPrediction = RandomForestAlgorithm.run(RFTrainPath, RFTestPath);

        // 0.9301423027166882 ++++++0
        // 0.9197930142302717
        // 0.9210866752910737
        // 0.926261319534282
        // 0.9197930142302717
        List<NsrIdAndPredictionAndLabel> MLPPrediction = MultilayerPerceptronAlgorithm.run(MLPTrainPath, MLPTestPath);

        // 0.8939197930142303 ++++++0
        // 0.8706338939197931
        // 0.8732212160413971
        // 0.9003880983182406
        // 0.8809831824062095
        List<NsrIdAndPredictionAndLabel> BYSPrediction = NaiveBayesAlgorithm.run(BYSTrainPath, BYSTestPath);

        // 0.8965071151358344 ++++++0
        // 0.8809831824062095
        // 0.8783958602846055
        // 0.88745148771022
        // 0.8822768434670116
        List<NsrIdAndPredictionAndLabel> GBTPrediction = GradientBoostingTreeAlgorithm.run(GBTTrainPath, GBTTestPath);

        // 最终结果
        List<NsrIdAndPredictionAndLabel> retultPrediction = new ArrayList<>();
        // 填入初始数据
        for(NsrIdAndPredictionAndLabel npl : RFPrediction) {
            NsrIdAndPredictionAndLabel predictionAndLabel = new NsrIdAndPredictionAndLabel();
            predictionAndLabel.setNsrId(npl.getNsrId());
            // 设置99.99，不是有那个默认值0
            predictionAndLabel.setPrediction(99.99);
            predictionAndLabel.setLabel(npl.getLabel());
            retultPrediction.add(predictionAndLabel);
        }

        // 进行投票
        for(NsrIdAndPredictionAndLabel npl : retultPrediction) {
            // 机票器
            double countSquare = 0;
            double countOpposition = 0;
            double bysS = 10;
            double bysO = 10;
            String nsrId = npl.getNsrId();
            System.out.print("nsrId:" + nsrId);

            // 遍历累加
            // 随机森林
            for(NsrIdAndPredictionAndLabel rfPrediction : RFPrediction) {
                if(rfPrediction.getNsrId().equals(nsrId)) {
                    if(rfPrediction.getPrediction() == 0.0) {
                        countSquare ++;
                        //countSquare += 0.9327296248382924;
                    } else if(rfPrediction.getPrediction() == 1.0) {
                        countOpposition ++;
                        //countOpposition += 0.9327296248382924;
                    }
                }
            }
            // 多层感知器
            for(NsrIdAndPredictionAndLabel mlpPrediction : MLPPrediction) {
                if(mlpPrediction.getNsrId().equals(nsrId)) {
                    if(mlpPrediction.getPrediction() == 0.0) {
                        countSquare ++;
                        //countSquare += 0.9301423027166882;
                    } else if(mlpPrediction.getPrediction() == 1.0) {
                        countOpposition ++;
                        //countOpposition += 0.9301423027166882;
                    }
                }
            }
            // 贝叶斯
            for(NsrIdAndPredictionAndLabel bysPrediction : BYSPrediction) {
                if(bysPrediction.getNsrId().equals(nsrId)) {
                    if(bysPrediction.getPrediction() == 0.0) {
                        countSquare ++;
                        //countSquare += 0.8939197930142303;
                    } else if(bysPrediction.getPrediction() == 1.0) {
                        countOpposition ++;
                        //countOpposition += 0.8939197930142303;
                    }
                }
            }
            // 梯度增强树
            for(NsrIdAndPredictionAndLabel gbtPrediction : GBTPrediction) {
                if(gbtPrediction.getNsrId().equals(nsrId)) {
                    if(gbtPrediction.getPrediction() == 0.0) {
                        countSquare ++;
                        //countSquare += 0.8965071151358344;
                    } else if(gbtPrediction.getPrediction() == 1.0) {
                        countOpposition ++;
                        //countOpposition += 0.8965071151358344;
                    }
                }
            }

            System.out.print(",countSquare:" + countSquare);
            System.out.println(",countOpposition:" + countOpposition);
            // 最终判定
            if(countSquare > countOpposition) {
                npl.setPrediction(0.0);
            } else if(countSquare == countOpposition) {
                //npl.setPrediction(2.0);
                //npl.setPrediction(0);
            } else if(countSquare < countOpposition) {
                npl.setPrediction(1.0);
            }
        }

        // 结果写出
        BufferedWriter writer = new BufferedWriter(new FileWriter("predictionResult/result.txt"));
        for(NsrIdAndPredictionAndLabel npl : retultPrediction) {
            if(npl.getPrediction() != npl.getLabel()) {
                writer.write(npl.toString());
                writer.newLine();
                writer.flush();
            }

            /*writer.write(npl.toString());
            writer.newLine();
            writer.flush();*/

        }
        writer.close();

        // 计算准确率
        double sum = 0;
        for(NsrIdAndPredictionAndLabel npl : retultPrediction) {
            if(npl.getPrediction() == npl.getLabel()) {
                sum ++;
            }
        }

        System.out.println("最终准确率:" + sum/retultPrediction.size());
    }

    /**
     * 进行交叉验证数据划分
     *
     * @throws Exception
     */
    public static void divisionTrainAndTest() throws Exception {
        // 随机森林算法数据
        CrossValidationUtils.createDataSet("data/RandomForestData/RFAttributeList.txt",
                "CVData/RandomForestData");
        // 多层感知器算法数据
        CrossValidationUtils.createDataSet("data/MultilayerPerceptronData/MLPAttributeList.txt",
                "CVData/MultilayerPerceptronData");
        // 贝叶斯算法数据
        CrossValidationUtils.createDataSet("data/NaiveBayesData/NaiveBayesAttributeList.txt",
                "CVData/NaiveBayesData");
        // 梯度增强树算法数据
        CrossValidationUtils.createDataSet("data/GradientBoostingTrees/GBTAttributeList.txt",
                "CVData/GradientBoostingTreesData");
    }
}
