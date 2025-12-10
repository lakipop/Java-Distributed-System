package RMIBankService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ============================================================
 * RMI BANK SERVER
 * ============================================================
 * Server that hosts the Bank RMI service.
 * Registers the Bank implementation with the RMI registry.
 * 
 * Usage:
 *   1. Run this server first
 *   2. Then run BankClient to connect
 * ============================================================
 */
public class BankServer {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 1100;
    private static final String SERVICE_NAME = "BankService";

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RMI BANK SERVER");
        System.out.println("============================================");
        
        try {
            // ========== CREATE RMI REGISTRY ==========
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("RMI Registry created on port " + PORT);
            
            // ========== CREATE AND BIND SERVICE ==========
            BankImplementation bankService = new BankImplementation();
            Naming.rebind("rmi://localhost:" + PORT + "/" + SERVICE_NAME, bankService);
            
            System.out.println("Bank Service bound to: " + SERVICE_NAME);
            System.out.println("--------------------------------------------");
            System.out.println("Server is ready and waiting for clients...");
            
        } catch (RemoteException e) {
            System.err.println("Remote error: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println("URL error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
