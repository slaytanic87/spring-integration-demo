package de.tcp.tcpdemo;

import de.tcp.tcpdemo.app.TcpConnectionListener;
import de.tcp.tcpdemo.controller.ReceiverHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Log4j2
@ComponentScan(basePackages = {"de.tcp.tcpdemo.*"})
@Configuration
public class TcpServerConfiguration {

    @Value("${app.port}")
    private int port;

    @Autowired
    private TcpConnectionListener tcpConnectionListener;

    @Autowired
    private ReceiverHandler receiverHandler;

    @Bean
    public TcpNetServerConnectionFactory connectionFactory() {
        TcpNetServerConnectionFactory tcpNetServerConnectionFactory = new TcpNetServerConnectionFactory(port);
//        tcpNetServerConnectionFactory.setDeserializer(new MessageDeserializer());
//        tcpNetServerConnectionFactory.setSerializer(new MessageSerializer());
        return tcpNetServerConnectionFactory;
    }

    /******************** Server (For incoming messages) *******************/

    /**
     * Channel for downstream messages.
     * @return MessageChannel for communication with the server
     */
    @Bean
    public MessageChannel inputChannel() {
        DirectChannel dc = new DirectChannel();
        if (receiverHandler == null) {
            throw new RuntimeException("receiver handler must not be null");
        }
        dc.subscribe(receiverHandler);
        return dc;
    }

    @Bean
    public TcpReceivingChannelAdapter receiverAdapter() {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(connectionFactory());
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }

    /******************** Client (For outgoing messages) *******************/

    /**
     * Channel for upstream messages.
     * @return MessageChannel for communication with the client
     */
    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "outputChannel")
    public TcpSendingMessageHandler senderAdapter() {
        TcpSendingMessageHandler handler = new TcpSendingMessageHandler();
        handler.setConnectionFactory(connectionFactory());
        return handler;
    }

    /*********************************************************************/


    /**
     * Listener for incoming connections.
     * @return ApplicationListener
     */
    @Bean
    public ApplicationListener<TcpConnectionEvent> listener() {
        return tcpConnectionListener;
    }

}