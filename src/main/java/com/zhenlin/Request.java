package com.zhenlin;

import java.net.Socket;

public class Request extends AbstractHttpServletRequest{
    private String method;
    private String url;
    private String protocol;

    private Socket socket;


    public Request(String method, String url, String protocl, Socket socket) {
        this.method = method;
        this.url = url;
        this.protocol = protocl;
        this.socket = socket;
    }

    public String getMethod() {
        return method;
    }

    public StringBuffer getRequestUrl() {
        return new StringBuffer(url);
    }

    public String getProtocol() {
        return protocol;
    }

    public Socket getSocket() {
        return socket;
    }
}
