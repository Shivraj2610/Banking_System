package com.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Account {
    private Connection connection;
    private Scanner scanner;
    PreparedStatement preparedStatement;
    private ResultSet resultSet=null;

    public Account(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }


    public long openAccount(String email){
        if(!account_exists(email)){
            String query="insert into accounts(account_number, fullname, email, balance, securitypin) values(?, ?, ?, ?, ?)";

            scanner.nextLine();
            System.out.print("Full Name: ");
            String fullName=scanner.nextLine();
            System.out.print("Initial Amount: ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Security Pin: ");
            String securityPin=scanner.nextLine();
            try {
                long account_number=generateAccNo();
                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,fullName);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,securityPin);
                int row=preparedStatement.executeUpdate();

                if(row>0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account creation failed");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Account Already Exists");
    }


    public long getAccountNo(String email){

        String query="select account_number from accounts where email=?";

        try {
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);

            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account number doesn't exist");
    }

    public long generateAccNo(){
        String query="select account_number from accounts ORDER BY account_number DESC LIMIT 1";
        try{
            preparedStatement=connection.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                long last_accNo =resultSet.getLong("account_number");
                return last_accNo+1;
            }else{
                return 1000100;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1000100;
    }

    public boolean account_exists(String email){
        String query="select * from accounts where email=?";

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
