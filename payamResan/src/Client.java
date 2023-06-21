import java.net.*;
import java.io.*;

public class Client implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 6666);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inMessage;
            while((inMessage = in.readLine())!= null ){
                System.out.println(inMessage);
            }

        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {  // mikham harrja exception bood estefade she ta etmam bekhore

        try {
            done = true;
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inReader.readLine();
                    if (message.equals("exit")) {// aqe payam tamomo dad bebande
                        out.println(message);
                        inReader.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }
                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }
    public static void main(String[] args){
        Client client = new Client();
        client.run();

    }

}
