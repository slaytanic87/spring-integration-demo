package de.tcp.tcpdemo.controller.deserializer;

import org.springframework.core.serializer.Deserializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public abstract class ADeserializer<T> implements Deserializer<T> {

    protected BitstreamReader bitstreamReader;

    public T deserialize(InputStream inputStream) throws IOException {
        return deserialize(toByteArrayFromStream(inputStream));
    }

    public static byte[] toByteArrayFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = is.read();
        while(reads != -1) {
            baos.write(reads); reads = is.read();
        }
        return baos.toByteArray();
    }



    public T deserialize(byte[] byteStream) {
        bitstreamReader = new BitstreamReader(byteStream);

        T deserializedObject = createDeserializedObject();

        int length = bitstreamReader.readL();
        byte[] mainData = bitstreamReader.readBytes(length);

        bitstreamReader.checkRemainingBytesAndThrowError();

        BitstreamReader reader = new BitstreamReader(mainData);
        while (reader.getByteStreamBuffer().available() > 0) {
            int tag = reader.readT();
            readTag(tag, deserializedObject, reader);
        }

        reader.checkRemainingBytesAndThrowError();
        return deserializedObject;
    }

    protected abstract T createDeserializedObject();

    protected abstract void readTag(int tag, T deserialized, BitstreamReader bitstreamReader);
}