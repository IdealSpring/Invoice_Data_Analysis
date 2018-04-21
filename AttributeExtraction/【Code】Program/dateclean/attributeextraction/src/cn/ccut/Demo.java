package cn.ccut;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Demo {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 5 + 6);

        int i = calendar.get(Calendar.MONTH);
        System.out.println(i);
    }
}
