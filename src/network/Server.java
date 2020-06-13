package network;

import utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

public class Server extends NetworkNode implements Iterator<Log> {

    // Additional fields for Server
    private static int serverCount = 0;
    private PriorityBlockingQueue<Log> messageQueue;
    private ServerSocket serverSocket;

    private HashSet<Socket> connections;
    private Thread addClientThread;
    private Thread sendThread;

    public Server(String name) throws IOException {
        super(name);
        Utilities.require(serverCount == 0, "Cannot create server, server count: " + serverCount);
        // System.out.println("Server: " + Utilities.externalIp());
        System.out.println("Server...");
        ++serverCount;
        messageQueue = new PriorityBlockingQueue<>();
        // connections = new ConcurrentHashMap<>();
        connections = new HashSet<>();
        serverSocket = new ServerSocket(PORT);

        addClientThread = new Thread(() -> {
            while (isAlive()) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread receive = receiveThread(socket);
                    connections.add(socket);
                    receive.start();
                    System.out.println("Connection established with " + socket.getInetAddress().toString());
                } catch (SocketException exp) {
                    // Server socket already closed ready to quit
                }
                catch (Exception exp) {
                    Utilities.handle(exp);
                }
            }
        });

        sendThread = new Thread(() -> {
            while (isAlive()) {
                if (!messageQueue.isEmpty()) {
                    notifyObservers();
                    Log log = messageQueue.poll();
                    for (Socket socket : connections) {
                        // Send the message
                        try {
                            PrintWriter pr = new PrintWriter(socket.getOutputStream());
                            pr.println(log);
                            System.out.println("Sent: "+ log);
                            pr.flush();
                        } catch (IOException exp) {
                            Utilities.handle(exp);
                        }
                    }
                }
            }
        });

        addClientThread.start();
        sendThread.start();
    }

    public Thread receiveThread(Socket socket) {
        return new Thread(() -> {
            boolean connectionAlive = true;
            while (isAlive() && connectionAlive) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String input = br.ready() ? br.readLine() : null;
                    if (input != null && input.length() > 1) {
                        if(input.equals("CLOSE")) {
                            socket.close();
                            connectionAlive = false;
                            System.out.println("CONNECTION CLOSED");
                        }
                        else {
                            int ownerLength = input.charAt(0) - '0';
                            int index = ownerLength + 1;
                            String owner = input.substring(1, index);
                            String message = input.substring(index);
                            messageQueue.offer(new Log(message, owner));
                            System.out.println("Received: " + input);
                        }
                    }
                } catch (IOException exp) {
                    Utilities.handle(exp);
                }
            }
        });
    }

    public void offer(Log log) {
        messageQueue.offer(log);
    }

    @Override
    public boolean hasNext() {
        return !messageQueue.isEmpty();
    }

    @Override
    public Log next() {
        return messageQueue.peek();
    }

    @Override
    public void kill() throws IOException {
        super.kill();
        --serverCount;
        for (Socket socket: connections) {
            socket.close();
        }
        serverSocket.close();
    }
}
