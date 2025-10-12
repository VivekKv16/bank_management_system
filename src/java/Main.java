//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Welcome to XBank");

        while(true) {
            while(true) {
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter Your choice : ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        BankTransaction.createAccount();
                        break;
                    case 2:
                        BankTransaction.login();
                        break;
                    case 3:
                        System.out.println("Thank You ");
                        System.exit(0);
                    default:
                        System.out.println("Invaild choice ");
                }
            }
        }
    }
}