package de.tcp.tcpdemo;

import de.tcp.tcpdemo.controller.serializer.MessageSerializer;
import de.tcp.tcpdemo.domain.MessageObj;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TcpdemoApplicationTests {

	@Test
	public void shouldConnectAndSendMessageToServer() throws IOException, InterruptedException {
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

		socket.close();
	}


}