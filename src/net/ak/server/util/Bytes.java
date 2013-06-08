package net.ak.server.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Author: Akira Koyasu <mail@akirakoyasu.net>
 * Date: 2013/06/08
 * Time: 22:28
 */
public class Bytes {
    private static final int BUF_SIZE = 0x1000;

    public static void copy(InputStream from, OutputStream to) throws IOException {
        copy(Channels.newChannel(from), Channels.newChannel(to));
    }

    public static void copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);
        while (from.read(buf) >= 0 || buf.position() != 0) {
            buf.flip();
            to.write(buf);
            buf.compact();
        }
    }
}
