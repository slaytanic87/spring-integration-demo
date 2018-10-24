package de.tcp.tcpdemo.app;

import de.tcp.tcpdemo.client.MessageClient;
import de.tcp.tcpdemo.controller.serializer.MessageSerializer;
import de.tcp.tcpdemo.domain.MessageObj;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionExceptionEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.stereotype.Component;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Log4j2
@Component
public class TcpConnectionListener implements ApplicationListener<TcpConnectionEvent> {

    @Autowired
    private SessionService connections;

    @Autowired
    private MessageClient messageClient;

    @Override
    public void onApplicationEvent(TcpConnectionEvent event) {
        if (event instanceof TcpConnectionOpenEvent) {
            log.info("create connection for {}", event.getConnectionId());
            connections.getConnections().add(event.getConnectionId());
//            messageClient.sendMessage("welcome " + event.getConnectionId(), event.getConnectionId());
            MessageObj msgObj = MessageObj.builder().message("Hello Client").type(1).build();
            MessageSerializer serializer = new MessageSerializer();
            messageClient.sendMessage(serializer.serialize(msgObj), event.getConnectionId());
        } else if (event instanceof TcpConnectionCloseEvent) {
            log.info("disconnect {}", event.getConnectionId());
            connections.getConnections().remove(event.getConnectionId());
        } else if (event instanceof TcpConnectionExceptionEvent) {
            log.info("connection error {}", event.getCause());
            connections.getConnections().remove(event.getConnectionId());
        } else {
            log.info("event {}", event);
        }
    }
}