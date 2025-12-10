package TCPChat;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ============================================================
 * TCP CHAT SERVER - MULTI-CLIENT SUPPORT
 * ============================================================
 * Central server that manages multiple chat clients.
 * Features:
 *   - Accepts multiple client connections
 *   - Broadcasts messages to all connected clients
 *   - Handles client join/leave notifications
 *   - Thread-safe client management
 * ============================================================
 */
public class TCPChatServer {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 5002;
    
    // ========== CONNECTED CLIENTS LIST ==========
    // Thread-safe list to store all connected client handlers
    private static List<ClientHandler> connectedClients = Collections.synchronizedList(new ArrayList<>());

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   TCP CHAT SERVER - MULTI-CLIENT");
        System.out.println("============================================");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);
            System.out.println("Waiting for clients to connect...");
            System.out.println("--------------------------------------------");
            
            // ========== MAIN SERVER LOOP ==========
            while (true) {
                // Accept new client connection
                Socket clientSocket = serverSocket.accept();
                
                // Create handler for this client
                ClientHandler clientHandler = new ClientHandler(clientSocket, connectedClients);
                connectedClients.add(clientHandler);
                
                // Start client handler in new thread
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                
                System.out.println("New client connected. Total clients: " + connectedClients.size());
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== BROADCAST MESSAGE TO ALL CLIENTS ==========
    public static void broadcastMessage(String message, ClientHandler sender) {
        synchronized (connectedClients) {
            for (ClientHandler client : connectedClients) {
                // Don't send message back to sender
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    // ========== REMOVE CLIENT ==========
    public static void removeClient(ClientHandler client) {
        connectedClients.remove(client);
        System.out.println("Client disconnected. Total clients: " + connectedClients.size());
    }
}
