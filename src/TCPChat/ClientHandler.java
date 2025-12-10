package TCPChat;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * ============================================================
 * CLIENT HANDLER - INDIVIDUAL CLIENT THREAD
 * ============================================================
 * Handles communication with a single connected client.
 * Runs in its own thread to allow concurrent client handling.
 * 
 * Responsibilities:
 *   - Receive messages from assigned client
 *   - Broadcast received messages to other clients
 *   - Handle client disconnection
 * ============================================================
 */
public class ClientHandler implements Runnable {

    // ========== CLIENT ATTRIBUTES ==========
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private List<ClientHandler> allClients;

    // ========== CONSTRUCTOR ==========
    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.clientSocket = socket;
        this.allClients = clients;
        
        try {
            // Initialize input/output streams
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error initializing streams: " + e.getMessage());
        }
    }

    // ========== MAIN RUN METHOD ==========
    @Override
    public void run() {
        try {
            // ========== GET CLIENT NAME ==========
            out.println("Enter your name: ");
            clientName = in.readLine();
            
            // Announce new user to all clients
            String joinMessage = "[SERVER] " + clientName + " has joined the chat!";
            System.out.println(joinMessage);
            TCPChatServer.broadcastMessage(joinMessage, this);
            
            out.println("Welcome " + clientName + "! You can start chatting now.");
            out.println("Type 'exit' to leave the chat.");
            
            // ========== MESSAGE RECEIVING LOOP ==========
            String message;
            while ((message = in.readLine()) != null) {
                // Check for exit command
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                
                // Format and broadcast message
                String formattedMessage = "[" + clientName + "]: " + message;
                System.out.println(formattedMessage);
                TCPChatServer.broadcastMessage(formattedMessage, this);
            }
            
        } catch (IOException e) {
            System.err.println("Connection error with " + clientName + ": " + e.getMessage());
        } finally {
            // ========== CLEANUP ON DISCONNECT ==========
            handleDisconnect();
        }
    }

    // ========== SEND MESSAGE TO THIS CLIENT ==========
    public void sendMessage(String message) {
        out.println(message);
    }

    // ========== HANDLE CLIENT DISCONNECTION ==========
    private void handleDisconnect() {
        try {
            // Announce departure
            String leaveMessage = "[SERVER] " + clientName + " has left the chat.";
            System.out.println(leaveMessage);
            TCPChatServer.broadcastMessage(leaveMessage, this);
            
            // Remove from client list
            TCPChatServer.removeClient(this);
            
            // Close resources
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
