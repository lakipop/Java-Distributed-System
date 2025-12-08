package RMIHelloWorld;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServer {
    public static void main(String args[]) throws RemoteException {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            RMI_Implementation impl = new RMI_Implementation();
            Naming.rebind("HelloServer", impl);
            System.out.println("Server ready");


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
