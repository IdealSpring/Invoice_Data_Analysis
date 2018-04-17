package cn.ccut.enterprise.tools;

import cn.ccut.enterprise.data.DataLoader;
import cn.ccut.enterprise.data.Dataset;
import cn.ccut.enterprise.exception.DescriptorException;
import cn.ccut.enterprise.utils.DFUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 训练数据属性值描述类
 */
public class Describe {
    private static final Logger log = LoggerFactory.getLogger(Describe.class);

    public static void main(String[] args) {
        //训练数据路径
        String dataPath = "C:\\Users\\zhipeng-Tong\\Desktop\\训练数据_测试数据及说明_V1.0\\训练数据及测试数据\\tran.dat";
        //属性值描述文件输出路径
        String descPath = "C:\\Users\\zhipeng-Tong\\Desktop\\训练数据_测试数据及说明_V1.0\\训练数据及测试数据\\tranAttr.info";
        //属性类型描述
        String descriptor = "I-C-C-C-C-C-L";

        try {
            runTool(dataPath, descriptor, descPath);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

    /**
     * 运行工具
     * @param dataPath
     * @param descriptor
     * @param filePath
     */
    private static void runTool(String dataPath, String descriptor, String filePath) throws DescriptorException, IOException {
        Path fPath = validateOutput(filePath);
        log.info("generating......");
        Dataset dataset = generateDataset(descriptor, dataPath);
        log.info("storing......");
        String json = dataset.toJSON();
        DFUtils.storeString(new Configuration(), fPath, json);
    }

    /**
     * 生成数据集合
     *
     * @param descriptor
     * @param dataPath
     * @return
     */
    private static Dataset generateDataset(String descriptor, String dataPath) throws IOException, DescriptorException {
        Path path = new Path(dataPath);
        FileSystem fs = path.getFileSystem(new Configuration());
        return DataLoader.generateDataset(descriptor, fs, path);
    }

    /**
     * 检查输出路径是否已经存在
     *
     * @param filePath
     * @return
     */
    private static Path validateOutput(String filePath) throws IOException {
        Path path = new Path(filePath);
        FileSystem fs = path.getFileSystem(new Configuration());
        if (fs.exists(path)) {
            throw new IllegalStateException("Attr.info already exists......");
        } else {
            return path;
        }
    }
}
