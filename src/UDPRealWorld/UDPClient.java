package UDPRealWorld;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) throws IOException {

        // Create UDP socket
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");

        // Get item code from user
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Item");
        String itemCode = in.nextLine();

        // Send item code to server
        byte[] sendData = itemCode.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 1099);
        socket.send(sendPacket);

       //received
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        //display
        String receivedItem = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Item "+itemCode+" received "+receivedItem);

        /// clear and close
        socket.close();
        in.close();

    }
}
