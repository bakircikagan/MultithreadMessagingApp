package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public final class Utilities {

    public static void require(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void handle(Exception exp) {
        exp.printStackTrace();
        System.exit(-1);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exp) {
            handle(exp);
        }
    }

    public static String internalIp() {
        Socket socket = new Socket();
        String result = null;
        try {
            socket.connect(new InetSocketAddress("1.1.1.1", 80));
            result = String.valueOf(socket.getLocalAddress()).substring(1);
            socket.close();
        } catch (IOException exp) {
            handle(exp);
        }
        return result;
    }

    public static String externalIp() {
        String ip = null;
        do {
            try {
                URL whatismyip = new URL("http://checkip.amazonaws.com");
                BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                ip = in.readLine();
            } catch (IOException exp) {
                handle(exp);
            }
        } while(ip.length() < 1);

        return ip;
    }
}
