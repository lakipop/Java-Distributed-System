package RMIBankService;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * ============================================================
 * RMI BANK SERVICE INTERFACE
 * ============================================================
 * Remote interface defining bank operations.
 * All methods can be called remotely by clients.
 * 
 * Operations:
 *   - createAccount: Create new bank account
 *   - deposit: Add money to account
 *   - withdraw: Remove money from account
 *   - getBalance: Check account balance
 *   - transfer: Transfer between accounts
 * ============================================================
 */
public interface BankInterface extends Remote {

    // ========== ACCOUNT MANAGEMENT ==========
    
    /**
     * Create a new bank account
     * @param accountHolder Name of the account holder
     * @param initialDeposit Initial deposit amount
     * @return Account number
     */
    String createAccount(String accountHolder, double initialDeposit) throws RemoteException;
    
    /**
     * Check if account exists
     * @param accountNumber Account number to check
     * @return true if account exists
     */
    boolean accountExists(String accountNumber) throws RemoteException;

    // ========== TRANSACTIONS ==========
    
    /**
     * Deposit money into account
     * @param accountNumber Target account
     * @param amount Amount to deposit
     * @return Success message
     */
    String deposit(String accountNumber, double amount) throws RemoteException;
    
    /**
     * Withdraw money from account
     * @param accountNumber Source account
     * @param amount Amount to withdraw
     * @return Success message or error
     */
    String withdraw(String accountNumber, double amount) throws RemoteException;
    
    /**
     * Transfer money between accounts
     * @param fromAccount Source account
     * @param toAccount Destination account
     * @param amount Amount to transfer
     * @return Success message or error
     */
    String transfer(String fromAccount, String toAccount, double amount) throws RemoteException;

    // ========== INQUIRY ==========
    
    /**
     * Get current balance
     * @param accountNumber Account to check
     * @return Current balance
     */
    double getBalance(String accountNumber) throws RemoteException;
    
    /**
     * Get account details
     * @param accountNumber Account to check
     * @return Account information string
     */
    String getAccountDetails(String accountNumber) throws RemoteException;
}
