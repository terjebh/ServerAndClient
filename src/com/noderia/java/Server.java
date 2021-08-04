package com.noderia.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {

        final int PORT;

        if(args.length == 1) {
            PORT = Integer.parseInt(args[0]);
        } else {
            PORT = 9898;
        }

        ServerSocket listener = new ServerSocket(PORT);
        while (true) {
            System.out.println("[SERVER] Server running on port " + PORT + ", waiting for connections... ");
            Socket client = listener.accept();
            System.out.println("Client connected");
            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);
            pool.execute(clientThread);
        }

        // out.println("Goodbye...");
        // System.exit(0);

    }

}
