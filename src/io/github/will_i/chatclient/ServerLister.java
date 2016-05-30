package io.github.will_i.chatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by Will on 5/29/2016.
 */
public class ServerLister {
    public int num = 0;
    InetAddress localhost;
    byte[] localhostIP;
    public ServerLister() {
        try {

            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()){
                NetworkInterface ni=(NetworkInterface) en.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while(ee.hasMoreElements()) {
                    InetAddress ia= (InetAddress) ee.nextElement();
                    if(ia instanceof Inet4Address) {
                        if(!ia.getHostAddress().startsWith("127.")) {
                            System.out.println(ia);
                            localhost = ia;
                            localhostIP = ia.getAddress();
                        }
                    }

                }
            }
        } catch (Exception e) {

        }
    }

    public static String getIP() {
        try {

            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()){
                NetworkInterface ni=(NetworkInterface) en.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while(ee.hasMoreElements()) {
                    InetAddress ia= (InetAddress) ee.nextElement();
                    if(ia instanceof Inet4Address) {
                        if(!ia.getHostAddress().startsWith("127.")) {
                            return ia.getHostAddress();
                        }
                    }

                }
            }
        } catch (Exception e) {

        }
        return "how did u even get here";
    }

    public static void main(String[] args) {
        ServerLister servers = new ServerLister();
        ArrayList<String> available =new ArrayList<>();
        try {
            while (servers.num < 254) {
                System.out.println(Arrays.toString(servers.getServer()));
            }
        }catch (Exception e) {

        }
    }

    private String getServerInfo(String ip) {
        try {
            Socket s = new Socket(ip,9002);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            out.println("!!BROWSER!!");

            return in.readLine();
        } catch (Exception e) {
            return "no server running on this device";
        }
    }

    public String[] getServer() {
        try {
            return getServerInfo(InetAddress.getByAddress(getNextIP()).toString().substring(1)).split(":");
        } catch (Exception e) {
        }
        return null;
    }


    private byte[] getNextIP() {
        byte[] ip = localhostIP;
        try {
            while(true) {
                num++;
                if (num > 254) {
                    return null;
                }
                ip[3] = (byte)num;
                InetAddress address = InetAddress.getByAddress(ip);
                if (address.isReachable(100)) {
                    System.out.println("found device");
                    return ip;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
