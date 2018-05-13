package cn.demo;

import java.text.NumberFormat;

/**
 * 小数转换成百分数
 *
 * @authorMr.Robot
 * @create2018-05-13 15:38
 */
public class DoubleDemoe {

    public static void main(String[] args) {
        int i = 123;
        int ii = 1234;

        NumberFormat num = NumberFormat.getPercentInstance();
        num.setMaximumIntegerDigits(3);
        num.setMaximumFractionDigits(2);

        System.out.println(num.format((double)i / ii));

    }
}
