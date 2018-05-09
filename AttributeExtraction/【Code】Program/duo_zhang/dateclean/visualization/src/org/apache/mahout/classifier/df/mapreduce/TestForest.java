/*
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

package org.apache.mahout.classifier.df.mapreduce;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.RegressionResultAnalyzer;
import org.apache.mahout.classifier.ResultAnalyzer;
import org.apache.mahout.classifier.df.DFUtils;
import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.data.DataConverter;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;
import org.apache.mahout.common.CommandLineUtil;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.common.commandline.DefaultOptionCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Tool to classify a Dataset using a previously built Decision Forest
 */
public class TestForest extends Configured implements Tool {

    private static final Logger log = LoggerFactory.getLogger(TestForest.class);

    private FileSystem dataFS;
    private Path dataPath; // test data path

    private Path datasetPath;

    private Path modelPath; // path where the forest is stored

    private FileSystem outFS;
    private Path outputPath; // path to predictions file, if null do not output the predictions

    private boolean analyze; // analyze the classification results ?

    private boolean useMapreduce; // use the mapreduce classifier ?

    @Override
    public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        GroupBuilder gbuilder = new GroupBuilder();

        Option inputOpt = DefaultOptionCreator.inputOption().create();

        Option datasetOpt = obuilder.withLongName("dataset").withShortName("ds").withRequired(true).withArgument(
                abuilder.withName("dataset").withMinimum(1).withMaximum(1).create()).withDescription("Dataset path")
                .create();

        Option modelOpt = obuilder.withLongName("model").withShortName("m").withRequired(true).withArgument(
                abuilder.withName("path").withMinimum(1).withMaximum(1).create()).
                withDescription("Path to the Decision Forest").create();

        Option outputOpt = DefaultOptionCreator.outputOption().create();

        Option analyzeOpt = obuilder.withLongName("analyze").withShortName("a").withRequired(false).create();

        Option mrOpt = obuilder.withLongName("mapreduce").withShortName("mr").withRequired(false).create();

        Option helpOpt = DefaultOptionCreator.helpOption();

        Group group = gbuilder.withName("Options").withOption(inputOpt).withOption(datasetOpt).withOption(modelOpt)
                .withOption(outputOpt).withOption(analyzeOpt).withOption(mrOpt).withOption(helpOpt).create();

        try {
            Parser parser = new Parser();
            parser.setGroup(group);
            CommandLine cmdLine = parser.parse(args);

            if (cmdLine.hasOption("help")) {
                CommandLineUtil.printHelp(group);
                return -1;
            }

            String dataName = cmdLine.getValue(inputOpt).toString();
            String datasetName = cmdLine.getValue(datasetOpt).toString();
            String modelName = cmdLine.getValue(modelOpt).toString();
            String outputName = cmdLine.hasOption(outputOpt) ? cmdLine.getValue(outputOpt).toString() : null;
            analyze = cmdLine.hasOption(analyzeOpt);
            useMapreduce = cmdLine.hasOption(mrOpt);

            if (log.isDebugEnabled()) {
                log.debug("inout     : {}", dataName);
                log.debug("dataset   : {}", datasetName);
                log.debug("model     : {}", modelName);
                log.debug("output    : {}", outputName);
                log.debug("analyze   : {}", analyze);
                log.debug("mapreduce : {}", useMapreduce);
            }

            dataPath = new Path(dataName);
            datasetPath = new Path(datasetName);
            modelPath = new Path(modelName);
            if (outputName != null) {
                outputPath = new Path(outputName);
            }
        } catch (OptionException e) {
            log.warn(e.toString(), e);
            CommandLineUtil.printHelp(group);
            return -1;
        }

        testForest();

