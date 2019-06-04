package com.ssaw.commons.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/5/31 20:29
 */
public class NioServer {

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

    private Selector selector;

    public NioServer(int port) throws IOException {
        /*
         * 以下的所有说明均已linux系统底层进行说明：
         *      nio 的底层实现是 epoll 模式，采用多路复用技术，对nio的代码进行深入分析，结合epoll的底层实现
         * 进行详细的说明
         *      1.linux网络编程是两个进程之间的通信，跨集群合网络
         *      2.开启一个socket线程，在linux系统上任何操作均以文件句柄数表示，默认情况下
         *        一个线程可以打开1024个句柄，也就说最多同时支持1024个网络连接请求。阿里云默认打开65535个文件
         *        句柄，通常情况下，1G内存最多可以打开10w个句柄数
         *
         *
         */

        // 打开服务器套接字通道
        // 底层: 在linux上面开启socket服务，启动一个线程。绑定ip地址和端口号
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 检索与此通道关联的服务器套接字
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 进行服务的绑定
        serverSocket.bind(new InetSocketAddress(port));
        // 通过open()方法找到Selector
        // 底层： 开启epoll，为当前socket服务创建epoll服务，epoll_create
        selector = Selector.open();
        // 注册到selector，等待连接
        /*
         * 底层：
         *      1.将当前的epoll,服务器地址，端口号绑定,如果有连接请求，直接添加到epoll中，epoll的底层是红黑树，
         *  可以快速的实现连接的查找和状态更新。如果有新的连接过来，直接存放到epoll中。如果有连接过期，中断，
         *  会从epoll中删除。
         *      2.通过epoll_ctl添加到epoll的同时，会注册一个回调函数给内核，当网卡有数据来的时候，会通知内核，内核
         *      调用回调函数，将当前内核数据的事件状态添加到list链表中
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server Start----8888:");
    }

    /**
     * 监听
     */
    public void listen() throws IOException {
        while (true) {
            // 选择一组键，并且相应的通道已经打开
            /*
             * epoll底层维护一个链表，rdlist，基于事件驱动模式，当网卡有数据请求过来，会发起硬件中断，通知内核已经有来了。内核调用
             * 回调函数，将当前的事件添加到rdlist中，将当前可用的rdlist列表发送给用户态，用户去遍历rdlist中的事件，进行处理
             */
            selector.select();
            // 返回此选择器的已选择键集。
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 获得当前epoll的rdlist复制到用户态，遍历，同事删除当前rdlist中的事件
                iterator.remove();
                handleKey(selectionKey);
            }
        }
    }

    /**
     * 处理请求
     *
     * @param selectionKey selectionKey
     */
    private void handleKey(SelectionKey selectionKey) throws IOException {
        // 接受请求
        ServerSocketChannel server;
        SocketChannel client;
        String receiveText;
        String sendText;
        int count;
        // 测试此键的通道是否已准备好接受新的套接字连接。
        if (selectionKey.isAcceptable()) {
            // 返回为之创建此键的通道。
            server = (ServerSocketChannel) selectionKey.channel();
            // 接受到此通道套接字的连接。
            // 此方法返回的套接字通道（如果有）将处于阻塞模式。
            client = server.accept();
            // 配置为非阻塞
            client.configureBlocking(false);
            // 注册到selector，等待连接
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            // 将缓冲区清空以备下次读取
            receiveBuffer.clear();
            // 读取服务器发送来的数据到缓冲区中
            count = client.read(receiveBuffer);
            if (count > 0) {
                receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println("服务器端接受客户端数据--:" + receiveText);
                client.register(selector, SelectionKey.OP_WRITE);
            }
        } else if (selectionKey.isWritable()) {
            // 将缓冲区清空以备下次写入
            sendBuffer.clear();
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            sendText = "message from server--" + flag++;
            // 向缓冲区中输入数据
            sendBuffer.put(sendText.getBytes());
            // 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            sendBuffer.flip();
            // 输出到通道
            client.write(sendBuffer);
            System.out.println("服务器端向客户端发送数据--：" + sendText);
            client.register(selector, SelectionKey.OP_READ);
        }
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer(9090);
        nioServer.listen();
    }
}