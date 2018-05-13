package lv.arturmaslenkov;


import org.postgresql.Driver;

import java.sql.*;

public class Table {
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    public static void main(String[] args) {
        Connection con = null;
        Statement stmnt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting...");
            DriverManager.registerDriver(new Driver());
            con = DriverManager.getConnection("jdbc:postgresql://ec2-79-125-117-53.eu-west-1.compute.amazonaws.com:5432/d1d8vh7p5kpuj9?user=lckrpemjcoirra&password=f26666ee47aeace16c9689b8b4a49d52c07a68b616aea82f1ef8b827a06700b2&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");

            stmnt = con.createStatement();
            stmnt.execute("CREATE TABLE chat " +
                    "(id INT PRIMARY KEY NOT NULL," +
                    "nickname VARCHAR(100)," +
                    "message VARCHAR(1000))");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmnt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
