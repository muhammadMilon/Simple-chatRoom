import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class Client_Side extends JFrame {

    JTextField messageField, clientNameField, serverAddressField;
    JTextArea messageArea;
    JScrollPane jScrollPane;
    JLabel connectLabel, disconnectLabel, sendLabel;
    JPanel jPanel1, jPanel2, jPanel3, jPanel4, jPanel5;
    JButton connectButton, sendButton, disconnectButton;

    String username, address;
    ArrayList<String> users = new ArrayList<>();
    boolean isConnected = false;

    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public Client_Side() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new JPanel(new GridBagLayout());
        jPanel2 = new JPanel(new GridBagLayout());
        jPanel4 = new JPanel(new GridBagLayout());
        jPanel5 = new JPanel(new GridBagLayout());
        
        clientNameField = new JTextField(10);
        serverAddressField = new JTextField(10);
        messageField = new JTextField(30);
        messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        
        jScrollPane = new JScrollPane(messageArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        connectButton = new JButton("Connect");
        connectButton.setBackground(Color.green);
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(Color.RED);

        connectLabel = new JLabel("Connect:");
        disconnectLabel = new JLabel("Disconnect:");
        sendLabel = new JLabel("Enter Message:");

        // Layout for connection panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        jPanel1.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        jPanel1.add(clientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanel1.add(new JLabel("Server Address:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel1.add(serverAddressField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        jPanel1.add(connectButton, gbc);

        // Layout for disconnect button
        gbc.gridx = 0;
        gbc.gridy = 0;
        jPanel4.add(disconnectLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        jPanel4.add(disconnectButton, gbc);

        // Layout for message input and send button
        gbc.gridx = 0;
        gbc.gridy = 0;
        jPanel2.add(sendLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanel2.add(messageField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        jPanel2.add(sendButton, gbc);

        // Layout for the message area with scroll pane
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        jPanel5.add(jScrollPane, gbc);

        // Add all panels to the frame
        this.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(jPanel1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(jPanel4, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(jPanel2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(jPanel5, gbc);

        this.setSize(600, 600); // Adjusted size for better layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        connectButton.addActionListener(evt -> connect());

        sendButton.addActionListener(evt -> sendMessage());

        disconnectButton.addActionListener(evt -> {
            sendDisconnect();
            disconnect();
        });
    }

    private void connect() {
        if (!isConnected) {
            username = clientNameField.getText();
            address = serverAddressField.getText();
            if (username.isEmpty() || address.isEmpty()) {
                messageArea.append("Username and Server Address must be filled.\n");
                return;
            }

            try {
                socket = new Socket(address, 5000);
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(streamReader);
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(username + ":has connected.:Connect");
                isConnected = true;
                clientNameField.setEditable(false);
                serverAddressField.setEditable(false);
                messageArea.append("Connected to server.\n");
                ListenThread();
            } catch (Exception e) {
                messageArea.append("Cannot connect! Try again.\n");
                clientNameField.setEditable(true);
                serverAddressField.setEditable(true);
            }
        } else {
            messageArea.append("You are already connected.\n");
        }
    }

    public void ListenThread() {
        Thread incomingThread = new Thread(new IncomingReader());
        incomingThread.start();
    }

    public void userAdd(String user) {
        users.add(user);
    }

    public void userRemove(String user) {
        messageArea.append((user + " is now offline.\n"));
    }

    public void writeUsername() {
        String[] listStrings = new String[users.size()];
        users.toArray(listStrings);
        for (@SuppressWarnings("unused") String token : listStrings) {
            // Handle user list display if needed
        }
    }

    public void sendDisconnect() {
        String bye = (username + ":Disconnect");
        try {
            printWriter.println(bye);
            printWriter.flush();
        } catch (Exception e) {
            messageArea.append("You are not connected.\n");
        }
    }

    public void disconnect() {
        try {
            messageArea.append("Disconnected.\n");
            socket.close();
            isConnected = false;
            clientNameField.setEditable(true);
            serverAddressField.setEditable(true);
        } catch (Exception e) {
            messageArea.append("Failed to disconnect.\n");
        }
    }

    public void sendMessage() {
        String nothing = "";
        if ((messageField.getText()).equals(nothing)) {
            messageField.setText("");
            messageField.requestFocus();
        } else {
            try {
                printWriter.println(username + ":" + messageField.getText() + ":Chat");
                printWriter.flush();
            } catch (Exception ex) {
                messageArea.append("Message was not sent.\n");
            }
            messageField.setText("");
            messageField.requestFocus();
        }
    }

    public class IncomingReader implements Runnable {
        @Override
        public void run() {
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

            try {
                while ((stream = bufferedReader.readLine()) != null) {
                    data = stream.split(":");

                    if (data[2].equals(chat)) {
                        messageArea.append(data[0] + ": " + data[1] + "\n");
                        messageArea.setCaretPosition(messageArea.getDocument().getLength());
                    } else if (data[2].equals(connect)) {
                        messageArea.removeAll();
                        userAdd(data[0]);
                    } else if (data[2].equals(disconnect)) {
                        userRemove(data[0]);
                    } else if (data[2].equals(done)) {
                        writeUsername();
                        users.clear();
                    }
                }
           } catch (Exception ex) {
                ex.printStackTrace();
           }
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client_Side().setVisible(true);
            }
        });
    }
}
