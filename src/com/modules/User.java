package com.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;

    public User(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }


    public void register(){
        scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName=scanner.nextLine();
        System.out.print("Email: ");
        String email=scanner.nextLine();
        System.out.print("Password: ");
        String password=scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User Already Exists for this Email Address");
            return;
        }

        String query="insert into user(email,fullname,password) values(?,?,?)";

        try{
            preparedStatement=connection.prepareStatement(query);

            preparedStatement.setString(1,email);
            preparedStatement.setString(2,fullName);
            preparedStatement.setString(3,password);

            int row=preparedStatement.executeUpdate();

            if(row>0){
                System.out.println("Registration done... !!!!");
            }else{
                System.out.println("Failed to Register.....!!");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public String login(){
        scanner.nextLine();
        System.out.print("Email: ");
        String email=scanner.nextLine();
        System.out.print("Password: ");
        String password=scanner.nextLine();

        String query="select * from user where email=? and password=?";

        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            resultSet=preparedStatement.executeQuery();

            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean user_exist(String email){
        String query="select * from user where email=?";
        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            resultSet=preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
