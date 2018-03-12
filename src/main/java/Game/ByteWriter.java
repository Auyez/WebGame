package Game;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteWriter {
    private ByteBuffer buffer;

    public ByteWriter() {
        buffer = ByteBuffer.allocate(16);
    }

    public byte[] bytes() {
        return Arrays.copyOf(buffer.array(), buffer.position());
    }

    public void writeInt(int x) {
        grow(buffer.position() + 4);
        buffer.putInt(x);
    }

    public void writeByte(byte x) {
        grow(buffer.position() + 1);
        buffer.put(x);
    }

    public void writeBytes(byte[] x) {
        grow(buffer.position() + x.length);
        buffer.put(x);
    }

    private void grow(int capacity) {
        if (buffer.capacity() < capacity) {
            ByteBuffer newBuffer = ByteBuffer.allocate(capacity * 2);
            newBuffer.put(buffer.array(), 0, buffer.position());
            buffer = newBuffer;
        }
    }

    @Override
    public String toString() {
        return toString(bytes());
    }

    public static String toString(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        s.append('[');
        for(int i = 0; i < bytes.length; ++i) {
            if (i != 0) s.append(',');
            s.append((int)bytes[i] & 0xFF);
        }
        s.append(']');
        return s.toString();
    }

    public static byte[] Integer2bytes(Integer x) {
        ByteBuffer buf = ByteBuffer.allocate(4).putInt(x);
        return buf.array();
    }

    public static byte[] Byte2bytes(Byte x) {
        byte[] bytes = new byte[1];
        bytes[0] = x;
        return bytes;
    }
}