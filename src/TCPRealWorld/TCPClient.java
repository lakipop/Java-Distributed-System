package TCPRealWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost",1099);
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Item Code");
        String itemCode = sc.nextLine();

        out.println(itemCode);
        out.flush();
        String response = in.readLine();
        System.out.println(response);
        socket.close();
        sc.close();
        
    }
}
