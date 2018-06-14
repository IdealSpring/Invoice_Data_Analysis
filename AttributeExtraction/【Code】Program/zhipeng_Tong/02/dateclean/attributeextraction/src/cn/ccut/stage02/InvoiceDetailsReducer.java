package cn.ccut.stage02;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

public class InvoiceDetailsReducer extends Reducer<Text, InvoiceDetails, InvoiceDetails, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<InvoiceDetails> values, Context context) throws IOException, InterruptedException {
        ArrayList<InvoiceDetails> detailsCustoms = new ArrayList<>();
        InvoiceDetails custom = null;

        for(InvoiceDetails value : values) {
            if(value.isDetails()) {
                String fp_nidMX = value.getFp_nidMX();
                String date_keyMX = value.getDate_keyMX();
                String hwmcMX = value.getHwmcMX();
                String ggxhMX = value.getGgxhMX();
                String dwMX = value.getDwMX();

                double slMX = value.getSlMX();
                double djMX = value.getDjMX();
                double jeMX = value.getJeMX();
                double seMX = value.getSeMX();

                String spbmMX = value.getSpbmMX();

                InvoiceDetails temp = new InvoiceDetails(fp_nidMX, date_keyMX, hwmcMX, ggxhMX,
                        dwMX, slMX, djMX, jeMX, seMX, spbmMX);

                detailsCustoms.add(temp);
            } else {
                String xf_id = value.getXf_id();
                String gf_id = value.getGf_id();

                custom = new InvoiceDetails();
                custom.setXf_id(xf_id);
                custom.setGf_id(gf_id);
            }
        }

        if(custom != null) {
            for(InvoiceDetails custom1 : detailsCustoms) {
                custom1.setXf_id(custom.getXf_id());
                custom1.setGf_id(custom.getGf_id());
                context.write(custom1, NullWritable.get());
            }
        }

    }
}
