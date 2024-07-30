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
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        
        clientNameField = new JTextField(10);
        serverAddressField = new JTextField(10);
        messageField = new JTextField(20);
        messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        
        jScrollPane = new JScrollPane(messageArea);

        connectButton = new JButton("Connect");
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");

        connectLabel = new JLabel("Connect:");
        disconnectLabel = new JLabel("Disconnect:");
        sendLabel = new JLabel("Enter Message:");

        jPanel1.add(new JLabel("Username:"));
        jPanel1.add(clientNameField);
        jPanel1.add(new JLabel("Server Address:"));
        jPanel1.add(serverAddressField);
        jPanel1.add(connectButton);

        jPanel2.add(sendLabel);
        jPanel2.add(messageField);
        jPanel3.add(sendButton);
        jPanel4.add(disconnectLabel);
        jPanel4.add(disconnectButton);
        jPanel5.add(jScrollPane);

        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.add(jPanel4);
        this.add(jPanel5);

        this.setLayout(new java.awt.GridLayout(5, 1));
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connect();
            }
        });

        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage();
            }
        });

        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendDisconnect();
                disconnect();
            }
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
