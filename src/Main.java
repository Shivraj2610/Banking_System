import com.db.DBconn;
import com.modules.Account;
import com.modules.AccountManager;
import com.modules.User;

import java.sql.Connection;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Connection connection= DBconn.getConnection();

        User user=new User(connection,scanner);
        Account account=new Account(connection,scanner);
        AccountManager accountManager=new AccountManager(connection,scanner);

        String email;
        long account_number;

        System.out.println("**** Welcome to Banking System ****");
        while(true){
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.println("Enter your choose");
            int choose1=scanner.nextInt();

            switch (choose1){
                case 1:
                    user.register();
                    break;

                case 2:
                    email = user.login();
                    if(email!=null){
                        System.out.println();
                        System.out.println("User Logged In!");

                        if(!account.account_exists(email)){
                            System.out.println();
                            System.out.println("1. Open a Account");
                            System.out.println("2. Exit");
                            if(scanner.nextInt()==1){
                                account_number=account.openAccount(email);
                                System.out.println("Account Created Successfully!");
                                System.out.println("Your account number is: "+account_number);
                            }
                        }

                        account_number=account.getAccountNo(email);
                        int choose2=0;

                        while (choose2 != 5){
                            System.out.println();
                            System.out.println("1. Credit Money");
                            System.out.println("2. Debit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Bank Balance");
                            System.out.println("5. Logged Out");

                            System.out.println("Enter your choose");
                            choose2=scanner.nextInt();

                            switch (choose2){
                                case 1:
                                    accountManager.credit(account_number);
                                    break;

                                case 2:
                                    accountManager.debit(account_number);
                                    break;

                                case 3:
                                    accountManager.transferMoney(account_number);
                                    break;

                                case 4:
                                    accountManager.checkBalance(account_number);
                                    break;

                                case 5:

                                    break;

                                default:
                                    System.out.println("Enter valid choose");
                                    break;
                            }

                        }

                    }else{
                        System.out.println("Incorrect Email or Password");
                        break;
                    }
                    break;

                case 0:
                    System.out.println("Thank you for visiting....");
                    System.out.println("Exit.....");
                    return;

                default:
                    System.out.println("Please Enter valid choose");
                    break;
            }
        }

//        user.register();
//        String email=user.login();
//        System.out.println(email);
    }
}