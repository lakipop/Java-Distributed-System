package RMICalculator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalServer {
    public static void main(String[] args) throws RemoteException {

        Registry registry = LocateRegistry.createRegistry(1099);

        RMICalImplement obj = new RMICalImplement();
        try {
            Naming.rebind("calServer", obj);
            System.out.println("Server ready");

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
