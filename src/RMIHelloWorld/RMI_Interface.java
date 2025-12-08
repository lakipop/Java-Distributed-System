package RMIHelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_Interface extends Remote {
    public String hello(String name) throws RemoteException;
}
