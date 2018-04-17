package cn.ccut.enterprise.data;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 属性类型值数据集
 */
public class Dataset {
    private Dataset.Attribute[] attributes;
    private int[] ignored;
    private String[][] values;
    private int labelId;
    private int nbInstances;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static final String TYPE = "type";
    static final String VALUES = "values";
    static final String LABEL = "label";

    protected Dataset() {
    }

    Dataset(Dataset.Attribute[] attrs, List<String>[] values, int nbInstances) {
        validateValues(attrs, values);
        int nbattrs = countAttributes(attrs);
        this.attributes = new Dataset.Attribute[nbattrs];
        this.values = new String[nbattrs][];
        this.ignored = new int[attrs.length - nbattrs];
        this.labelId = -1;
        int ignoredId = 0;
        int ind = 0;

        for(int attr = 0; attr < attrs.length; ++attr) {
            if (attrs[attr].isIgnored()) {
                this.ignored[ignoredId++] = attr;
            } else {
                if (attrs[attr].isLabel()) {
                    if (this.labelId != -1) {
                        throw new IllegalStateException("Label found more than once");
                    }

                    this.labelId = ind;
                    attrs[attr] = Dataset.Attribute.CATEGORICAL;
                }

                if (attrs[attr].isCategorical() || attrs[attr].isLabel()) {
                    this.values[ind] = new String[values[attr].size()];
                    values[attr].toArray(this.values[ind]);
                }

                this.attributes[ind++] = attrs[attr];
            }
        }

        if (this.labelId == -1) {
            throw new IllegalStateException("Label not found");
        } else {
            this.nbInstances = nbInstances;
        }
    }

    public int nbValues(int attr) {
        return this.values[attr].length;
    }

    public String[] labels() {
        return (String[]) Arrays.copyOf(this.values[this.labelId], this.nblabels());
    }

    public int nblabels() {
        return this.values[this.labelId].length;
    }

    public int getLabelId() {
        return this.labelId;
    }

    public double getLabel(Instance instance) {
        return instance.get(this.getLabelId());
    }

    public Dataset.Attribute getAttribute(int attr) {
        return this.attributes[attr];
    }

    public int labelCode(String label) {
        return ArrayUtils.indexOf(this.values[this.labelId], label);
    }

    public String getLabelString(double code) {
        return Double.isNaN(code) ? "unknown" : this.values[this.labelId][(int)code];
    }

    public String toString() {
        return "attributes=" + Arrays.toString(this.attributes);
    }

    public int valueOf(int attr, String token) {
        Preconditions.checkArgument(!this.isNumerical(attr), "Only for CATEGORICAL attributes");
        Preconditions.checkArgument(this.values != null, "Values not found (equals null)");
        return ArrayUtils.indexOf(this.values[attr], token);
    }

    public int[] getIgnored() {
        return this.ignored;
    }

    private static int countAttributes(Dataset.Attribute[] attrs) {
        int nbattrs = 0;
        Dataset.Attribute[] arr$ = attrs;
        int len$ = attrs.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Dataset.Attribute attr = arr$[i$];
            if (!attr.isIgnored()) {
                ++nbattrs;
            }
        }

