package RMIChat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIChatInterface  extends Remote {

    String sendMessage(String message) throws RemoteException;

}
