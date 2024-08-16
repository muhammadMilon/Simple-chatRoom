# Simple-chatRoom

A simple chat application with a server-client architecture built in Java. This project includes both a server-side application and a client-side application, allowing multiple clients to connect and communicate with each other in real-time.

## Features

- **Server-side Application**:
  - Start and stop the server.
  - Display online users.
  - Broadcast messages to all connected clients.
  - Handle user connections and disconnections.

- **Client-side Application**:
  - Connect to a server using IP address and port.
  - Send and receive messages in real-time.
  - Display incoming messages and notifications.
  - Disconnect from the server.

## Demo

[Watch the **Demo** Video](https://www.youtube.com/watch?v=gz4mQVkoeYw&list=PLcpndM7GLzjoY4KC20QDfufO3LX5IoUWg&index=5)

## Motivation

This project was created to practice Java networking concepts and develop a real-time chat application with a server-client model. It provides hands-on experience with Java sockets and multi-threaded programming.

## Usage

To use the Simple-chatRoom application:

### Running the Server

1. **Compile the Server Code**:
    ```bash
    javac Server_Side.java
    ```
2. **Start the Server**:
    Open a terminal, navigate to the directory containing `Server_Side.class`, and run:
    ```bash
    java Server_Side
    ```

### Running the Client

1. **Compile the Client Code**:
    ```bash
    javac Client_Side.java
    ```
2. **Start the Client**:
    Open another terminal, navigate to the directory containing `Client_Side.class`, and run:
    ```bash
    java Client_Side
    ```
3. **Connect to the Server**:
    - Enter your username and the server address (e.g., `localhost` if running on the same machine).
    - Click "Connect" to establish a connection to the server.

4. **Send and Receive Messages**:
    - Type your message in the input field and click "Send" to broadcast it to all connected clients.
    - Messages from other clients will be displayed in the chat area.

5. **Disconnect**:
    - Click "Disconnect" to leave the chat and close the connection to the server.

## Technologies Used

- **Java**: For both server-side and client-side development.
- **Java Swing**: For the graphical user interface of the client application.
- **Java Sockets**: For implementing server-client communication.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/simple-chatroom.git
    cd simple-chatroom
    ```
2. Compile the code:
    ```bash
    javac Server_Side.java Client_Side.java
    ```

## Contributions

Contributions to enhance the functionality, improve UI/UX, or fix issues are welcome! If you have any suggestions or improvements, please open an issue or submit a pull request.

## Acknowledgments

- Special thanks to the CSE department for inspiration and guidance.
- Appreciation to all contributors and mentors for their support.

---

**Author:** [Muhammad Milon](https://github.com/muhammadMilon)  
**Project Link:** [Here](https://github.com/muhammadMilon/Simple-chatRoom)
