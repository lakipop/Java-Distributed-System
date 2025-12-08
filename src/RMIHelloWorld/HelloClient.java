package RMIHelloWorld;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class HelloClient {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        try {
            RMI_Interface stub = (RMI_Interface) Naming.lookup("rmi://localhost:1099/HelloServer");

            String hello = stub.hello("Laki");
            System.out.println(hello);

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
