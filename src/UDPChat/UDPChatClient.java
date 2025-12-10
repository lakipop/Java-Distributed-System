package UDPChat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ============================================================
 * UDP CHAT CLIENT
 * ============================================================
 * Client application for connectionless UDP chat.
 * Features:
 *   - Send messages via UDP protocol
 *   - Receive messages from server
 *   - Fast, lightweight messaging
 *   - No persistent connection required
 * ============================================================
 */
public class UDPChatClient {

    // ========== CLIENT CONFIGURATION ==========
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5003;
    private static final int BUFFER_SIZE = 1024;
    
    // ========== CLIENT STATE ==========
    private static volatile boolean running = true;

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   UDP CHAT CLIENT");
        System.out.println("============================================");
        
        Scanner scanner = new Scanner(System.in);
        
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            
            // ========== GET USERNAME ==========
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            
            // Send join message to server
            sendMessage(socket, serverAddress, "JOIN:" + username);
            
            System.out.println("Connected! Type your messages (type 'exit' to leave):");
            System.out.println("--------------------------------------------");
            
            // ========== MESSAGE RECEIVER THREAD ==========
            Thread receiverThread = new Thread(() -> {
                byte[] receiveBuffer = new byte[BUFFER_SIZE];
                
                while (running) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        socket.receive(receivePacket);
                        
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(message);
                        
                        // Clear buffer
                        java.util.Arrays.fill(receiveBuffer, (byte) 0);
                        
                    } catch (IOException e) {
                        if (running) {
                            System.err.println("Receive error: " + e.getMessage());
                        }
                    }
                }
            });
            receiverThread.start();
            
            // ========== MESSAGE SENDER LOOP ==========
            while (running) {
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("exit")) {
                    // Send leave message
                    sendMessage(socket, serverAddress, "LEAVE");
                    running = false;
                    break;
                }
                
                // Send regular message
                sendMessage(socket, serverAddress, input);
            }
            
            System.out.println("Disconnected from chat.");
            
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // ========== SEND MESSAGE TO SERVER ==========
    private static void sendMessage(DatagramSocket socket, InetAddress serverAddress, String message) 
            throws IOException {
        byte[] sendBuffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(
            sendBuffer, 
            sendBuffer.length, 
            serverAddress, 
            SERVER_PORT
        );
        socket.send(sendPacket);
    }
}
