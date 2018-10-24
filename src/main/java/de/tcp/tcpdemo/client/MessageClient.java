package de.tcp.tcpdemo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Component
public class MessageClient {

    private MessageChannel outputChannel;

    @Lazy
    @Value("#{outputChannel}")
    public void setChannel(DirectChannel channel) {
        this.outputChannel = channel;
    }

    public <T> void sendMessage(T message, String connectionId) {
        outputChannel.send(MessageBuilder.withPayload(message)
                .setHeader(IpHeaders.CONNECTION_ID, connectionId)
                .build());
    }
}