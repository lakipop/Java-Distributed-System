package TCPChat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ============================================================
 * TCP CHAT CLIENT
 * ============================================================
 * Client application for connecting to TCP chat server.
 * Features:
 *   - Connect to chat server
 *   - Send messages to other clients
 *   - Receive messages from other clients in real-time
 *   - Separate threads for sending and receiving
 * ============================================================
 */
public class TCPChatClient {

    // ========== CLIENT CONFIGURATION ==========
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5002;
    
    // ========== CLIENT STATE ==========
    private static volatile boolean running = true;

    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   TCP CHAT CLIENT");
        System.out.println("============================================");
        
        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to chat server!");
            
            // ========== MESSAGE RECEIVER THREAD ==========
            Thread receiverThread = new Thread(() -> {
                try {
                    String message;
                    while (running && (message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Connection lost: " + e.getMessage());
                    }
                }
            });
            receiverThread.start();
            
            // ========== MESSAGE SENDER LOOP ==========
            String userInput;
            while (running) {
                userInput = scanner.nextLine();
                out.println(userInput);
                
                // Check for exit command
                if (userInput.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
            }
            
            System.out.println("Disconnected from chat server.");
            
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
