package cn.ccut;

/**
 * @authorMr.Robot
 * @create2018-05-26 19:32
 */
public class StringDemo {
    public static void main(String[] args) {
        String s = "173641,1,0,1,0,1,0,0,0,0,0,0,1,0,1,0,1,1,1,2";
        String[] s1 = s.split(",");
        String nsr_id = s1[0];
//        System.out.println(nsr_id);

        int j = s1.length;

        String lable = s1[s1.length - 1];
        String ss = "";

        for (int i = 1; i <= j - 2; i ++) {
            ss += " " + i + ":" + s1[i];
        }

        String sss = lable + " " + ss;

        System.out.println(sss);
    }
}
