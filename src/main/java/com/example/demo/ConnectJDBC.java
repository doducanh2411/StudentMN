package com.example.demo;
import java.sql.*;
public class ConnectJDBC {
    public static Connection getConnection(){
        Connection connection = null;
        try{
            String url = "jdbc:mysql://bbijfqtchwxu1qzbcs3a-mysql.services.clever-cloud.com/bbijfqtchwxu1qzbcs3a";
            String user = "umqfjq7z8gsp3ifx";
            String pass = "8bN3SXmecwDzvSnadaZV";
            connection = DriverManager.getConnection(url, user, pass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}