package cn.ccut;

import java.text.DecimalFormat;

public class Demo {
    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("0.0000");
        double a = 0.123456789;
        String s = format.format(a);
        System.out.println(s);
    }
}
