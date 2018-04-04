package lobby;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
}
