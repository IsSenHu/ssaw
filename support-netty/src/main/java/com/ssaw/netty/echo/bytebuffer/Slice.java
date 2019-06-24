package com.ssaw.netty.echo.bytebuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author HuSen
 * create on 2019/6/24 16:18
 */
public class Slice {
    public static void main(String[] args) {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte)'J');
        // 将会成功，因为数据是共享的
        assert buf.getByte(0) == sliced.getByte(0);
    }
}
