package com.ssaw.netty.echo.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author HuSen
 * @date 2019/6/11 10:54
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    private final static int INT_BYTES = 4;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        while (msg.readableBytes() >= INT_BYTES) {
            int value = Math.abs(msg.readInt());
            out.add(value);
        }
    }
}