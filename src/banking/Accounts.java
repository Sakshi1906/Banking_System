package banking;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
	
    private Connection con;
    private Scanner sc;
    
    public Accounts(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;

    }

    public long open_account(String email){
        if(!account_exist(email)) {
            String open_account_query = "INSERT INTO Accounts(acc_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = sc.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = sc.nextLine();
            try {
                long account_number = generateAccountNumber();
                PreparedStatement prepStmt = con.prepareStatement(open_account_query);
                prepStmt.setLong(1, account_number);
                prepStmt.setString(2, full_name);
                prepStmt.setString(3, email);
                prepStmt.setDouble(4, balance);
                prepStmt.setString(5, security_pin);
                int rowsAffected = prepStmt.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } 
                else {
                    throw new RuntimeException("Account Creation failed!!");
                }
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account Already Exist!");

    }

    public long getAccount_number(String email) {
        String query = "SELECT acc_number from Accounts WHERE email = ?";
        try{
            PreparedStatement prepStmt = con.prepareStatement(query);
            prepStmt.setString(1, email);
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("acc_number");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    private long generateAccountNumber() {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT acc_number from Accounts ORDER BY acc_number DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("acc_number");
                return last_account_number+1;
            } 
            else {
                return 1000100;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email){
        String query = "SELECT acc_number from Accounts WHERE email = ?";
        try{
            PreparedStatement prepStmt = con.prepareStatement(query);
            prepStmt.setString(1, email);
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
}
