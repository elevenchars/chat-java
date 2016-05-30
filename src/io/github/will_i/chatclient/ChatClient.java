package io.github.will_i.chatclient;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * ChatClient.java
 * Will Ingarfield
 * 5/25/2016
 */
public class ChatClient {
    BufferedReader in;
    PrintWriter out;
    Socket s;

    public ChatClient(String ip, int port) {

        try {
            s = setSocket(ip,port);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Couldn't create BufferedReader or PrintWriter");
        }
    }

    public Socket setSocket(String ip, int port) {
        try {
            return new Socket(ip,port);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("oops! couldn't create Socket object. Returning null");
            return null;
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public PrintWriter getPrintWriter() {
        return out;
    }

    public BufferedReader getBufferedReader() {
        return in;
    }

    public String getNextMessage() throws  IOException {
        return in.readLine();
    }


    public Socket getSocket() {
        return s;
    }
}
