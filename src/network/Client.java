package network;

import utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public final class Client extends NetworkNode {
    private Thread sendThread;
    private Thread receiveThread;

    private Socket socket;
    private AtomicReference<String> userMessage;
    private AtomicReference<String> serverMessage;

    public Client(String name, String serverIp) throws IOException {
        super(name);
        socket = new Socket(serverIp, PORT);
        userMessage = new AtomicReference<>(null);
        serverMessage = new AtomicReference<>(null);

        receiveThread = new Thread( () -> {
            while (isAlive()) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String input = br.ready() ? br.readLine() : null;
                    // Log receieved from Server
                    if (input != null && input.length() > 0) {
                        // push log to window
                        if (input.charAt(0) == '-') {
                            kill();
                        }
                        else {
                            serverMessage.set(input);
                            notifyObservers();
                            serverMessage.set(null);
                        }
                        System.out.println("Received: " + input);
                    }
                } catch (IOException exp) {
                    Utilities.handle(exp);
                }
            }
        });

        sendThread = new Thread(() -> {
            while (isAlive()) {
                // Send the message to server
                String message = userMessage.get();
                if (message != null && message.length() > 0) {
                    try {
                        PrintWriter pr = new PrintWriter(socket.getOutputStream());
                        String output = message.equals("CLOSE") ? message : name.length() + name + message;
                        pr.println(output);
                        System.out.println("Sent: " + output);
                        pr.flush();
                        userMessage.set(null);
                    } catch (IOException exp) {
                        Utilities.handle(exp);
                    }
                }
            }
        });
        receiveThread.start();
        sendThread.start();
    }


    public void pushMessageToServer(String message) {
        userMessage.set(message);
    }

    public String pushLogToWindow() {
        return serverMessage.get();
    }

    @Override
    public void kill() throws IOException {
        super.kill();
        socket.close();
    }
}
