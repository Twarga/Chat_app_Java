import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.*;

public class ChatClient extends UnicastRemoteObject implements Client {
    private ChatService chatService;
    private String name;
    private ChatClientGUI gui;

    public ChatClient(String name) throws RemoteException {
        this.name = name;
        try {
            chatService = (ChatService) Naming.lookup("rmi://localhost/ChatService");
            gui = new ChatClientGUI(this);
            chatService.registerClient(this, name);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void sendMessage(String message) throws RemoteException {
        chatService.sendMessage(message, name);
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        gui.receiveMessage(message);
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        try {
            new ChatClient(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
