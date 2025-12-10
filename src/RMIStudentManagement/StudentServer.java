package RMIStudentManagement;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * ============================================================
 * RMI STUDENT MANAGEMENT SERVER
 * ============================================================
 * Server that hosts the Student Management RMI service.
 * Registers the StudentImplementation with the RMI registry.
 * 
 * Usage:
 *   1. Run this server first
 *   2. Then run StudentClient to connect
 * ============================================================
 */
public class StudentServer {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 1101;
    private static final String SERVICE_NAME = "StudentService";

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RMI STUDENT MANAGEMENT SERVER");
        System.out.println("============================================");
        
        try {
            // ========== CREATE RMI REGISTRY ==========
            Registry registry = LocateRegistry.createRegistry(PORT);
            System.out.println("RMI Registry created on port " + PORT);
            
            // ========== CREATE AND BIND SERVICE ==========
            StudentImplementation studentService = new StudentImplementation();
            Naming.rebind("rmi://localhost:" + PORT + "/" + SERVICE_NAME, studentService);
            
            System.out.println("Student Service bound to: " + SERVICE_NAME);
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