        return 0;
    }

    private void testForest() throws IOException, ClassNotFoundException, InterruptedException {

        // make sure the output file does not exist
        if (outputPath != null) {
            outFS = outputPath.getFileSystem(getConf());
            if (outFS.exists(outputPath)) {
                throw new IllegalArgumentException("Output path already exists");
            }
        }

        // make sure the decision forest exists
        FileSystem mfs = modelPath.getFileSystem(getConf());
        if (!mfs.exists(modelPath)) {
            throw new IllegalArgumentException("The forest path does not exist");
        }

        // make sure the test data exists
        dataFS = dataPath.getFileSystem(getConf());
        if (!dataFS.exists(dataPath)) {
            throw new IllegalArgumentException("The Test data path does not exist");
        }

        if (useMapreduce) {
            mapreduce();
        } else {
            sequential();
        }

    }

    private void mapreduce() throws ClassNotFoundException, IOException, InterruptedException {
        if (outputPath == null) {
            throw new IllegalArgumentException("You must specify the ouputPath when using the mapreduce implementation");
        }

        Classifier classifier = new Classifier(modelPath, dataPath, datasetPath, outputPath, getConf());

        classifier.run();

        if (analyze) {
            double[][] results = classifier.getResults();
            if (results != null) {
                Dataset dataset = Dataset.load(getConf(), datasetPath);
                if (dataset.isNumerical(dataset.getLabelId())) {
                    RegressionResultAnalyzer regressionAnalyzer = new RegressionResultAnalyzer();
                    regressionAnalyzer.setInstances(results);
                    log.info("{}", regressionAnalyzer);
                } else {
                    ResultAnalyzer analyzer = new ResultAnalyzer(Arrays.asList(dataset.labels()), "unknown");
                    for (double[] res : results) {
                        analyzer.addInstance(dataset.getLabelString(res[0]),
                                new ClassifierResult(dataset.getLabelString(res[1]), 1.0));
                    }
                    log.info("{}", analyzer);

                }
            }
        }
    }

    private void sequential() throws IOException {

        log.info("Loading the forest...");
        DecisionForest forest = DecisionForest.load(getConf(), modelPath);

        if (forest == null) {
            log.error("No Decision Forest found!");
            return;
        }

        // load the dataset
        Dataset dataset = Dataset.load(getConf(), datasetPath);
        DataConverter converter = new DataConverter(dataset);

        log.info("Sequential classification...");
        long time = System.currentTimeMillis();

        Random rng = RandomUtils.getRandom();

        List<double[]> resList = new ArrayList<>();
        if (dataFS.getFileStatus(dataPath).isDir()) {
            //the input is a directory of files
            testDirectory(outputPath, converter, forest, dataset, resList, rng);
        }  else {
            // the input is one single file
            testFile(dataPath, outputPath, converter, forest, dataset, resList, rng);
        }

        time = System.currentTimeMillis() - time;
        log.info("Classification Time: {}", DFUtils.elapsedTime(time));

        if (analyze) {
            if (dataset.isNumerical(dataset.getLabelId())) {
                RegressionResultAnalyzer regressionAnalyzer = new RegressionResultAnalyzer();
                double[][] results = new double[resList.size()][2];
                regressionAnalyzer.setInstances(resList.toArray(results));
                log.info("{}", regressionAnalyzer);
            } else {
                ResultAnalyzer analyzer = new ResultAnalyzer(Arrays.asList(dataset.labels()), "unknown");
                for (double[] r : resList) {
                    analyzer.addInstance(dataset.getLabelString(r[0]),
                            new ClassifierResult(dataset.getLabelString(r[1]), 1.0));
                }
                log.info("{}", analyzer);

                //将fiscore写入文档
                String weightedF1score = analyzer.getWeightedF1score();
                writeF1scoreToFile(weightedF1score);
            }
        }
    }

    /**
     * 将F1score写进文件
     *
     * @param weightedF1score
     * @throws IOException
     */
    private void writeF1scoreToFile(String weightedF1score) throws IOException {
        File f1scoreFile = new File("D:/视频诗词/异常企业资料/F1_Score/f1score_list.txt");
        //文件原始数据
        ArrayList<String> data = new ArrayList<>();

        if(f1scoreFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(f1scoreFile));

            String line = null;
            while ((line = reader.readLine()) != null) {
                data.add(line.trim());
            }

            reader.close();
        }

        data.add(weightedF1score);

        BufferedWriter writer = new BufferedWriter(new FileWriter(f1scoreFile));

        for(String line : data) {
            writer.write(line.trim());
            writer.newLine();
            writer.flush();
        }

        writer.close();
    }

    private void testDirectory(Path outPath,
                               DataConverter converter,
                               DecisionForest forest,
                               Dataset dataset,
                               Collection<double[]> results,
                               Random rng) throws IOException {
        Path[] infiles = DFUtils.listOutputFiles(dataFS, dataPath);

        for (Path path : infiles) {
            log.info("Classifying : {}", path);
            Path outfile = outPath != null ? new Path(outPath, path.getName()).suffix(".out") : null;
            testFile(path, outfile, converter, forest, dataset, results, rng);
        }
    }

    private void testFile(Path inPath,
                          Path outPath,
                          DataConverter converter,
                          DecisionForest forest,
                          Dataset dataset,
                          Collection<double[]> results,
                          Random rng) throws IOException {
        //显示决策森林
        //forest.showForest();

        // create the predictions file
        FSDataOutputStream ofile = null;

        if (outPath != null) {
            ofile = outFS.create(outPath);
        }

        try (FSDataInputStream input = dataFS.open(inPath)){
            Scanner scanner = new Scanner(input, "UTF-8");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {

                    Instance instance = converter.convert(line);
                    double prediction = forest.classify(dataset, rng, instance);

                    int predictionInt = 0;
                    if(prediction == 1.0) {
                        predictionInt = 1;
                    } else {
                        predictionInt = 0;
                    }

                    String nsr_id = line.substring(0, line.indexOf(","));

                    if (ofile != null) {
                        String record = getPrediction(nsr_id);
                        if(record != null) {
                            ofile.writeBytes(record + "," + Integer.toString(predictionInt) + "\n");
                        } else {
                            ofile.writeBytes("null," + nsr_id + "," + "null," + "null," + "null," + Integer.toString(predictionInt) + "\n");
                        }
                    }

                    /*if (ofile != null) {
                        ofile.writeChars(Double.toString(prediction)); // write the prediction
                        ofile.writeChar('\n');
                    }*/

                    results.add(new double[]{dataset.getLabel(instance), prediction});
                }
            }

            scanner.close();
        }
    }

    /**
     * 添加完整信息
     *
     * @param
     * @return
     */
    private String getPrediction(String nsrxx_id) throws IOException {
        String fileNsrxx = "D://视频诗词/异常企业资料/信息3/nsrxx.dat";
        BufferedReader readerNsrxx = new BufferedReader(new FileReader(fileNsrxx));
        ArrayList<String> dataNsrxx = new ArrayList<>();

        String line = null;
        while ((line = readerNsrxx.readLine()) != null) {
            dataNsrxx.add(line);
        }

        readerNsrxx.close();

        for(int i = 0; i < dataNsrxx.size(); i++) {
            String nsrxx = dataNsrxx.get(i);
            String[] split1 = nsrxx.split(",");
            String nsr_id = split1[1].trim();

            if(nsrxx_id.equals(nsr_id)) {
                line = nsrxx.substring(0, nsrxx.lastIndexOf(","));
                dataNsrxx.remove(i);
                break;
            }
        }

        return line;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new TestForest(), args);
    }

}
