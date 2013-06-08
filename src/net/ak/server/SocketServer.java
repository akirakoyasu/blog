package net.ak.server;

import net.ak.server.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Author: Akira Koyasu <mail@akirakoyasu.net>
 * Date: 2013/06/08
 * Time: 22:22
 */
public class SocketServer {
    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket();) {
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(8080));
            System.out.println("Server listening on port 8080...");
            while (true) {
                try (Socket socket = listener.accept();) {
                    InputStream from = socket.getInputStream();
                    OutputStream to = socket.getOutputStream();
                    System.out.printf("ACCEPT %s%n", socket);
                    Bytes.copy(from, to);
                    System.out.printf("CLOSE %s%n", socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
