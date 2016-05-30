package io.github.will_i.chatclient;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by ingarfie_902980 on 5/26/2016.
 */
public class ChatCLI {

    static ChatClient c;

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter server ip: ");
        String ip = kb.nextLine();
        System.out.print("Enter server port: ");
        int port = kb.nextInt();

        c = new ChatClient(ip,port);
        System.out.println("Connected to " + c.getSocket());

        InputHandler i = new InputHandler(c.getBufferedReader());
        OutputHandler o = new OutputHandler(c.getPrintWriter());

        i.start();
        o.start();

        while (true) {

        }
    }
    private static class InputHandler extends Thread{
        BufferedReader in;
        public InputHandler(BufferedReader in) {
            this.in = in;
        }

        public void run() {
            try {
                while(true) {
                    System.out.println(c.getNextMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class OutputHandler extends Thread {
        PrintWriter out;
        public OutputHandler(PrintWriter out) {
            this.out = out;
        }

        public void run() {
            try {
                Scanner kb = new Scanner(System.in);
                while (true) {
                    String send = kb.nextLine();
                    out.println(send);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
