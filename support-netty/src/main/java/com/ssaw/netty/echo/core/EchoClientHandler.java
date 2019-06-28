package com.ssaw.netty.echo.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.CharBuffer;


/**
 * @author HuSen
 * @date 2019/6/5 15:57
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 在到服务器的连接已经建立之后将被调用
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("链接已建立...");
        ByteBuf buffer = ctx.channel().alloc().buffer(4 + "1000001".length() * 8);
//        buffer.writeIntLE("1000001".length());
//        buffer.writeCharSequence("1000001", CharsetUtil.UTF_8);
        buffer.writeBytes("blog.csdn.net/u014209975/article/details/53320395".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(buffer.retain());
    }

    /**
     * 当从服务器接收到一条消息时被调用
     *
     * @param channelHandlerContext channelHandlerContext
     * @param byteBuf               byteBuf
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        log.info("Client received: {}", byteBuf.toString(CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(byteBuf.retain());
    }


    /**
     * 在处理过程中引发异常时被调用
     *
     * @param ctx   ChannelHandlerContext
     * @param cause Throwable
     * @throws Exception Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常:", cause);
        ctx.close();
    }

    public static void main(String[] args) {
        byte[] rst = new byte[4];
        // 先写int的最后一个字节
        rst[0] = (byte)(0x3e9 & 0xFF);
        // int 倒数第二个字节
        rst[1] = (byte)((0x3e9 & 0xFF00) >> 8 );
        // int 倒数第三个字节
        rst[2] = (byte)((0x3e9 & 0xFF0000) >> 16 );
        // int 第一个字节
        rst[3] = (byte)((0x3e9 & 0xFF000000) >> 24 );
        ByteBuf byteBuf = Unpooled.copiedBuffer(rst);
        System.out.println(byteBuf.readCharSequence(byteBuf.readIntLE(), CharsetUtil.UTF_8));
    }
}