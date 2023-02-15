package com.zhenlin;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private String appName;
    private Map<String, Servlet> urlPatternMapping = new HashMap<>();

    public Context(String appName) {
        this.appName = appName;
    }

    public Servlet getByUrlPattern(String urlPattern) {
        for (String key : urlPatternMapping.keySet()) {
            if (key.contains(urlPattern)){
                return urlPatternMapping.get(key);
            }
        }
        return null;
    }

    public void addUrlPatternMapping(String urlPattern, Servlet servlet) {
        urlPatternMapping.put(urlPattern, servlet);
    }
}
