package com.huaweidong.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketServerThread implements Runnable{
    /**
     * 日志
     */
    private static final Log LOGGER = LogFactory.getLog(SocketServerThread.class);

    private Socket socket;

    public SocketServerThread (Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Integer sourcePort = socket.getPort();
            int maxLen = 1024;
            byte[] contextBytes = new byte[maxLen];
            //使用线程，同样无法解决read方法的阻塞问题，
            //也就是说read方法处同样会被阻塞，直到操作系统有数据准备好
            int readLen = in.read(contextBytes, 0, maxLen);
            //读取信息
            String message = new String(contextBytes, 0, readLen);

            //下面打印信息
            SocketServerThread.LOGGER.info("服务器收到来自于端口：" + sourcePort + "的信息：" + message);

            //下面开始发送信息
            out.write("回发响应信息！".getBytes());
        } catch(Exception e) {
            SocketServerThread.LOGGER.error(e.getMessage(), e);
        } finally {
            //试图关闭
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
                if(this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException e) {
                SocketServerThread.LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
