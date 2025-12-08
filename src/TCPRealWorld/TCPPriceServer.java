package TCPRealWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPPriceServer {

    static HashMap<String, Double> prices = new HashMap<>();
    static  HashMap<String, Double> discounts = new HashMap<>();

    static  {
        prices.put("Item1", 100.00);
        prices.put("Item2", 200.00);
        prices.put("Item3", 350.00);
        prices.put("Item4", 1000.00);
        prices.put("Item5", 1250.00);

        discounts.put("Item1", 10.00);
        discounts.put("Item2", 20.00);
        discounts.put("Item3", 10.00);
        discounts.put("Item4", 30.00);
        discounts.put("Item5", 25.00);
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1099);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String item = in.readLine();
            System.out.println("Request Item "+item);

            if(prices.containsKey(item) && discounts.containsKey(item)) {
                double price = prices.get(item);
                double discount = discounts.get(item);
                double finalPrice = price - ((price * discount)/100);

                System.out.println("Item "+item+" price "+price+" discount "+discount +" finalPrice "+finalPrice);
                out.println("Item "+item+" price "+price+" discount "+discount +" finalPrice "+finalPrice);

            }
            else {
                out.println("Invalid item");
            }

            socket.close();
            System.out.println("Closing connection");

        }

    }

}
