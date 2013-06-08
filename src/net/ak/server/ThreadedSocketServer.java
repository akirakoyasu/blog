package net.ak.server;

import net.ak.server.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Akira Koyasu <mail@akirakoyasu.net>
 * Date: 2013/06/08
 * Time: 23:07
 */
public class ThreadedSocketServer {
    public static void main(String[] args) {
        ExecutorService worker = Executors.newCachedThreadPool();
        try (ServerSocket listener = new ServerSocket();) {
            listener.setReuseAddress(true);
            listener.bind(new InetSocketAddress(8081));
            System.out.println("Server listening on port 8081...");
            while (true) {
                final Socket _socket = listener.accept();
                System.out.printf("ACCEPT %s%n", _socket);
                worker.submit(new Runnable() {
                    @Override
                    public void run() {
                        try ( Socket socket = _socket;) {
                            InputStream from = socket.getInputStream();
                            OutputStream to = socket.getOutputStream();
                            Bytes.copy(from, to);
                            System.out.printf("CLOSE %s%n", socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            worker.shutdown();
        }
    }
}
