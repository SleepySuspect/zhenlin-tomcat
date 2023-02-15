package com.zhenlin;

import java.net.URL;
import java.net.URLClassLoader;

public class WebappClassLoader extends URLClassLoader {
    public WebappClassLoader(URL[] urls) {
        super(urls);
    }
}
