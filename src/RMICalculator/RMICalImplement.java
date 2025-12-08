package RMICalculator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMICalImplement extends UnicastRemoteObject implements RMICalInterface {

    public RMICalImplement () throws RemoteException {
        super();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return  a + b;
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        return  a - b;
    }

    @Override
    public int mul(int a, int b) throws RemoteException {
        return  a * b;
    }

    @Override
    public int div(int a, int b) throws RemoteException, ArithmeticException {
        return  a / b;
    }

    @Override
    public int mod(int a, int b) throws RemoteException {
        return  a % b;
    }
}
