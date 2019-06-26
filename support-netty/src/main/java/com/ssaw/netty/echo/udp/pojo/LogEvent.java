package com.ssaw.netty.echo.udp.pojo;

import lombok.Data;

import java.net.InetSocketAddress;

/**
 * @author HuSen
 * create on 2019/6/25 18:14
 */
@Data
public final class LogEvent {
    public static final byte SEPARATOR = (byte)':';

    private final InetSocketAddress source;

    private final String logFile;

    private final String msg;

    private final Long received;

    public LogEvent(String logFile, String msg) {
        this(null, logFile, msg, -1L);
    }

    public LogEvent(InetSocketAddress source, String logFile, String msg, Long received) {
        this.source = source;
        this.logFile = logFile;
        this.msg = msg;
        this.received = received;
    }
}
