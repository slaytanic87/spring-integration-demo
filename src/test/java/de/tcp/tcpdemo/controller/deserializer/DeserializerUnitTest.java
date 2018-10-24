package de.tcp.tcpdemo.controller.deserializer;

import de.tcp.tcpdemo.controller.serializer.MessageSerializer;
import de.tcp.tcpdemo.domain.MessageObj;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class DeserializerUnitTest {

    MessageSerializer messageSerializer;
    MessageObj message;
    @Before
    public void setup() {
        messageSerializer = new MessageSerializer();
        message = MessageObj.builder()
                .message("Hello World")
                .type(1).build();
    }

    @Test
    public void shouldDeserializeMessage() {
        // given
        byte[] serializedMessage = messageSerializer.serialize(message);
        MessageDeserializer messageDeserializer = new MessageDeserializer();

        // when
        MessageObj deserialized = messageDeserializer.deserialize(serializedMessage);

        // then
        Assertions.assertThat(deserialized.getMessage()).isEqualToIgnoringCase(message.getMessage());
        Assertions.assertThat(deserialized.getType()).isEqualTo(message.getType());
    }



}
