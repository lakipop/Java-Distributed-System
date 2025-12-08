package RMIHelloWorld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMI_Implementation  extends UnicastRemoteObject implements RMI_Interface  {
    public RMI_Implementation() throws RemoteException {
        super();
    }
    public String hello(String name) throws RemoteException {
        return "Hello " + name;
    }

}
