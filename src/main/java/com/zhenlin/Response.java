package com.zhenlin;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response extends AbstractHttpServletResponse{
    private int status = 200;
    private String message = "ok";

    private final byte SP = ' ';
    private final byte CR = '\r';
    private final byte LF = '\n';
    private Map<String, String> headers = new HashMap<>();

    private Request request;
    private OutputStream socketOutputStream;

    private ResponseServletOutputStream responseServletOutputStream = new ResponseServletOutputStream();

    public Response(Request request) {
        this.request = request;
        try {
            this.socketOutputStream = request.getSocket().getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStatus(int i, String s) {
        status = i;
        message = s;
    }

    @Override
    public void setHeader(String s, String s1) {
        headers.put(s, s1);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ResponseServletOutputStream getOutputStream() throws IOException {
        return responseServletOutputStream;
    }

    @Override
    public void addHeader(String s, String s1) {
        headers.put(s, s1);
    }

    public void complete() throws Exception {
        //发送响应行
        sendResponseLine();

        //发送响应头
        sendResponseHeader();

        //发送响应体
        sendResponseBody();
    }

    private void sendResponseLine() throws Exception{
        socketOutputStream.write(request.getProtocol().getBytes());
        socketOutputStream.write(SP);

        socketOutputStream.write(status);
        socketOutputStream.write(SP);

        socketOutputStream.write(message.getBytes());
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }

    private void sendResponseHeader() throws Exception{

        if(!headers.containsKey("Content-Length")){
            headers.put("Content-Length", String.valueOf(getOutputStream().getPos()));
        }

        if(!headers.containsKey("Content-Type")){
            headers.put("Content-Type", "text/plain;charset=utf-8");
        }

        for (Map.Entry<String, String> entry :
                headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            socketOutputStream.write(key.getBytes());
            socketOutputStream.write(":".getBytes());
            socketOutputStream.write(value.getBytes());
            socketOutputStream.write(CR);
            socketOutputStream.write(LF);
        }
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }

    private void sendResponseBody() throws Exception{
        socketOutputStream.write(getOutputStream().getBytes());
    }


}