        return nbattrs;
    }

    private static void validateValues(Dataset.Attribute[] attrs, List<String>[] values) {
        Preconditions.checkArgument(attrs.length == values.length, "attrs.length != values.length");

        for(int attr = 0; attr < attrs.length; ++attr) {
            Preconditions.checkArgument(!attrs[attr].isCategorical() || values[attr] != null, "values not found for attribute " + attr);
        }

    }

    public int nbAttributes() {
        return this.attributes.length;
    }

    public boolean isNumerical(int attr) {
        return this.attributes[attr].isNumerical();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Dataset)) {
            return false;
        } else {
            Dataset dataset = (Dataset)obj;
            if (!Arrays.equals(this.attributes, dataset.attributes)) {
                return false;
            } else {
                for(int attr = 0; attr < this.nbAttributes(); ++attr) {
                    if (!Arrays.equals(this.values[attr], dataset.values[attr])) {
                        return false;
                    }
                }

                return this.labelId == dataset.labelId && this.nbInstances == dataset.nbInstances;
            }
        }
    }

    public int hashCode() {
        int hashCode = this.labelId + 31 * this.nbInstances;
        Dataset.Attribute[] arr$ = this.attributes;
        int len$ = arr$.length;

        int i$;
        for(i$ = 0; i$ < len$; ++i$) {
            Dataset.Attribute attr = arr$[i$];
            hashCode = 31 * hashCode + attr.hashCode();
        }

        String[][] arr1 = this.values;
        len$ = arr1.length;

        for(i$ = 0; i$ < len$; ++i$) {
            String[] valueRow = arr1[i$];
            if (valueRow != null) {
                String[] arr2 = valueRow;
                int len2 = valueRow.length;

                for(int i = 0; i < len2; ++i$) {
                    String value = arr2[i];
                    hashCode = 31 * hashCode + value.hashCode();
                }
            }
        }

        return hashCode;
    }

    public static Dataset load(Configuration conf, Path path) throws IOException {
        FileSystem fs = path.getFileSystem(conf);
        long bytesToRead = fs.getFileStatus(path).getLen();
        byte[] buff = new byte[Long.valueOf(bytesToRead).intValue()];
        FSDataInputStream input = fs.open(path);

        try {
            input.readFully(buff);
        } finally {
            Closeables.close(input, true);
        }

        String var7 = new String(buff, Charset.defaultCharset());
        return fromJSON(var7);
    }

    public String toJSON() {
        List<Map<String, Object>> toWrite = new LinkedList();
        int ignoredCount = 0;

        for(int i = 0; i < this.attributes.length + this.ignored.length; ++i) {
            int attributesIndex = i - ignoredCount;
            Map attribute;
            if (ignoredCount < this.ignored.length && i == this.ignored[ignoredCount]) {
                attribute = this.getMap(Dataset.Attribute.IGNORED, (String[])null, false);
                ++ignoredCount;
            } else if (attributesIndex == this.labelId) {
                attribute = this.getMap(this.attributes[attributesIndex], this.values[attributesIndex], true);
            } else {
                attribute = this.getMap(this.attributes[attributesIndex], this.values[attributesIndex], false);
            }

            toWrite.add(attribute);
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(toWrite);
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static Dataset fromJSON(String json) {
        List fromJSON;
        try {
            fromJSON = (List)OBJECT_MAPPER.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception var11) {
            throw new RuntimeException(var11);
        }

        List<Dataset.Attribute> attributes = new LinkedList();
        List<Integer> ignored = new LinkedList();
        String[][] nominalValues = new String[fromJSON.size()][];
        Dataset dataset = new Dataset();

        int i;
        for(i = 0; i < fromJSON.size(); ++i) {
            Map<String, Object> attribute = (Map)fromJSON.get(i);
            if (Dataset.Attribute.fromString((String)attribute.get("type")) == Dataset.Attribute.IGNORED) {
                ignored.add(i);
            } else {
                Dataset.Attribute asAttribute = Dataset.Attribute.fromString((String)attribute.get("type"));
                attributes.add(asAttribute);
                if ((Boolean)attribute.get("label")) {
                    dataset.labelId = i - ignored.size();
                }

                if (attribute.get("values") != null) {
                    List<String> get = (List)attribute.get("values");
                    String[] array = (String[])get.toArray(new String[get.size()]);
                    nominalValues[i - ignored.size()] = array;
                }
            }
        }

        dataset.attributes = (Dataset.Attribute[])attributes.toArray(new Dataset.Attribute[attributes.size()]);
        dataset.ignored = new int[ignored.size()];
        dataset.values = nominalValues;

        for(i = 0; i < dataset.ignored.length; ++i) {
            dataset.ignored[i] = (Integer)ignored.get(i);
        }

        return dataset;
    }

    private Map<String, Object> getMap(Dataset.Attribute type, String[] values, boolean isLabel) {
        Map<String, Object> attribute = new HashMap();
        attribute.put("type", type.toString().toLowerCase(Locale.getDefault()));
        attribute.put("values", values);
        attribute.put("label", isLabel);
        return attribute;
    }

    public static enum Attribute {
        IGNORED,
        NUMERICAL,
        CATEGORICAL,
        LABEL;

        private Attribute() {
        }

        public boolean isNumerical() {
            return this == NUMERICAL;
        }

        public boolean isCategorical() {
            return this == CATEGORICAL;
        }

        public boolean isLabel() {
            return this == LABEL;
        }

        public boolean isIgnored() {
            return this == IGNORED;
        }

        private static Dataset.Attribute fromString(String from) {
            Dataset.Attribute toReturn = LABEL;
            if (NUMERICAL.toString().equalsIgnoreCase(from)) {
                toReturn = NUMERICAL;
            } else if (CATEGORICAL.toString().equalsIgnoreCase(from)) {
                toReturn = CATEGORICAL;
            } else if (IGNORED.toString().equalsIgnoreCase(from)) {
                toReturn = IGNORED;
            }

            return toReturn;
        }
    }
}
