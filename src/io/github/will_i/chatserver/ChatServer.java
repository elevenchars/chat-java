package io.github.will_i.chatserver;

import io.github.will_i.chatclient.ServerLister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by will-i on 5/19/2016.
 */
//http://cs.lmu.edu/~ray/notes/javanetexamples/

public class ChatServer {
    private static final int PORT = 9002;
    private static ArrayList<String> names = new ArrayList<>();
    private static ArrayList<PrintWriter> writers = new ArrayList<>();
    private static String serverName = "a chat server";
    public static void main(String[] args) throws Exception{
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter server name: ");
        serverName = kb.nextLine();
        System.out.println(" Server started on  " + ServerLister.getIP() + ":" + PORT + " with server name " + serverName);
        ServerSocket listener = new ServerSocket(PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                broadcast("!!SERVER IS SHUTTING DOWN!!");
            }
        }, "Shutdown-thread"));
        while (true) {
            new ClientHandler(listener.accept()).start();
            System.out.println("client found");
        }
    }

    public static void broadcast(String msg) {
        System.out.println("[CHAT] " + msg);
        for(PrintWriter p: writers) {
            p.println(msg);
        }
    }

    private static class ClientHandler extends Thread {
        private String username;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket s) {
            socket = s;
        }



        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println(serverName + ":" + names.size() + ":" + ServerLister.getIP());

                while(true) {
                    out.println("please type your name");
                    username = in.readLine();
                    if (username.equals("!!BROWSER!!")) {
                        System.out.println("client was actually server browser");
                        return;
                    }
                    System.out.println("user connected with name " + username);
                    if (username == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(username)) {
                            names.add(username);
                            break;
                        }
                        out.println(username + " is taken, please try again");
                    }
                }
                out.println("name accepted!");
                writers.add(out);
                broadcast(username + " has joined the room");
                while(true) {
                    for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
                        parseInput(msg);
                    }
                    return;
                }
            } catch (Exception e) {
                System.out.println("user " + username + " has disconnected or timed out");
            } finally {

                if(username != null) {
                    names.remove(username);
                }
                if(out != null) {
                    writers.remove(out);
                }
                try {
                    if(!username.equals("!!BROWSER!!")) {
                        broadcast(username + " has left the room");
                        socket.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        public void parseInput(String msg) {
            if (msg.equals("/help") || msg.startsWith("/help")) {
                out.println("commands:\n/help\n/me\n/name\n/list\n/about");
            } else if(msg.startsWith("/me ")) {
                broadcast("*" + username + msg.substring(3, msg.length()) + "*");
            } else if (msg.startsWith("/name ")) {
                String newUsername = msg.substring(6,msg.length());
                broadcast(username + " is now " + newUsername);
                names.set(names.indexOf(username),newUsername);
                username = newUsername;
            } else if (msg.startsWith("/about ") || msg.equals("/about")) {
                out.println("Created by github.com/will-i for an ap computer science final");
            } else if (msg.startsWith("/list ") || msg.equals("/list")) {
                out.println(names);
            } else if (msg.equalsIgnoreCase("/roll")) {
                broadcast(username + " rolled " + (new Random().nextInt(6) + 1));
            } else if (msg.startsWith("/roll ")) {
                broadcast(username + " rolled "+(new Random().nextInt(Integer.parseInt(msg.substring(6,msg.length())))+1));
            } else if (msg.startsWith("/vince ")) {
                broadcast(username + "> " + msg.substring(7,msg.length()).replaceAll(" ", " NO "));
            }
            else if(msg != null || msg != " " || msg != "") {
                broadcast(username + "> " + msg);
            }
        }
    }
}
