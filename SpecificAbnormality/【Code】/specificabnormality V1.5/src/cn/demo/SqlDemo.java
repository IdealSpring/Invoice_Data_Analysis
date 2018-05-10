package cn.demo;

import java.sql.*;

/**
 * @authorMr.Robot
 * @create2018-05-08 20:34
 */
public class SqlDemo {
    public static void main(String[] args) {
        String industry = "8919";

        // 声明Connection对象
        Connection con;
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名invoice
        String url = "jdbc:mysql://localhost:3306/invoice";
        // Mysql配置的用户名
        String user = "root";
        // Mysql配置的密码
        String password = "root";
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连接数据库
            con = DriverManager.getConnection(url, user, password);
            // 创建statement类对象, 用来执行SQL语句
            Statement statement = con.createStatement();
            if (industry != null) {
                industry = industry.substring(0, 2);
                System.out.println(industry);
                String sql = "SELECT industry FROM enterprise_industrycode WHERE industrycode=" + industry;
                System.out.println(sql);
                // ResultSet类, 用来存放结果集
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    String industry1 = rs.getString(1);
                    System.out.println(industry1);
                }
            }
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
                        e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
            }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            }finally{
            System.out.println("数据库数据成功获取！！");
            }
    }
}
