package it.unipi.lab3.abalderi1.utils;

import it.unipi.lab3.abalderi1.config.ConfigHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Classe di utilità per l'invio di messaggi multicast.
 */
public class MulticastSocketUtil {
    private static final String MULTICAST_ADDRESS = ConfigHandler.getInstance().getProperty("multicastAddress", "224.0.0.1");
    private static final int MULTICAST_PORT = ConfigHandler.getInstance().getIntProperty("multicastPort", 4446);

    /**
     * Condivide la stringa fornita come messaggio multicast.
     *
     * @param result La stringa da multicast.
     * @throws IOException se si verifica un errore di I/O durante il multicast.
     */
    public static void share(String result) throws IOException {
        MulticastSocket socket = new MulticastSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        byte[] buf = result.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);

        socket.send(packet);
        socket.close();
    }
}
