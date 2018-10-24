package de.tcp.tcpdemo.controller;

import de.tcp.tcpdemo.app.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Log4j2
@Component
public class ReceiverHandler implements MessageHandler {

    @Autowired
    private SessionService connections;


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.info("Read following from client: {}", message.getPayload());

    }

    private static String hexDumpToByteArray(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }
}