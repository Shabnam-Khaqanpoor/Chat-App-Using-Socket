import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket serverSocket;
    private boolean done;
    private ExecutorService pool;

    public Server() {
        connections = new ArrayList<>();
        done = false;
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                this.connections.add(handler);

                pool.execute(handler);
                //a client connected and add to connections
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : this.connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        //close all
        try {
            done = true;
            pool.shutdown();
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            //ignore
        }
    }


    class ConnectionHandler implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            Instant start = Instant.now();
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("Welcome");
                out.println("Enter your username:");
                this.name = in.readLine();
                System.out.println(this.name + " connected!");
                broadcast(this.name + " joined the chat!");

                //joined the chat

                Instant end1 = Instant.now();
                Duration timeElapsed1 = Duration.between(start, end1);
                out.println("ping " + timeElapsed1.toMillis() + "ms.");
                out.println();
                showMessages();

                //show ping when connected


                String message;
                while ((message = in.readLine()) != null) {

                    if (message.equals("exit")) {
                        broadcast(this.name + " left the chat!");
                        shutdown();

                    } else if (message.equals("ping")) {

                        Instant end2 = Instant.now();
                        Duration timeElapsed2 = Duration.between(start, end2);
                        out.println("ping " + timeElapsed2.toMillis() + "ms.");
                    } else {
                        broadcast(this.name + " :" + message);
                        saveToDatabase(message);
                        //send message
                    }
                }
            } catch (Exception e) {
                shutdown();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
            //send
        }

        public void saveToDatabase(String message) throws Exception {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/messenger", "root", "4013623007");
            //connected to the database
            String text = this.name + " : " + message;
            String sql = String.format("INSERT INTO (texts) VALUES ('%s')", text);
            Statement s = con.prepareStatement(sql);
            s.execute(sql);


            //saved to database
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                pool.shutdown();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }

        public void showMessages() {
            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/messenger", "root", "4013623007");
                Statement stmt = con.createStatement();
                String text = String.format("SELECT * from texts");
                ResultSet rs = stmt.executeQuery(text);

                // Open a connection
                while (rs.next()) {
                    out.println(rs);
                    //Display values
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
