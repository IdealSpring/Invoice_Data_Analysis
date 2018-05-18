package cn.ccut.stage04;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MergerReducer extends Reducer<Text, MergerTwo, MergerTwo, NullWritable> {
    private MergerTwo mergerTwo = new MergerTwo();

    @Override
    protected void reduce(Text key, Iterable<MergerTwo> values, Context context) throws IOException, InterruptedException {
        String mainFields = null;
        String secondaryFields = null;
        String resultFields = null;

        for(MergerTwo mergerTwo : values) {
            if(mergerTwo.isMain()) {
                mainFields = mergerTwo.getMainFields();
            } else {
                secondaryFields = mergerTwo.getSecondaryFields();
            }
        }

        if(mainFields != null && secondaryFields != null) {
            String mainFieldsOne = mainFields.substring(0, mainFields.lastIndexOf(","));
            String mainFieldsLabel = mainFields.substring(mainFields.lastIndexOf(","));

            String secondaryFieldsOne = secondaryFields.substring(secondaryFields.indexOf(","));
            resultFields = mainFieldsOne + secondaryFieldsOne + mainFieldsLabel;

            mergerTwo.setResultFields(resultFields);

            //去除有Null值的数据
            if(!secondaryFieldsOne.contains("Null")) {
                context.write(mergerTwo, NullWritable.get());
            }

        }

        clearUp();
    }

    private void clearUp() {
        mergerTwo.setMain(false);
        mergerTwo.setMainFields("Null");
        mergerTwo.setSecondaryFields("Null");
        mergerTwo.setResultFields("Null");
    }
}
