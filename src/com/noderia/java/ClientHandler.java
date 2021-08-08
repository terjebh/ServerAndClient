package com.noderia.java;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private int clientNUmber;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients, int clientNumber) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.clientNUmber = clientNumber;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                String request = in.readLine().toLowerCase(Locale.ROOT);

                if(request.contains("date")) {
                    out.println("[SERVER] The date today is: " + LocalDate.now());
                } else if (request.contains("lagre")) {
                    FileWriter notatFil = new FileWriter("notater.txt",true);
                    notatFil.append(request.replace("lagre","").trim()+"\n");
                    notatFil.close();
                    out.println("[SERVER] Notatet er lagret");
                    System.out.println("Notat lagret...");
                } else if (request.startsWith("vis")) {
                   out.append("[SERVER] Innholdet i filen notater.txt\n\n");
                    Files.lines(Paths.get("notater.txt")).forEach(a -> out.append(a + "\n"));
                    out.println();
                } else if (request.startsWith("shout")) {
                     outToAll(request.replace("shout ",""), clientNUmber);
                } else {
                    out.println("[SERVER] I don't know what that means...");
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception in client handler");
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
        }

    }

    private void outToAll(String msg, int clientNUmber) {
        for (ClientHandler aClient : clients) {
          aClient.out.println("Client "+clientNUmber+" says: "+msg);
        }

    }
}
