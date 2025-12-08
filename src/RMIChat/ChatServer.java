package RMIChat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServer {
    public static void main(String args[]) throws RemoteException, MalformedURLException {

        Registry registry = LocateRegistry.createRegistry(1099);
        RMIChatImplementation chat = new RMIChatImplementation();
        try {
            Naming.rebind("rmi://localhost:1099/chat", chat);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Chat Server ready");

    }
}
