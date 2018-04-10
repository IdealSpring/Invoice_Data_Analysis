package cn.ccut;

public class Demo {
    public static void main(String[] args) {
        double i = 1;
        double j = 0;
        double temp = i/j;
        System.out.println(temp);
        if(temp == Double.NaN) {
            System.out.println("NaN");
        }
    }
}
