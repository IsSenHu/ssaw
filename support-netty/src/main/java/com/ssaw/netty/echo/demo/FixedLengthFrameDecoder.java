package com.ssaw.netty.echo.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author HuSen
 * @date 2019/6/11 10:35
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    /**
     * 帧的长度
     * */
    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 检查是否有足够的字节可以被读取，以生成下一个帧
        while (in.readableBytes() >= frameLength) {
            // 从ByteBuf中读取新帧
            ByteBuf byteBuf = in.readBytes(frameLength);
            // 将该帧添加到已被解码的消息列表中
            out.add(byteBuf);
        }
    }
}