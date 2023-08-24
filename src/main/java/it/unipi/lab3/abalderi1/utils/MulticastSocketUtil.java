package it.unipi.lab3.abalderi1.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSocketUtil {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int MULTICAST_PORT = 4446;

    public static void share(String result) throws IOException {
        MulticastSocket socket = new MulticastSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        byte[] buf = result.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);

        socket.send(packet);
        socket.close();
    }
}
