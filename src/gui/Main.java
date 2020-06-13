package gui;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        System.out.print("1 for server 0 for client: ");
        int choice = scan.nextInt();
        if (choice == 1) {
            new ServerFrame(name);
        }
        else {
            scan.nextLine();
            boolean flag;
            String serverIp;
            do {
                System.out.print("Enter the server IP please: ");
                serverIp = scan.nextLine();
                System.out.println("server IP: " + serverIp);
                flag = false;
                try {
                  new ClientFrame(name, serverIp);
                } catch (ConnectException exp) {
                  flag = true;
                  System.out.println("Server " + serverIp + " not found. Please try again");
                } catch (UnknownHostException exp) {
                    flag = true;
                    System.out.println("Host could not found: " + serverIp);
                }
            } while(flag);


        }
        scan.close();
    }
}
