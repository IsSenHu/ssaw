package com.ssaw.netty.echo.bytebuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author HuSen
 * create on 2019/6/24 14:52
 */
public class Test {
    public static void main(String[] args) {
        ByteBuf heapBuf = Unpooled.buffer();
        heapBuf.writeBytes("你好啊".getBytes());
        // 检查ByteBuf是否有一个支撑数组, 如果不是，则这是一个直接缓冲区
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            System.out.println();
        }
    }
}
