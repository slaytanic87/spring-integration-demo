package de.tcp.tcpdemo.controller.serializer;

import de.tcp.tcpdemo.domain.MessageObj;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
public class SerializerUnitTest {

    private MessageSerializer messageSerializer;

    @Before
    public void setup() {
        messageSerializer = new MessageSerializer();
    }

    @Test
    public void shouldSerializeMessage() {
        // given
        MessageObj message = MessageObj.builder()
                .message("Hello World").type(2).build();
        // when
        byte[] serialized = messageSerializer.serialize(message);

        // then
        Assertions.assertThat(serialized.length).isEqualTo(17);
    }

    @Test
    public void test() throws IOException {
        // given
        Socket socket = SocketFactory.getDefault().createSocket("localhost", 10000);
        MessageObj messageObj = MessageObj.builder().message("Hello server").type(2).build();

        // when
        MessageSerializer serializer = new MessageSerializer();
        byte[] serialized = serializer.serialize(messageObj);
        socket.getOutputStream().write(serialized);

        // then
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        while (bufferedReader.ready()) {
            bufferedReader.read();
        }

        //socket.close();
    }
}