package RMICalculator;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMICalInterface extends Remote {

    public int add(int a, int b) throws RemoteException;
    public int sub(int a, int b) throws RemoteException;
    public int mul(int a, int b) throws RemoteException;
    public int div(int a, int b) throws RemoteException;
    public int mod(int a, int b) throws RemoteException;

}
