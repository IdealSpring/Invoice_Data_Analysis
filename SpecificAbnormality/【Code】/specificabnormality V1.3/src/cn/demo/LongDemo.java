package cn.demo;

/**
 * @authorMr.Robot
 * @create2018-05-07 20:37
 */
public class LongDemo {
    public static void main(String[] args) {
        Long d = 0L;
        for (int i = 0; i < 200; i++) {
            d ++;
            System.out.println(d.toString());
        }
    }
}
