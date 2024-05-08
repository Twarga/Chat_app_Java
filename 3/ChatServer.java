import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

public class ChatServer extends UnicastRemoteObject implements ChatService {
    private Map<Client, String> clients;

    public ChatServer() throws RemoteException {
        clients = new HashMap<>();
    }

    @Override
    public synchronized void sendMessage(String message, String sender) throws RemoteException {
        for (Client client : clients.keySet()) {
            client.receiveMessage(message, sender, Color.BLACK); // You can adjust the color as needed
        }
    }

    @Override
    public synchronized void registerClient(Client client, String name) throws RemoteException {
        clients.put(client, name);
        sendMessage(name + " has joined the chat.", "Server");
    }

    @Override
    public synchronized void unregisterClient(Client client) throws RemoteException {
        String name = clients.get(client);
        clients.remove(client);
        sendMessage(name + " has left the chat.", "Server");
    }

    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            ChatService server = new ChatServer();
            java.rmi.Naming.rebind("ChatService", server);
            System.out.println("Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
