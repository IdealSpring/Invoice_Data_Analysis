package cn.ccut.enterprise.data;

import cn.ccut.enterprise.exception.DescriptorException;
import cn.ccut.enterprise.utils.DescriptorUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import cn.ccut.enterprise.data.Dataset.Attribute;

/**
 * 数据加载类
 */
public class DataLoader {
    private static final Pattern SEPARATORS = Pattern.compile("[, ]");

    private DataLoader() {
    }


    /**
     * 生成数据集
     *
     * @param descriptor
     * @param fs
     * @param path
     * @return
     */
    public static Dataset generateDataset(String descriptor, FileSystem fs, Path path) throws IOException, DescriptorException {
        Attribute[] attrs = DescriptorUtils.parseDescriptor(descriptor);
        FSDataInputStream input = fs.open(path);
        Scanner scanner = new Scanner(input, "UTF-8");
        Set<String>[] valsets = new Set[attrs.length];
        int size = 0;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty() && parseString(attrs, valsets, line)) {
                ++size;
            }
        }

        scanner.close();
        List<String>[] values = new List[attrs.length];

        for(int i = 0; i < valsets.length; ++i) {
            if (valsets[i] != null) {
                values[i] = Lists.newArrayList(valsets[i]);
            }
        }

        return new Dataset(attrs, values, size);
    }

    private static boolean parseString(Attribute[] attrs, Set<String>[] values, String string) {
        String[] tokens = SEPARATORS.split(string);
        Preconditions.checkArgument(tokens.length == attrs.length, "Wrong number of attributes in the string: " + tokens.length + ". Must be: " + attrs.length);

        int attr;
        for(attr = 0; attr < attrs.length; ++attr) {
            if (!attrs[attr].isIgnored() && "?".equals(tokens[attr])) {
                return false;
            }
        }

        for(attr = 0; attr < attrs.length; ++attr) {
            if (!attrs[attr].isIgnored()) {
                String token = tokens[attr];
                if (!attrs[attr].isCategorical() && !attrs[attr].isLabel()) {
                    try {
                        Double.parseDouble(token);
                    } catch (NumberFormatException var8) {
                        return false;
                    }
                } else {
                    if (values[attr] == null) {
                        values[attr] = new HashSet();
                    }

                    values[attr].add(token);
                }
            }
        }

        return true;
    }
}
