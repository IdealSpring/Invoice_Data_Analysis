package cn.ccut.enterprise.utils;

import cn.ccut.enterprise.data.Dataset;
import cn.ccut.enterprise.data.Dataset.Attribute;
import cn.ccut.enterprise.exception.DescriptorException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性描述工具
 */
public class DescriptorUtils {
    private DescriptorUtils() {
    }

    /**
     * 返回属性列表
     *
     * @param descriptor
     * @return
     */
    public static Dataset.Attribute[] parseDescriptor(String descriptor) throws DescriptorException {
        List<Attribute> attributes = new ArrayList();
        String[] split = StringUtils.split(descriptor, "-");

        for(int i= 0; i < split.length; i++) {
            if("I".equalsIgnoreCase(split[i])) {
                attributes.add(Attribute.IGNORED);
            } else if("N".equalsIgnoreCase(split[i])) {
                attributes.add(Attribute.NUMERICAL);
            } else if("C".equalsIgnoreCase(split[i])) {
                attributes.add(Attribute.CATEGORICAL);
            } else {
                if(!"L".equalsIgnoreCase(split[i])) {
                    throw new DescriptorException("Bad Label : " + split[i]);
                }

                attributes.add(Attribute.LABEL);
            }
        }

        return (Attribute[])attributes.toArray(new Attribute[attributes.size()]);
    }
}
