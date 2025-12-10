package RMIBankService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================
 * RMI BANK SERVICE IMPLEMENTATION
 * ============================================================
 * Implementation of the Bank remote interface.
 * Manages accounts using HashMaps for storage.
 * 
 * Features:
 *   - Account creation with unique IDs
 *   - Deposit and withdrawal operations
 *   - Balance inquiry
 *   - Inter-account transfers
 * ============================================================
 */
public class BankImplementation extends UnicastRemoteObject implements BankInterface {

    // ========== ACCOUNT STORAGE ==========
    // HashMap storing account balances
    private HashMap<String, Double> accountBalances = new HashMap<>();
    
    // HashMap storing account holder names
    private HashMap<String, String> accountHolders = new HashMap<>();
    
    // Counter for generating unique account numbers
    private AtomicInteger accountCounter = new AtomicInteger(1000);

    // ========== CONSTRUCTOR ==========
    public BankImplementation() throws RemoteException {
        super();
        System.out.println("Bank Service initialized with empty accounts.");
    }

    // ========== ACCOUNT MANAGEMENT ==========
    
    @Override
    public synchronized String createAccount(String accountHolder, double initialDeposit) throws RemoteException {
        // Generate unique account number
        String accountNumber = "ACC" + accountCounter.incrementAndGet();
        
        // Store account information
        accountBalances.put(accountNumber, initialDeposit);
        accountHolders.put(accountNumber, accountHolder);
        
        System.out.println("[CREATE] Account " + accountNumber + " created for " + accountHolder);
        return accountNumber;
    }
    
    @Override
    public boolean accountExists(String accountNumber) throws RemoteException {
        return accountBalances.containsKey(accountNumber);
    }

    // ========== TRANSACTIONS ==========
    
    @Override
    public synchronized String deposit(String accountNumber, double amount) throws RemoteException {
        // Validate account
        if (!accountExists(accountNumber)) {
            return "Error: Account " + accountNumber + " not found.";
        }
        
        // Validate amount
        if (amount <= 0) {
            return "Error: Deposit amount must be positive.";
        }
        
        // Perform deposit
        double currentBalance = accountBalances.get(accountNumber);
        double newBalance = currentBalance + amount;
        accountBalances.put(accountNumber, newBalance);
        
        System.out.println("[DEPOSIT] " + accountNumber + " deposited " + amount + " | New Balance: " + newBalance);
        return String.format("Deposited %.2f. New balance: %.2f", amount, newBalance);
    }
    
    @Override
    public synchronized String withdraw(String accountNumber, double amount) throws RemoteException {
        // Validate account
        if (!accountExists(accountNumber)) {
            return "Error: Account " + accountNumber + " not found.";
        }
        
        // Validate amount
        if (amount <= 0) {
            return "Error: Withdrawal amount must be positive.";
        }
        
        // Check sufficient balance
        double currentBalance = accountBalances.get(accountNumber);
        if (currentBalance < amount) {
            return "Error: Insufficient balance. Current balance: " + currentBalance;
        }
        
        // Perform withdrawal
        double newBalance = currentBalance - amount;
        accountBalances.put(accountNumber, newBalance);
        
        System.out.println("[WITHDRAW] " + accountNumber + " withdrew " + amount + " | New Balance: " + newBalance);
        return String.format("Withdrew %.2f. New balance: %.2f", amount, newBalance);
    }
    
    @Override
    public synchronized String transfer(String fromAccount, String toAccount, double amount) throws RemoteException {
        // Validate both accounts
        if (!accountExists(fromAccount)) {
            return "Error: Source account " + fromAccount + " not found.";
        }
        if (!accountExists(toAccount)) {
            return "Error: Destination account " + toAccount + " not found.";
        }
        
        // Validate amount
        if (amount <= 0) {
            return "Error: Transfer amount must be positive.";
        }
        
        // Check sufficient balance
        double fromBalance = accountBalances.get(fromAccount);
        if (fromBalance < amount) {
            return "Error: Insufficient balance in source account.";
        }
        
        // Perform transfer
        accountBalances.put(fromAccount, fromBalance - amount);
        accountBalances.put(toAccount, accountBalances.get(toAccount) + amount);
        
        System.out.println("[TRANSFER] " + fromAccount + " -> " + toAccount + " Amount: " + amount);
        return String.format("Transferred %.2f from %s to %s", amount, fromAccount, toAccount);
    }

    // ========== INQUIRY ==========
    
    @Override
    public double getBalance(String accountNumber) throws RemoteException {
        if (!accountExists(accountNumber)) {
            return -1; // Account not found
        }
        return accountBalances.get(accountNumber);
    }
    
    @Override
    public String getAccountDetails(String accountNumber) throws RemoteException {
        if (!accountExists(accountNumber)) {
            return "Error: Account " + accountNumber + " not found.";
        }
        
        String holder = accountHolders.get(accountNumber);
        double balance = accountBalances.get(accountNumber);
        
        return String.format("Account: %s | Holder: %s | Balance: %.2f", 
                            accountNumber, holder, balance);
    }
}
