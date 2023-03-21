package com.example.demo;
import java.sql.*;
public class ConnectJDBC {
    public static Connection getConnection(){
        Connection connection = null;
        try{
            String url = "jdbc:mysql://localhost:3306/studentmn";
            String user = "root";
            String pass = "";
            connection = DriverManager.getConnection(url, user, pass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
