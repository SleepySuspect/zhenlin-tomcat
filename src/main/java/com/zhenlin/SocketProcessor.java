package com.zhenlin;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable{

    private Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        processSocket(socket);
    }

    private void processSocket(Socket socket) {
        //处理socket连接
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);

            //解析请求方法
            int pos = 0;
            int begin = 0, end = 0;
            for (; pos < bytes.length; pos ++, end ++){
                if (bytes[pos] == ' ')
                    break;
            }
            StringBuilder method = new StringBuilder();
            for (; begin < end; begin ++){
                method.append((char) bytes[begin]);
            }

            //解析url
            pos ++;
            begin ++;
            end ++;
            StringBuilder url = new StringBuilder();
            for (; pos < bytes.length; pos ++, end ++){
                if (bytes[pos] == ' ')
                    break;
            }
            for (; begin < end; begin ++){
                url.append((char) bytes[begin]);
            }

            //解析协议版本
            pos ++;
            begin ++;
            end ++;
            StringBuilder protocl = new StringBuilder();
            for (; pos < bytes.length; pos ++, end ++){
                if (bytes[pos] == '\n')
                    break;
            }
            for (; begin < end; begin ++){
                protocl.append((char) bytes[begin]);
            }
            Request request = new Request(method.toString(), url.toString(), protocl.toString(), socket);
            Response response = new Response(request);
            ZhenlinServlet zhenlinServlet = new ZhenlinServlet();

            zhenlinServlet.service(request, response);

            //发送响应
            response.complete();


//            //解析字符流
//            for (byte b :
//                    bytes) {
//                System.out.print((char) b);
//            }
//            socket.getOutputStream().write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
