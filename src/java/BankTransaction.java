import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class BankTransaction {
    //    static BankAccount b=new BankAccount("12335666","vivek k v");
    static Scanner sc = new Scanner(System.in);
    static Connection con = null;

    static PreparedStatement ps = null;
    static int loggedIn = 0;


    static void createAccount() throws SQLException, ClassNotFoundException {

        System.out.println("Enter Your Name");
        String name = sc.next();
        System.out.println("Enter the email");
        String email = sc.next();
        System.out.println("Enter the password");
        String password = sc.next();
        System.out.println("enter the initial Balance");
        double balance = sc.nextDouble();
        con = ProvideConnection.giveusConnection();

        String iqry = "insert into bankaccount(name,email,password,balance) values(?,?,?,?)";
        ps = con.prepareStatement(iqry);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setDouble(4, balance);
        int rs = ps.executeUpdate();
        if (rs > 0) {
            System.out.println("Account Created Successfull");
        } else {
            System.out.println("Failed to Create Account");
        }
    }

    static void login() throws ClassNotFoundException, SQLException {
        System.out.println("Enter the email");
        String email = sc.next();
        System.out.println("Enter the password");
        String password = sc.next();
        con = ProvideConnection.giveusConnection();
        String sqry = "select * from bankaccount where email=? and password=?";
        ps = con.prepareStatement(sqry);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("logged in SuccessFull");
            System.out.println("Welcome " + rs.getString("name"));
            int loggedIn = rs.getInt(1);
            if (loggedIn != 0) {
                while (loggedIn != 0) {
                    loginMenu();
                    System.out.println("Enter the choice");
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1:
                            withdraw();
                            break;
                        case 2:
                            deposit();
                            break;
                        case 3:
                            checkBalance();
                            break;
                        case 4:
                            transferAmount();
                            break;
                        case 5:
                            transactions();
                            break;
                        case 6:
                            viewAccount(rs.getLong(1));
                            break;
                        case 7:
                            loggedIn=0;
                            break;
                        default:
                            System.out.println("invalid input");
                    }
                }
            } else{
                System.out.println("No Record Found");
                System.out.println("Login Failed");
            }

        }
    }

    private static void viewAccount(long accountNumber) throws SQLException {
        System.out.println("enter the number");

        System.out.println("Your Account Information : ");
        String sqry="select * from bankaccount where accountnumber=?";
        ps=con.prepareStatement(sqry);
        ps.setLong(1,accountNumber);
        ResultSet rs=ps.executeQuery();
        rs.next();
        System.out.println("Name : "+rs.getString("name"));
        System.out.println("Account Number : "+rs.getLong("accountnumber"));
        System.out.println("Email : "+rs.getString("email"));
        System.out.println("Current balance : "+rs.getDouble("balance"));
    }



    private static void loginMenu() {

        System.out.println("1. withdraw");
        System.out.println("2. deposit");
        System.out.println("3.  Check Balance");
        System.out.println("4. transfer Amount");
        System.out.println("5. transaction");
        System.out.println("6. view Account");
        System.out.println("7. logout");

    }

    private static void withdraw() throws SQLException, ClassNotFoundException {
        System.out.println("Enter your Account Number:");
        long accountNumber = sc.nextLong();
        System.out.println("Enter your Password:");
        String password = sc.next();
        System.out.println("Enter the Amount to Withdraw:");
        double amount = sc.nextDouble();

        con = ProvideConnection.giveusConnection();
        String sqry = "select balance from bankaccount where AccountNumber=? AND password=?";
        ps = con.prepareStatement(sqry);
        ps.setLong(1, accountNumber);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            double balfetch = rs.getDouble("balance");
            if (balfetch >= amount) {
                String iqry = "update bankaccount set balance=balance-? where AccountNumber=? and password=?";
                PreparedStatement ps2 = con.prepareStatement(iqry);
                ps2.setDouble(1, amount);
                ps2.setLong(2, accountNumber);
                ps2.setString(3, password);
                int updated = ps2.executeUpdate();
                if (updated > 0) {
                    double remainingBalance = balfetch - amount;
                    System.out.println("Withdrawal Successfull..... Remaining balance: " + remainingBalance);
                    recordTrnsactionHistory("withdraw", amount, accountNumber);

                } else {
                    System.out.println("Withdraw Failed");
                }
            }
        }
        else{
            System.out.println("wrong password and email");
        }
    }

    private static void deposit() throws SQLException, ClassNotFoundException {
        System.out.println("Enter your Account Number:");
        long accountNumber = sc.nextLong();
        System.out.println("Enter your Password:");
        String password = sc.next();
        System.out.println("Enter the Amount to Deposit:");
        double amount = sc.nextDouble();
        con= ProvideConnection.giveusConnection();

        String sqry = "select balance from bankaccount where AccountNumber=? AND password=?";
        ps = con.prepareStatement(sqry);
        ps.setLong(1, accountNumber);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            double balfetch = rs.getDouble("balance");
            if (amount>0) {
                String iqry = "update bankaccount set balance=balance+? where AccountNumber=? and password=?";
                PreparedStatement ps2 = con.prepareStatement(iqry);
                ps2.setDouble(1, amount);
                ps2.setLong(2, accountNumber);
                ps2.setString(3, password);
                int updated = ps2.executeUpdate();
                if (updated > 0) {
                    recordTrnsactionHistory("deposit", amount, accountNumber);
                    System.out.println("Deposit Successfull");
                    double remainingBalance = balfetch + amount;
                    System.out.println("deposit Successfull..... Remaining balance: " + remainingBalance);

                } else {
                    System.out.println("deposit Failed");
                }
            }
        }
        else{
            System.out.println("wrong password and email");
        }
    }

    static void checkBalance() throws SQLException, ClassNotFoundException {
        System.out.println("Enter your Account Number:");
        String accountNumber = sc.next();
        System.out.println("Enter your Password:");
        String password = sc.next();
        con= ProvideConnection.giveusConnection();
        String iqry="select balance from bankaccount where AccountNumber=? and password=?";
        ps=con.prepareStatement(iqry);
        ps.setString(1,accountNumber);
        ps.setString(2,password);
        ResultSet rs= ps.executeQuery();
        if(rs.next()){
            double balfetch = rs.getDouble("balance");
            System.out.println(balfetch);
        }


    }


    private static void transactions() throws SQLException {
        System.out.println("enter the account number");
        int accountNumber = sc.nextInt();
        String sqry = "select * from transactions where AccountNumber=? order by timestamp desc";
        ps = con.prepareStatement(sqry);
        ps.setInt(1, accountNumber);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("trx_id :" + rs.getLong(1) + " |  trx_type :" + rs.getString(3) + " |  trx_amnt :"
                    + rs.getDouble(4) + " |  trx_Account ID :" + rs.getLong(5) + " |  trx_date :" + rs.getTimestamp(6));

        }
    }

    private static void transferAmount() throws SQLException {
        System.out.println("Enter your password");
        String password = sc.next();
        System.out.println("Enter the Account id to send the Amount");
        long accountNumber = sc.nextLong();
        System.out.println("Enter the Amount to transfer");
        double amount = sc.nextDouble();
        try {
            con.setAutoCommit(false);
            String sqry = "update bankaccount set balance=balance-? where password=?";

            ps = con.prepareStatement(sqry);
            ps.setDouble(1, amount);
            ps.setString(2, password);
            int res = ps.executeUpdate();

            String sqry1 = "update bankaccount set balance=balance+? where accountNumber=?";
            ps = con.prepareStatement(sqry1);
            ps.setDouble(1, amount);
            ps.setLong(2, accountNumber);
            int res1 = ps.executeUpdate();
            System.out.println("Sucessfully Transferred to the ID : " + accountNumber);
            recordTrnsactionHistory("credit", amount, accountNumber);
        }
        catch(SQLException e){
            try {
                System.out.println("transfer failed");
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



    }
    private static void recordTrnsactionHistory(String type, double amount, long accountNumber) throws SQLException {
        String iqry = "insert into transactions(type,amount,AccountNumber,timestamp) values(?,?,?,?)";
        ps = con.prepareStatement(iqry);
        ps.setString(1, type);
        ps.setDouble(2, amount);
        ps.setLong(3, accountNumber);
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        int res1 = ps.executeUpdate();
        if(res1>0) {
            System.out.println("transaction saved");
        }
    }
}




//
//
//    void checkBalance(int pin){
//        if(pin==b.getPin()){
//            System.out.println(b.getBalance());
//        }
//    }

//    void withdraw(double amount,int pin){
//        if(pin==b.getPin() && amount<b.getBalance()){
//            double bal=b.getBalance();
//            bal=bal-amount;
//            b.setBalance(bal);
//            System.out.println("withdraw Successfull");
//        }
//        else{
//            System.out.println("insufficient balanace");
//        }
//    }

//void deposit(double amount){
//    if(amount>0){
//        double bal=b.getBalance();
//        bal=bal+amount;
//        b.setBalance(bal);
//    }
//    else{
//        System.out.println("enter the valid amount");
//    }
//}

//}
