package io.github.will_i.chatclient;

import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by willi on 5/26/16.
 */
public class ChatGUI extends JFrame{
    private JPanel panel1;
    private JTextPane textPane1;
    private JTextField textField1;
    private JButton clearButton;
    private JButton saveLogsButton;

    public ChatGUI() {
        this(getIP());
    }

    public ChatGUI(String ip) {
        super("Chat!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        setVisible(true);
        pack();
        setSize(600,800);
        ChatClient c = new ChatClient(ip, 9002);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                super.keyReleased(keyEvent);
                if((int)keyEvent.getKeyChar() == 10 ) {
                    c.sendMessage(textField1.getText());
                    textField1.setText("");
                }
            }
        });

        DefaultCaret caret = (DefaultCaret)textPane1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        inputListener i = new inputListener(c.getBufferedReader());
        i.start();
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textPane1.setText("");
            }
        });
        saveLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*chooser.showSaveDialog(null);
                System.out.println(chooser.getSelectedFile());
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
                String formattedDate = sdf.format(date);
                System.out.println(formattedDate);
                chooser.getSelectedFile().createNewFile()*/
                JOptionPane.showMessageDialog(null, "This feature is not implemented yet.");
            }
        });
    }

    private static String getIP() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        JTextField ip = new JTextField();
        JLabel ipLabel = new JLabel("IP");

        panel.add(ipLabel);
        panel.add(ip);

        JOptionPane.showMessageDialog(null,panel,"Enter server information",JOptionPane.QUESTION_MESSAGE);
        return ip.getText();

    }
    public void updateChatPane(String s) {
        textPane1.setText(textPane1.getText() + s + "\n");
    }

    public static void main(String[] args) {
        ChatGUI c = new ChatGUI();
    }

    private class inputListener extends Thread {
        BufferedReader in;
        public inputListener(BufferedReader in) {
            this.in = in;
        }

        public void run() {
            try {
                setTitle(in.readLine().split(":")[0]);
                for (String msg = in.readLine(); msg != null; msg = in.readLine()) {
                    updateChatPane(msg);
                    System.out.println(msg);
                }
                updateChatPane("server appears to be down");
            } catch (Exception e) {
                System.out.println("something went wrong");
            }
        }
    }
}
