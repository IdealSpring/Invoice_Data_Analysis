package cn.ccut;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Demo {
    public static void main(String[] args) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        System.out.println(c1.before(c2));
    }
}
