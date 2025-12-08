package RMIChat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        try {
            RMIChatInterface stub = (RMIChatInterface) Naming.lookup("rmi://localhost/chat");
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter your message: ");
            String message = sc.nextLine();
            String response = stub.sendMessage(message);
            System.out.println(response);



        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
