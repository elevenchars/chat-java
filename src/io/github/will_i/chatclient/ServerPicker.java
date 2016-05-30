package io.github.will_i.chatclient;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by Will on 5/29/2016.
 */
public class ServerPicker extends JFrame{
    private JTable table1;
    private JPanel panel1;
    private JButton connectButton;
    private DefaultTableModel dtm;

    public ServerPicker() {
        super("Server Picker");

        setSize(600,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel1);
        createUIComponents();
        setVisible(true);
        ServerListManager s = new ServerListManager();
        dtm = new DefaultTableModel(new String[]{"room name","number of members","ip"},0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(dtm);

        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);
        s.start();

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ChatGUI c = new ChatGUI((String)table1.getValueAt(table1.getSelectedRow(),2));
            }
        });
    }

    public static void main(String[] args) {
        ServerPicker p = new ServerPicker();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private class ServerListManager extends Thread {
        ServerLister lister;
        public ServerListManager() {
            lister = new ServerLister();
        }
        public void run() {
            while(lister.num < 254) {
                String[] data = lister.getServer();
                if (data.length > 1) {
                    System.out.println(Arrays.toString(data));
                    dtm.addRow(data);
                }
            }
        }
    }
}
