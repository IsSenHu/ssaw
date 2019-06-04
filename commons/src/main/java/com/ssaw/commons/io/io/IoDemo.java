package com.ssaw.commons.io.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * @author HuSen
 * @date 2019/5/31 18:09
 */
public class IoDemo {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("准备接收数据");
        Socket clientSocket = serverSocket.accept();
        System.out.println("接收数据完毕");

        // 处理完一个请求才能处理下一个请求
        while (true) {
            String resp = new DataInputStream(clientSocket.getInputStream()).readUTF();
            System.out.println("接收到结果: " + resp);
            new DataOutputStream(clientSocket.getOutputStream()).writeUTF("end");
            if ("down".equals(resp)) {
                break;
            }
            Thread.sleep(10000);
        }
        serverSocket.close();
        clientSocket.close();
    }

    private static String processRequest(String request) {
        if (Objects.isNull(request)) {
            return "who are you";
        }
        return request.concat(" you are beatify");
    }

    private static class Client {

        public static void main(String[] args) throws Exception {
            Socket client = new Socket("127.0.0.1", 8080);
            System.out.println("远程主机地址: " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应: " + in.readUTF());
            client.close();
        }
    }
}