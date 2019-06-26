package com.ssaw.netty.echo.udp.decoder;

import com.ssaw.netty.echo.udp.pojo.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author HuSen
 * create on 2019/6/26 13:26
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        ByteBuf data = datagramPacket.content();
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        String fileName = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        String msg = data.slice(idx + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEvent event = new LogEvent(datagramPacket.sender(), fileName, msg, System.currentTimeMillis());
        out.add(event);
    }
}
