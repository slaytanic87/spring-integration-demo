package de.tcp.tcpdemo.controller.serializer;

import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public abstract class ASerializer<T> implements Serializer<T> {

    private BitstreamWriter bitstreamWriter = new BitstreamWriter();

    @Override
    public void serialize(T object, OutputStream outputStream) throws IOException {
        outputStream.write(serialize(object));
    }

    public byte[] serialize(T model) {
        byte[] mainData = serializeData(model, new BitstreamWriter());
        bitstreamWriter.writeV(mainData.length);
        bitstreamWriter.writeBytes(mainData);
        return bitstreamWriter.buildByteArray();
    }

    protected abstract byte[] serializeData(T message, BitstreamWriter bitstreamWriter);
}