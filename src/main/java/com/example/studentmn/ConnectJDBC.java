package com.example.studentmn;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectJDBC {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://bvw1yvhujo2wpm22lhdl-mysql.services.clever-cloud.com/bvw1yvhujo2wpm22lhdl";
            String user = "uyapm0dpzyup3y4r";
            String pass = "543bbvRt0Jw3jnbFT4jd";

            /*String url = "jdbc:mysql://localhost:3306/test";
            String user = "root";
            String pass = "";*/

            /*String url = "jdbc:mysql://bbijfqtchwxu1qzbcs3a-mysql.services.clever-cloud.com/bbijfqtchwxu1qzbcs3a";
            String user = "umqfjq7z8gsp3ifx";
            String pass = "8bN3SXmecwDzvSnadaZV";*/
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return connection;
    }
}
