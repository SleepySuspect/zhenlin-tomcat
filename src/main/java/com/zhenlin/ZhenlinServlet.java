package com.zhenlin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpCookie;

public class ZhenlinServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod());

        resp.addHeader("Content-Length", "11");
        resp.addHeader("Content-Type", "text/plain;charset=utf-8");
        resp.getOutputStream().write("hello world".getBytes());


    }
}
