package lobby;

import java.nio.ByteBuffer;

public class ByteReader {
    private ByteBuffer buffer;

    public ByteReader(byte[] bytes) {
        buffer = ByteBuffer.wrap(bytes);
    }

    public int readInteger() {
        return buffer.getInt();
    }

    public byte readByte() {
        return buffer.get();
    }

    public String readString() {
        int length = buffer.getInt();
        byte[] chars = new byte[length];
        buffer.get(chars, 0, length);

        return new String(chars);
    }
}
