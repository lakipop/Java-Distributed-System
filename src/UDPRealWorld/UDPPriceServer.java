package UDPRealWorld;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class UDPPriceServer {

    static HashMap<String,Double> ItemPrice =new HashMap<>();
    static HashMap<String,Double> ItemDiscount =new HashMap<>();

    static {
        ItemPrice.put("Apple",100.0);
        ItemPrice.put("Banana",200.0);
        ItemPrice.put("Orange",300.0);
        ItemPrice.put("Grapes",400.0);
        ItemPrice.put("Potatoes",250.0);

        ItemDiscount.put("Apple",10.0);
        ItemDiscount.put("Banana",20.0);
        ItemDiscount.put("Orange",30.0);
        ItemDiscount.put("Grapes",30.0);
        ItemDiscount.put("Potatoes",20.0);
    }

    public static void main(String[] args) throws SocketException {

        try {

            //create server socket
            DatagramSocket socket = new DatagramSocket(1099);
            System.out.println("Server started");

            while (true) {
                //received request
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String item = new String(packet.getData());
                System.out.println(item);

                if(ItemPrice.containsKey(item)&&ItemDiscount.containsKey(item)) {
                    double price = ItemPrice.get(item);
                    double discount = ItemDiscount.get(item);
                    double finalPrice = price - (price * discount / 100);

                    String response = "price" + price + " discount " + discount + " finalPrice " + finalPrice;

                    byte[] responseBytes = response.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(responseBytes, responseBytes.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                }
                else {
                    System.out.println("No item found");
                }

                socket.close();
                System.out.println("Server stopped");

            }


        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
