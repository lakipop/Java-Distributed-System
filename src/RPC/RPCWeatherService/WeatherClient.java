package RPC.RPCWeatherService;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ============================================================
 * RPC WEATHER CLIENT
 * ============================================================
 * Client that requests weather data through RPC-style calls.
 * Demonstrates Remote Procedure Call pattern using Java sockets.
 * 
 * Features:
 *   - Get weather for a specific city
 *   - List all available cities
 *   - Interactive menu-driven interface
 * ============================================================
 */
public class WeatherClient {

    // ========== CLIENT CONFIGURATION ==========
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5001;

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   RPC WEATHER CLIENT");
        System.out.println("============================================");
        
        Scanner scanner = new Scanner(System.in);
        
        // ========== MAIN CLIENT LOOP ==========
        while (true) {
            // Display menu
            System.out.println("\n--- Menu ---");
            System.out.println("1. Get Weather for City");
            System.out.println("2. List All Cities");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    // Get weather for specific city
                    System.out.print("Enter city name: ");
                    String city = scanner.nextLine();
                    getWeather(city);
                    break;
                    
                case 2:
                    // List all available cities
                    getAllCities();
                    break;
                    
                case 3:
                    // Exit the client
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    // ========== RPC: GET WEATHER ==========
    private static void getWeather(String city) {
        // Create RPC request for getWeather method
        RPCRequest request = new RPCRequest("getWeather", new Object[]{city});
        
        // Send request and process response
        RPCResponse response = sendRPCRequest(request);
        
        if (response != null) {
            if (response.isSuccess()) {
                WeatherData weather = (WeatherData) response.getResult();
                System.out.println("\n" + weather);
            } else {
                System.out.println("Error: " + response.getErrorMessage());
            }
        }
    }

    // ========== RPC: GET ALL CITIES ==========
    private static void getAllCities() {
        // Create RPC request for getAllCities method
        RPCRequest request = new RPCRequest("getAllCities", null);
        
        // Send request and process response
        RPCResponse response = sendRPCRequest(request);
        
        if (response != null) {
            if (response.isSuccess()) {
                String[] cities = (String[]) response.getResult();
                System.out.println("\nAvailable Cities:");
                for (String city : cities) {
                    System.out.println("  - " + city);
                }
            } else {
                System.out.println("Error: " + response.getErrorMessage());
            }
        }
    }

    // ========== RPC REQUEST SENDER ==========
    private static RPCResponse sendRPCRequest(RPCRequest request) {
        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // Send the RPC request
            out.writeObject(request);
            out.flush();
            
            // Receive and return the response
            return (RPCResponse) in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection error: " + e.getMessage());
            return null;
        }
    }
}
