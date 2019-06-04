package com.ssaw.commons.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/5/31 20:08
 */
public class NioClient {

    /**
     * 标识数字
     */
    private static int flag = 0;

    /**
     * 缓冲区大小
     */
    private static int block = 4096;

    /**
     * 接受数据缓冲区
     */
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(block);

    /**
     * 发送数据缓冲区
     */
    private static ByteBuffer receiveBuffer = ByteBuffer.allocate(block);

    /**
     * 服务器端地址
     */
    private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("localhost", 9090);


    public static void main(String[] args) throws Exception {
        // 打开socket通道
        SocketChannel channel = SocketChannel.open();
        // 设为非阻塞
        channel.configureBlocking(false);
        // 打开多路复用器
        Selector selector = Selector.open();
        // 注册连接服务端socket动作
        channel.register(selector, SelectionKey.OP_CONNECT);
        // 连接
        channel.connect(SERVER_ADDRESS);
        // 分配缓冲区大小内存
        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterator;
        SelectionKey selectionKey;
        SocketChannel client;
        String receiveText;
        String sendText;
        int count;

        LOOP:
        while (true) {
            // 选择一组键，其相应的通道已为 I/O 操作准备就绪。
            // 此方法执行处于阻塞模式的选择操作。
            selector.select();
            // 返回此选择器的已选择键集。
            selectionKeys = selector.selectedKeys();
            iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                selectionKey = iterator.next();
                if (selectionKey.isConnectable()) {
                    System.out.println("client connect");
                    client = (SocketChannel) selectionKey.channel();
                    // 判断此通道上是否正在进行连接操作。
                    // 完成套接字通道的连接过程。
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        System.out.println("完成连接!");
                        sendBuffer.clear();
                        sendBuffer.put("Hello,Server".getBytes());
                        sendBuffer.flip();
                        client.write(sendBuffer);
                    }
                    client.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    client = (SocketChannel) selectionKey.channel();
                    // 将缓冲区清空以备下次读取
                    receiveBuffer.clear();
                    count = client.read(receiveBuffer);
                    if (count > 0) {
                        receiveText = new String(receiveBuffer.array(), 0, count);
                        if ("down".equals(receiveText)) {
                            break LOOP;
                        }
                        System.out.println("客户端接受服务器端数据--:" + receiveText);
                        client.register(selector, SelectionKey.OP_WRITE);
                    }
                } else if (selectionKey.isWritable()) {
                    sendBuffer.clear();
                    client = (SocketChannel) selectionKey.channel();
                    sendText = "message from client--" + (flag++);
                    sendBuffer.put(sendText.getBytes());
                    // 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                    sendBuffer.flip();
                    client.write(sendBuffer);
                    System.out.println("客户端向服务器端发送数据--：" + sendText);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }
    }
}