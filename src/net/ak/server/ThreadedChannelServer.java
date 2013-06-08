package net.ak.server;

import net.ak.server.util.Bytes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Akira Koyasu <mail@akirakoyasu.net>
 * Date: 2013/06/08
 * Time: 23:39
 */
public class ThreadedChannelServer {
    public static void main(String[] args) {
        ExecutorService worker = Executors.newCachedThreadPool();
        try (ServerSocketChannel listener = ServerSocketChannel.open();) {
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
            listener.bind(new InetSocketAddress(8081));
            System.out.println("Server listening on port 8081...");
            while (true) {
                final SocketChannel _channel = listener.accept();
                System.out.printf("ACCEPT %s%n", _channel);
                worker.submit(new Runnable() {
                    @Override
                    public void run() {
                        try (SocketChannel channel = _channel;) {
                            Bytes.copy(channel, channel);
                            System.out.printf("CLOSE %s%n", channel);
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
