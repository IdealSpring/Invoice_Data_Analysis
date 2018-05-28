package cn.ccut.stage04;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MergerTwo  implements Writable {
    //是否是前15个属性字段
    private boolean isMain = false;
    //前15个属性字段
    private String mainFields = "Null";
    //hwmx中的字段
    private String secondaryFields = "Null";
    //最后结果
    private String resultFields = "Null";

    @Override
    public String toString() {
        return DealWithResult.changeResult(resultFields);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(this.isMain);
        dataOutput.writeUTF(this.mainFields);
        dataOutput.writeUTF(this.secondaryFields);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.isMain = dataInput.readBoolean();
        this.mainFields = dataInput.readUTF();
        this.secondaryFields = dataInput.readUTF();
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getMainFields() {
        return mainFields;
    }

    public void setMainFields(String mainFields) {
        this.mainFields = mainFields;
    }

    public String getSecondaryFields() {
        return secondaryFields;
    }

    public void setSecondaryFields(String secondaryFields) {
        this.secondaryFields = secondaryFields;
    }

    public String getResultFields() {
        return resultFields;
    }

    public void setResultFields(String resultFields) {
        this.resultFields = resultFields;
    }
}
