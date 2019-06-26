package com.ssaw.netty.echo.udp.handler;

import com.ssaw.netty.echo.udp.pojo.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author HuSen
 * create on 2019/6/26 13:39
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) {
        String stringBuilder = msg.getReceived() +
                " [" +
                msg.getSource().toString() +
                "] [" +
                msg.getLogFile() +
                "] : " +
                msg.getMsg();
        System.out.println(stringBuilder);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
