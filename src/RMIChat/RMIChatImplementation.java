package RMIChat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIChatImplementation extends UnicastRemoteObject implements RMIChatInterface {
    protected RMIChatImplementation() throws RemoteException {
        super();
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        return "Sent message: " + message;
    }
}
