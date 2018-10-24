package de.tcp.tcpdemo.controller.serializer;

import de.tcp.tcpdemo.domain.MessageObj;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class MessageSerializer extends ASerializer<MessageObj> {

    private static final int TAG_TYPE = 1;
    private static final int TAG_MESSAGE = 2;


    @Override
    public byte[] serializeData(MessageObj message, BitstreamWriter bitstreamWriter) {

        bitstreamWriter.writeT(TAG_TYPE);
        bitstreamWriter.writeL(message.getType());
        bitstreamWriter.writeV(message.getType());

        bitstreamWriter.writeT(TAG_MESSAGE);
        byte[] messageAsBytes = message.getMessage().getBytes();
        bitstreamWriter.writeL(messageAsBytes);
        bitstreamWriter.writeBytes(messageAsBytes);

        return bitstreamWriter.buildByteArray();
    }
}