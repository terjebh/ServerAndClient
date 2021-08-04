package com.noderia.java;

import javax.sql.rowset.serial.SQLInputImpl;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler (Socket clientSocket) throws IOException {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                String request = in.readLine().toLowerCase(Locale.ROOT);

                if(request.contains("date")) {
                    out.println("The date today is: " + LocalDate.now());
                } else if (request.contains("lagre")) {
                    FileWriter notatFil = new FileWriter("notater.txt",true);
                    notatFil.append(request.replace("lagre","").trim()+"\n");
                    notatFil.close();
                    out.println("Notatet er lagret");
                    System.out.println("Notat lagret...");
                } else if (request.startsWith("vis")) {
                    StringBuffer notater = new StringBuffer();
                    notater.append("Innholdet i filen notater.txt\n\n");
                    Files.lines(Paths.get("notater.txt")).forEach(a -> notater.append(a+"\n"));
                    out.println(notater);
                } else {
                    out.println("I don't know what that means...");
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
}
