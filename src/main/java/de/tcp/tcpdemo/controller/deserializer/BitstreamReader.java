package de.tcp.tcpdemo.controller.deserializer;

import java.io.ByteArrayInputStream;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class BitstreamReader {

    private static final int MASK_255 = 0b11111111;
    private static final int MASK_127 = 0b01111111;
    private static final int MASK_128 = 0b10000000;
    private static final int OCTET_7 = 7;
    private static final int OCTET_8 = 8;

    private ByteArrayInputStream bais;

    public BitstreamReader(byte[] byteStream) {
        bais = new ByteArrayInputStream(byteStream);
    }

    public ByteArrayInputStream getByteStreamBuffer() {
        return bais;
    }

    protected byte[] readBytes(int length) {
        checkEmptyBuferAndThrowError();
        byte[] value = new byte[length];
        for (int i = 0; i < length; i++) {
            value[i] = (byte) bais.read();
        }
        return value;
    }

    protected long readLong(int length) {
        checkEmptyBuferAndThrowError();
        long value = 0;
        for (int i = 0; i < length; i++) {
            int block = bais.read() & MASK_255;
            value <<= OCTET_8;
            value |= block;
        }
        return value;
    }

    public void checkEmptyBuferAndThrowError() {
        if (bais.available() == 0) {
            throw new RuntimeException("There are no bytes left to be read!");
        }
    }

    public void checkRemainingBytesAndThrowError() {
        if (bais.available() > 0) {
            throw new RuntimeException("There are still bytes in the buffer!");
        }
    }

    protected long readBigEndianLong() {
        checkEmptyBuferAndThrowError();
        long value = 0;
        byte blockWithMsb = (byte) bais.read();
        byte blockData = (byte) (blockWithMsb & MASK_127);
        int msb = blockWithMsb & MASK_128;

        while (msb == MASK_128) {
            value |= blockData;
            value = value << OCTET_7;
            blockWithMsb = (byte) bais.read();
            blockData = (byte) (blockWithMsb & MASK_127);
            msb = blockWithMsb & MASK_128;
        }

        value |= blockData;

        return value;
    }

    public int readL() {
        return (int) readBigEndianLong();
    }

    public int readT() {
        return (int) readBigEndianLong();
    }

    public long readV() {
        return readBigEndianLong();
    }

    public long readV(int length) {
        return readLong(length);
    }
}