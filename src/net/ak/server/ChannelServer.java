package net.ak.server;

import net.ak.server.util.Bytes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Author: Akira Koyasu <mail@akirakoyasu.net>
 * Date: 2013/06/08
 * Time: 23:24
 */
public class ChannelServer {
    public static void main(String[] args) {
        try (ServerSocketChannel listener = ServerSocketChannel.open();) {
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
            listener.bind(new InetSocketAddress(8080));
            System.out.println("Server listening on port 8080...");
            while (true) {
                try (SocketChannel channel = listener.accept();) {
                    System.out.printf("ACCEPT %s%n", channel);
                    Bytes.copy(channel, channel);
                    System.out.printf("CLOSE %s%n", channel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
