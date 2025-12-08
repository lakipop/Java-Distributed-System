package TCPClientServerCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {

        try {
            ServerSocket serverSocket = new ServerSocket(1099);
            System.out.println("Server started");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("Client received: " + message);
                out.println("Hello from Server");

                socket.close();
                serverSocket.close();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
