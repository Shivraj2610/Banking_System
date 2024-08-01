package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconn {
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String user="root";
    private static final String pass="#Shivraj17#";

    public static Connection getConnection(){
        Connection connection=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection(url,user,pass);
        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
        }

        return connection;
    }
}
