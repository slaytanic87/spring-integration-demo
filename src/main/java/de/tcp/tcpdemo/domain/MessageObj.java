package de.tcp.tcpdemo.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Builder
@Data
public class MessageObj extends SerializedMessage {

    private int type;

    private String message;

}