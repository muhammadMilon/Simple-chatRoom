import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Server_Side extends JFrame {
    JTextArea jTextArea;
    JLabel jLabel2, jLabel3, jLabel4, jLabel5, server;
    JPanel jPanel1, jPanel2, jPanel3, jPanel4, jPanel5;
    JScrollPane jScrollPane;

    Main OOP = new Main();
    ArrayList<PrintWriter> clientOutputStreams;
    ArrayList<String> users;
    ServerSocket serverSocket;
    boolean isRunning = false;

    public Server_Side() {
        // Initialize GUI components
        initComponents();

        // Initialize the client output streams and users list
        clientOutputStreams = new ArrayList<>();
        users = new ArrayList<>();
    }

    private void initComponents() {
        jTextArea = new JTextArea(15, 50);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jTextArea);

        jLabel2 = new JLabel("Start Server");
        jLabel3 = new JLabel("Stop Server");
        jLabel4 = new JLabel("Online Users");
        jLabel5 = new JLabel("Clear Screen");
        server = new JLabel("Server");

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();

        jPanel1.add(server);
        jPanel2.add(jLabel2);
        jPanel3.add(jLabel3);
        jPanel4.add(jLabel4);
        jPanel5.add(jLabel5);

        this.setLayout(new java.awt.GridLayout(6, 1));
        this.add(jPanel1);
        this.add(jPanel2);
        this.add(jPanel3);
        this.add(jPanel4);
        this.add(jPanel5);
        this.add(scrollPane);

        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited();
            }
        });

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited();
            }
        });

        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited();
            }
        });

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited();
            }
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server_Side().setVisible(true);
            }
        });
    }

    public void startServer() {
        if (!isRunning) {
            isRunning = true;
            Thread starter = new Thread(new StartServer());
            starter.start();
        }
    }

    public void stopServer() {
        if (isRunning) {
            isRunning = false;
            try {
                serverSocket.close();
                for (PrintWriter writer : clientOutputStreams) {
                    writer.close();
                }
                clientOutputStreams.clear();
                users.clear();
                jTextArea.append("Server stopped.\n");
            } catch (Exception e) {
                jTextArea.append("Error stopping server: " + e.getMessage() + "\n");
            }
        }
    }

    public class ClientHandler implements Runnable {
        Socket socket;
        PrintWriter client;
        BufferedReader reader;

        public ClientHandler(Socket clientSocket, PrintWriter user) {
            client = user;
            try {
                socket = clientSocket;
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isr);
            } catch (Exception e) {
                jTextArea.append("Unexpected error....\n");
            }
        }

        @Override
        public void run() {
            String msg, chat = "Chat";
            String[] data;

            try {
                while ((msg = reader.readLine()) != null) {
                    jTextArea.append("Received: " + msg + '\n');
                    data = msg.split(":");

                    for (String token : data) {
                        jTextArea.append(token + '\n');
                    }

                    switch (data[2]) {
                        case "Connect":
                            tellEveryone((data[0] + ":" + data[1] + ":" + chat));
                            userAdd(data[0]);
                            break;

                        case "Disconnect":
                            tellEveryone((data[0] + " has disconnected." + ":" + chat));
                            userRemove(data[0]);
                            break;
                        case "Chat":
                            tellEveryone(msg);
                            break;

                        default:
                            jTextArea.append("No Conditions were met.\n");
                    }
                }
            } catch (Exception e) {
                jTextArea.append("Lost a connection.\n");
                e.printStackTrace();
                clientOutputStreams.remove(client);
            }
        }
    }

    private void jLabel2MouseClicked() {
        OOP.server_change(server);
        startServer();
        jTextArea.append("Server started... \n");
    }

    private void jLabel3MouseClicked() {
        OOP.server_normal(server);
        stopServer();
        jTextArea.append("Server stopping...\n");
        jTextArea.setText("");
    }

    private void jLabel2MouseEntered() {
        OOP.change_color(jPanel2);
    }

    private void jLabel2MouseExited() {
        OOP.normal_color(jPanel2);
    }

    private void jLabel3MouseEntered() {
        OOP.change_color(jPanel3);
    }

    private void jLabel3MouseExited() {
        OOP.normal_color(jPanel3);
    }

    private void jLabel4MouseClicked() {
        jTextArea.append("\nOnline users: \n");
        for (String currentUser : users) {
            jTextArea.append(currentUser);
            jTextArea.append("\n");
        }
    }

    private void jLabel4MouseEntered() {
        OOP.change_color(jPanel4);
    }

    private void jLabel4MouseExited() {
        OOP.normal_color(jPanel4);
    }

    private void jLabel5MouseClicked() {
        jTextArea.setText("");
    }

    private void jLabel5MouseEntered() {
        OOP.change_color(jPanel5);
    }

    private void jLabel5MouseExited() {
        OOP.normal_color(jPanel5);
    }

    public void userAdd(String data) {
        String msg, add = ": :Connect", done = "Server: :Done", name = data;
        jTextArea.append("Before " + name + " added. \n");
        users.add(name);
        jTextArea.append("After " + name + " added. \n");
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token : tempList) {
            msg = (token + add);
            tellEveryone(msg);
        }
        tellEveryone(done);
    }

    public void userRemove(String data) {
        String msg, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name);
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);

        for (String token : tempList) {
            msg = (token + add);
            tellEveryone(msg);
        }
        tellEveryone(done);
    }

    public void tellEveryone(String msg) {
        Iterator<PrintWriter> it = clientOutputStreams.iterator();

        while (it.hasNext()) {
            try {
                PrintWriter writer = it.next();
                writer.println(msg);
                jTextArea.append("Sending: " + msg + "\n");
                writer.flush();
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
            } catch (Exception e) {
                jTextArea.append("Error telling everyone. \n");
            }
        }
    }

    public class StartServer implements Runnable {

        @Override
        public void run() {
            clientOutputStreams = new ArrayList<>();
            users = new ArrayList<>();

            try {
                serverSocket = new ServerSocket(5000);

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
                    clientOutputStreams.add(printWriter);

                    Thread listener = new Thread(new ClientHandler(clientSocket, printWriter));
                    listener.start();
                    jTextArea.append("Got a connection.\n");
                }

            } catch (Exception e) {
                if (isRunning) {
                    jTextArea.append("Server error: " + e.getMessage() + "\n");
                } else {
                    jTextArea.append("Server stopped.\n");
                }
            }
        }
    }
}

class Main {
    public void change_color(JPanel panel) {
        panel.setBackground(java.awt.Color.BLUE);
    }

    public void normal_color(JPanel panel) {
        panel.setBackground(java.awt.Color.LIGHT_GRAY);
    }

    public void server_change(JLabel server) {
        server.setText("Server is Running");
        server.setForeground(java.awt.Color.GREEN);
    }

    public void server_normal(JLabel server) {
        server.setText("Server");
        server.setForeground(java.awt.Color.BLACK);
    }
}
