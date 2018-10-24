package de.tcp.tcpdemo.controller.deserializer;

import de.tcp.tcpdemo.domain.MessageObj;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class MessageDeserializer extends ADeserializer<MessageObj> {

    private static final int TAG_TYPE = 1;
    private static final int TAG_MESSAGE = 2;

    /**
     * Is needed to store the value during the deserialization.
     * @return Message object
     */
    @Override
    public MessageObj createDeserializedObject() {
        return MessageObj.builder().build();
    }

    @Override
    public void readTag(int tag, MessageObj deserialized, BitstreamReader bitstreamReader) {
        switch (tag) {
            case TAG_TYPE:
                int typeLength = bitstreamReader.readL();
                long value = bitstreamReader.readV(typeLength);
                deserialized.setType((int) value);
                break;
            case TAG_MESSAGE:
                int messageLength = bitstreamReader.readL();
                byte[] messageByte = bitstreamReader.readBytes(messageLength);
                deserialized.setMessage(new String(messageByte));
                break;
            default:
                throw new RuntimeException("Unknown tag in " + this.getClass().getName());
        }
    }
}