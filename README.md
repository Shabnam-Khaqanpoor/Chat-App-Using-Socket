# 🗨️ Chat Application 

## 📄 Description

This is a simple chat application built using Java, which allows multiple clients to connect to a server. The server handles multiple client connections and broadcasts messages to all connected clients. The server also stores messages in a MySQL database. The client connects to the server and can send messages, view messages from the server, and ping the server to measure latency.

### 🌟 Features
- 🌍 Multiple client connections
- 📡 Real-time message broadcasting
- 🏓 Ping functionality to measure server response time
- 💾 Messages are saved to a MySQL database
- 📜 The ability to view previous messages from the database

## 🔧 Components

The project consists of two main components:

### 1. **Server**

- 🖥️ **ServerSocket**: Listens for incoming client connections on port 9999.
- 🔄 **ConnectionHandler**: Handles communication with each client, including sending and receiving messages.
- 📢 **Message Broadcasting**: Sends messages to all connected clients.
- 🗄️ **Database Integration**: Saves messages to a MySQL database and retrieves previous messages for new clients.
- ⏱️ **Ping**: Measures the round-trip latency between the client and server.

### 2. **Client** 

- 🌐 **Socket**: Connects to the server on `127.0.0.1` (localhost) and port `6666`.
- 📝 **InputHandler**: Reads user input and sends messages to the server.
- 💬 **Message Display**: Displays messages received from the server.

## ⚙️ Requirements

1. **Java 8 or higher** for compiling and running the code.
2. **MySQL Database**:
   - The MySQL database named `messenger` should be set up with a table `texts` for storing messages.
   - You can create the table using the following SQL query:
   
     ```sql
     CREATE TABLE texts (
         id INT AUTO_INCREMENT PRIMARY KEY,
         message TEXT NOT NULL
     );
     ```

3. **JDBC**: Ensure that the MySQL JDBC driver (`com.mysql.cj.jdbc.Driver`) is included in the classpath for database operations.

4. **Ports**:
   - Server listens on port `9999`.
   - Client connects to the server on port `6666`.

## 🏃‍♂️ How to Run

### 1. **Start the Server** 💻

Compile and run the `Server.java` file.

```bash
javac Server.java
java Server
```

The server will start listening for client connections on port 9999.

### 2. **Start the Client** 📱

Compile and run the `Client.java` file.

```bash
javac Client.java
java Client
```

The client will connect to the server on `127.0.0.1:6666`.

### 3. **Interacting with the Application** 💬

- Once connected, you will be prompted to enter a username.
- You can then send messages, which will be broadcast to all other clients.
- Type `exit` to leave the chat.
- You can ping the server by typing `ping` to measure latency.

## 💾 Database Operations

Messages sent by clients are saved to the `texts` table in the `messenger` database. On connecting, the client can view all previous messages saved in the database.

## ⚠️ Shutdown Procedure

- Clients can type `exit` to leave the chat and close the connection.
- The server automatically shuts down if the connection is lost or an error occurs.
