package UDPChat;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ============================================================
 * UDP CHAT SERVER
 * ============================================================
 * Connectionless chat server using UDP protocol.
 * Features:
 *   - No persistent connections (UDP is connectionless)
 *   - Tracks active clients by their socket addresses
 *   - Broadcasts messages to all registered clients
 *   - Lightweight and fast message delivery
 * ============================================================
 */
public class UDPChatServer {

    // ========== SERVER CONFIGURATION ==========
    private static final int PORT = 5003;
    private static final int BUFFER_SIZE = 1024;
    
    // ========== REGISTERED CLIENTS ==========
    // Map of client addresses to their usernames
    private static Map<InetSocketAddress, String> clients = new HashMap<>();

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   UDP CHAT SERVER");
        System.out.println("============================================");
        
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("UDP Chat server started on port " + PORT);
            System.out.println("Waiting for messages...");
            System.out.println("--------------------------------------------");
            
            byte[] receiveBuffer = new byte[BUFFER_SIZE];
            
            // ========== MAIN SERVER LOOP ==========
            while (true) {
                // Create packet to receive data
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);
                
                // Extract client address and message
                InetSocketAddress clientAddress = new InetSocketAddress(
                    receivePacket.getAddress(), 
                    receivePacket.getPort()
                );
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                
                // Process the message
                processMessage(serverSocket, clientAddress, message);
                
                // Clear buffer
                Arrays.fill(receiveBuffer, (byte) 0);
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== MESSAGE PROCESSOR ==========
    private static void processMessage(DatagramSocket socket, InetSocketAddress clientAddress, String message) 
            throws IOException {
        
        // ========== HANDLE JOIN COMMAND ==========
        if (message.startsWith("JOIN:")) {
            String username = message.substring(5);
            clients.put(clientAddress, username);
            System.out.println("[JOIN] " + username + " has joined the chat.");
            
            // Send welcome message
            sendToClient(socket, clientAddress, "Welcome " + username + "! You are now connected.");
            
            // Broadcast join notification
            broadcastMessage(socket, clientAddress, "[SERVER] " + username + " has joined the chat!");
            return;
        }
        
        // ========== HANDLE LEAVE COMMAND ==========
        if (message.equalsIgnoreCase("LEAVE")) {
            String username = clients.getOrDefault(clientAddress, "Unknown");
            clients.remove(clientAddress);
            System.out.println("[LEAVE] " + username + " has left the chat.");
            
            // Broadcast leave notification
            broadcastMessage(socket, clientAddress, "[SERVER] " + username + " has left the chat.");
            return;
        }
        
        // ========== HANDLE REGULAR MESSAGE ==========
        String username = clients.getOrDefault(clientAddress, "Anonymous");
        String formattedMessage = "[" + username + "]: " + message;
        System.out.println(formattedMessage);
        
        // Broadcast to all other clients
        broadcastMessage(socket, clientAddress, formattedMessage);
    }

    // ========== SEND MESSAGE TO SPECIFIC CLIENT ==========
    private static void sendToClient(DatagramSocket socket, InetSocketAddress client, String message) 
            throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(
            sendBuffer, 
            sendBuffer.length, 
            client.getAddress(), 
            client.getPort()
        );
        socket.send(sendPacket);
    }

    // ========== BROADCAST MESSAGE TO ALL CLIENTS ==========
    private static void broadcastMessage(DatagramSocket socket, InetSocketAddress sender, String message) 
            throws IOException {
        for (InetSocketAddress client : clients.keySet()) {
            // Don't send to the sender
            if (!client.equals(sender)) {
                sendToClient(socket, client, message);
            }
        }
    }
}
