package RMIBankService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * ============================================================
 * RMI BANK CLIENT
 * ============================================================
 * Client application for connecting to Bank RMI service.
 * Provides menu-driven interface for banking operations.
 * 
 * Features:
 *   - Create new accounts
 *   - Deposit and withdraw money
 *   - Check balance
 *   - Transfer between accounts
 * ============================================================
 */
public class BankClient {

    // ========== CLIENT CONFIGURATION ==========
    private static final String SERVER_URL = "rmi://localhost:1100/BankService";
    
    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RMI BANK CLIENT");
        System.out.println("============================================");
        
        try {
            // ========== CONNECT TO RMI SERVICE ==========
            BankInterface bank = (BankInterface) Naming.lookup(SERVER_URL);
            System.out.println("Connected to Bank Service!");
            
            Scanner scanner = new Scanner(System.in);
            
            // ========== MAIN MENU LOOP ==========
            while (true) {
                displayMenu();
                System.out.print("Choose option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1:
                        // ========== CREATE ACCOUNT ==========
                        System.out.print("Enter account holder name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter initial deposit: ");
                        double initialDeposit = scanner.nextDouble();
                        
                        String accountNumber = bank.createAccount(name, initialDeposit);
                        System.out.println("Account created successfully!");
                        System.out.println("Your account number: " + accountNumber);
                        break;
                        
                    case 2:
                        // ========== DEPOSIT ==========
                        System.out.print("Enter account number: ");
                        String depAccount = scanner.nextLine();
                        System.out.print("Enter deposit amount: ");
                        double depAmount = scanner.nextDouble();
                        
                        String depResult = bank.deposit(depAccount, depAmount);
                        System.out.println(depResult);
                        break;
                        
                    case 3:
                        // ========== WITHDRAW ==========
                        System.out.print("Enter account number: ");
                        String withAccount = scanner.nextLine();
                        System.out.print("Enter withdrawal amount: ");
                        double withAmount = scanner.nextDouble();
                        
                        String withResult = bank.withdraw(withAccount, withAmount);
                        System.out.println(withResult);
                        break;
                        
                    case 4:
                        // ========== CHECK BALANCE ==========
                        System.out.print("Enter account number: ");
                        String balAccount = scanner.nextLine();
                        
                        double balance = bank.getBalance(balAccount);
                        if (balance >= 0) {
                            System.out.println("Current Balance: " + balance);
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;
                        
                    case 5:
                        // ========== TRANSFER ==========
                        System.out.print("Enter source account number: ");
                        String fromAcc = scanner.nextLine();
                        System.out.print("Enter destination account number: ");
                        String toAcc = scanner.nextLine();
                        System.out.print("Enter transfer amount: ");
                        double transAmount = scanner.nextDouble();
                        
                        String transResult = bank.transfer(fromAcc, toAcc, transAmount);
                        System.out.println(transResult);
                        break;
                        
                    case 6:
                        // ========== ACCOUNT DETAILS ==========
                        System.out.print("Enter account number: ");
                        String detAccount = scanner.nextLine();
                        
                        String details = bank.getAccountDetails(detAccount);
                        System.out.println(details);
                        break;
                        
                    case 7:
                        // ========== EXIT ==========
                        System.out.println("Thank you for using our bank!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
                
                System.out.println();
            }
            
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== DISPLAY MENU ==========
    private static void displayMenu() {
        System.out.println("\n--- BANK MENU ---");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Check Balance");
        System.out.println("5. Transfer");
        System.out.println("6. Account Details");
        System.out.println("7. Exit");
    }
}
