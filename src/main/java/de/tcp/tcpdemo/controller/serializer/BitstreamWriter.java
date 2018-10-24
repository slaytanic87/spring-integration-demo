package de.tcp.tcpdemo.controller.serializer;

import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class BitstreamWriter {

    private static final int MASK_128 = 0b10000000;
    private static final int MASK_127 = 0b01111111;
    private static final int MASK_255 = 0b11111111;
    private static final int OCTET_7 = 7;
    private static final int OCTET_8 = 8;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    protected BitstreamWriter writeLong(long value, int length) {
        byte[] buffer = new byte[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = (byte) (value & MASK_255);
            value = value >> OCTET_8;
        }
        if (value != 0) {
            throw new RuntimeException("there are still values to be written, " +
                    "please check the length!");
        }
        ArrayUtils.reverse(buffer);
        try {
            baos.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    protected BitstreamWriter writeBytes(byte[] value) {
        try {
            baos.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    protected BitstreamWriter writeBigEndianLong(long value) {
        int length = calcLength(value);
        byte[] buffer = new byte[length];
        long valueTmp = value;

        for (int i = 0; i < length; i++) {
            byte blockValue = (byte) (valueTmp & MASK_127);
            if (i == 0) {
                buffer[i] = blockValue;
            } else {
                buffer[i] = (byte) (blockValue | MASK_128);
            }
            valueTmp = valueTmp >> OCTET_7;
        }

        try {
            ArrayUtils.reverse(buffer);
            baos.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    public int calcLength(long value) {
        int numberOfBits = (value == 0) ? 1 : 0;

        while (value > 0) {
            value = value >> 1;
            numberOfBits++;
        }

        int length = numberOfBits / OCTET_7;
        if ((numberOfBits % OCTET_7) != 0) {
            length++;
        }

        return length;
    }

    public void writeT(int tag) {
        writeBigEndianLong(tag);
    }

    public void writeL(long value) {
        writeBigEndianLong(calcLength(value));
    }

    public void writeL(byte[] values) {
        writeBigEndianLong(values.length);
    }

    public void writeV(long value) {
        writeBigEndianLong(value);
    }

    public void writeV(byte[] bytes) {
        writeBytes(bytes);
    }

    public void writeLv(long value, int length) {
        writeLong(value, length);
    }

    public void writeTv(long tag, long value) {
        writeBigEndianLong(tag)
                .writeBigEndianLong(value);
    }

    public void writeTlv(long tag, int length, long value) {
        writeBigEndianLong(tag)
                .writeLong(value, length);
    }

    public byte[] buildByteArray() {
        return baos.toByteArray();
    }

    public void reset() {
        baos.reset();
    }
}