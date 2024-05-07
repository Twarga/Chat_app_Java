import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    void sendMessage(String message, String sender) throws RemoteException;
    void registerClient(Client client, String name) throws RemoteException;
    void unregisterClient(Client client) throws RemoteException;
}
