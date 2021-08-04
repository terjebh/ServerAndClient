package com.noderia.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {


    public static void main(String[] args) {

        final String IP;
        final int PORT;

        if (args.length == 2) {
            IP = args[0];
            PORT = Integer.parseInt(args[1]);
        } else {
            IP = "localhost";
            PORT = 9898;
        }

        System.out.println("IP: " + IP);
        System.out.println("Port: " + PORT);

        try {
            Socket socket = new Socket(IP, PORT);

            ServerConnection serverCon = new ServerConnection(socket);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(serverCon).start();

            while (true) {
                System.out.print("> ");
                String command = keyboard.readLine();
                if (command.equals("quit")) break;
                out.println(command);
            }

            socket.close();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
