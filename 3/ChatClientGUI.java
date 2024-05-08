import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ChatClientGUI extends JFrame {
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private ChatClient client;
    private Map<String, Color> userColors; // Store user names and their associated colors
    private static final Color DEFAULT_CHAT_COLOR = Color.BLACK; // Default color for chat messages

    public ChatClientGUI(ChatClient client) {
        this.client = client;
        setTitle("Chat Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel footerLabel = new JLabel("By Youssef Nassime");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.NORTH);

        setVisible(true);

        userColors = new HashMap<>();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                client.sendMessage(message);
                messageField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error sending message: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void receiveMessage(String message, String sender) {
        Color usernameColor = getUserColor(sender); // Get color based on sender's username
        if (usernameColor != null) {
            receiveMessage(message, sender, usernameColor);
        } else {
            receiveMessage(message, sender, Color.BLUE); // Use default color (blue) if user color is not set
        }
    }

    public void receiveMessage(String message, String sender, Color usernameColor) {
        StyledDocument doc = chatArea.getStyledDocument();

        SimpleAttributeSet usernameAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(usernameAttr, usernameColor);

        SimpleAttributeSet chatAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(chatAttr, DEFAULT_CHAT_COLOR);

        try {
            doc.insertString(doc.getLength(), sender + ":", usernameAttr); // Insert username with color
            doc.insertString(doc.getLength(), " " + message + "\n", chatAttr); // Insert message with default chat color
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the color associated with a user, or null if not found
    private Color getUserColor(String user) {
        return userColors.get(user);
    }

    // Method to set the color for a user
    public void setUserColor(String user, Color color) {
        userColors.put(user, color);
    }

    // Method to prompt the user to choose a color
    public String promptForColor() {
        String[] colors = {"Red", "Green", "Blue"};
        int choice = JOptionPane.showOptionDialog(this, "Choose a color for your username:", "Color Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, colors, colors[0]);
        if (choice >= 0 && choice < colors.length) {
            return colors[choice].toLowerCase(); // Return lowercase color name
        } else {
            return null; // User canceled
        }
    }
}
