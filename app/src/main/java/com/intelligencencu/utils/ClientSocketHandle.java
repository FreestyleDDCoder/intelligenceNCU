package com.helloncu.houseinformationsystem.utils;

import java.io.*;
import java.net.Socket;

/**
 * Created by liangzhan on 18-3-16.
 * 这是客户端socket助手
 */
public class ClientSocketHandle {
    //设置发送ip地址和socket端口号
    private String url = "192.168.1.104";
    private int port = 8888;

    public String sendMessage(String message) {
        String result = null;
        try {
            //这里开始把流传输给esb,建立socket通信
            Socket socket = new Socket(url, port);
            //打开socket传输通道
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8")), true);
            //socket接受（BufferedReader相当于管道，然后把流写入其中，其中BufferedReader有个相当于阀门的机制，只能单工操作）
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            //往socket通道写入消息
            pw.println(message);
            System.out.println("客户端往ESB成功发送信息：" + message);
            //接收从socket通道后端返回的结果(阻塞机制)
            String readLine = br.readLine();
            System.out.println("接收到ESB返回的消息：" + readLine);
            result = readLine;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
