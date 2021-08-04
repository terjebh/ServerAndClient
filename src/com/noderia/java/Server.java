package com.noderia.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;

public class Server {


    public static void main(String[] args) throws IOException {

        final int PORT;

        if(args.length == 1) {
             PORT = Integer.parseInt(args[0]);
        } else {
             PORT = 9898;
        }

        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("[SERVER] Server running on port "+PORT+", waiting for connections... ");
        Socket client = listener.accept();
        System.out.println("Client connected");
        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

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
                } else if (request.contains("vis")) {
                    StringBuffer notater = new StringBuffer();
                    notater.append("Innholdet i filen notater.txt\n\n");
                    Files.lines(Paths.get("notater.txt")).forEach(a -> notater.append(a+"\n"));
                    out.println(notater.toString());
                } else {
                   out.println("I don't know what that means...");
                }
            }
        } finally {
            in.close();
            out.close();
            out.println("Goodbye...");
            System.exit(0);
        }

    }

}
