package com.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void debit(long account_number){
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);

            if(account_number!=0){
                String query="select * from accounts where account_number=? and securitypin=?";
                preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,securityPin);

                resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                        double current_balance=resultSet.getDouble("balance");

                        if(current_balance>=amount){
                            String debit_query="update accounts set balance=balance-? where account_number=?";
                            PreparedStatement preparedStatement1 =connection.prepareStatement(debit_query);
                            preparedStatement1.setDouble(1,amount);
                            preparedStatement1.setLong(2,account_number);

                            int row=preparedStatement1.executeUpdate();
                            if(row>0){
                                System.out.println("Rs."+amount+" debited Successfully");
                                connection.commit();
                                connection.setAutoCommit(true);
                                return;
                            }else {
                                System.out.println("Transcation failed: ");
                                connection.rollback();
                                connection.setAutoCommit(true);
                            }
                        }
                }else{
                    System.out.println("Invalid Pin..");
                }
            }
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public void credit(long account_number){
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            String query="select * from accounts where account_number = ? and securitypin=?";
            preparedStatement=connection.prepareStatement(query);

            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);

            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                String credit_query="update accounts set balance=balance+? where account_number=?";
                PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);
                preparedStatement1.setDouble(1,amount);
                preparedStatement1.setLong(2,account_number);

                int row=preparedStatement1.executeUpdate();
                if(row>0){
                    System.out.println("Rs."+amount+" credited Successfully");
                    connection.commit();
                    connection.setAutoCommit(true);
                    return;
                }else{
                    System.out.println("Failed.........");
                    connection.setAutoCommit(true);
                }
            }else{
                System.out.println("Invalid Security Pin... !!!");
            }
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public void transferMoney(long sender_account_number){
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number=scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(receiver_account_number!=0 && sender_account_number!=0){
                preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and securitypin=?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance=resultSet.getLong("account_number");
                    if(amount<=current_balance){
                        String debit_query="Update accounts set balance=balance-? where account_number=?";
                        String credit_query="Update accounts set balance=balance+? where account_number=?";

                        PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
                        PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);

                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,sender_account_number);

                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,receiver_account_number);

                        int row1=debitPreparedStatement.executeUpdate();
                        int row2=creditPreparedStatement.executeUpdate();
                        

                        if(row1>0 && row2>0){
                            System.out.println("Transaction Successful");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            if(row2==0){
                                System.out.println("Incorrect Account Number");
                            }
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin..");
                }
            }else{
                System.out.println("Invalid Account Number...");
            }
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public void checkBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();

        String query="select balance from accounts where account_number=? and securitypin=?";

        try{
            preparedStatement=connection.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);

            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
}
