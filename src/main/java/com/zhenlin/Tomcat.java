package com.zhenlin;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Tomcat {

    private Map<String, Context> contextMap = new HashMap<>();

    public void start(){
        //socket连接
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            ServerSocket serverSocket = new ServerSocket(8080);


            while (true){
                Socket socket = serverSocket.accept();
                executorService.execute(new SocketProcessor(socket, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
        Tomcat tomcat = new Tomcat();

        tomcat.deployApps();
        tomcat.start();
    }

    //部署所有应用
    private void deployApps() throws ClassNotFoundException, MalformedURLException {
        String webApps = System.getProperty("user.dir") + "\\" + "webapps";
        File webapps = new File(webApps);
        for (String app :
                webapps.list()) {
            deployApp(webApps, app);
        }
    }

    //部署单个应用
    private void deployApp(String webapps, String appName) throws ClassNotFoundException, MalformedURLException {
        Context context = new Context(appName);

        //找到所有servlet
        File appDirectory = new File(webapps, appName);
        File classesDir = new File(appDirectory, "classes");

        //所有文件   【都是class文件】
        List<File> allFilePath = getAllFilePath(classesDir);

        for (File clazz :
                allFilePath) {
            String name = clazz.getPath();
            name = name.replace(classesDir + "\\", "");
            name = name.replace(".class", "");
            name = name.replace("\\", ".");

//            Class<?> servletClazz = Thread.currentThread().getContextClassLoader().loadClass(name);
            WebappClassLoader classLoader = new WebappClassLoader(new URL[]{classesDir.toURL()});

            //加载类
            Class<?> servletClazz = classLoader.loadClass(name);
            //判断是不是servlet
            if (HttpServlet.class.isAssignableFrom(servletClazz)) {
                WebServlet annotation = servletClazz.getAnnotation(WebServlet.class);
                String[] urlPatterns = annotation.urlPatterns();
                for (String urlPattern :
                        urlPatterns) {
                    try {
                        context.addUrlPatternMapping(urlPattern, (Servlet) servletClazz.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        contextMap.put(appName, context);
    }

    //递归找到文件夹下所有的文件
    public List<File> getAllFilePath(File srcFile){
        ArrayList<File> result = new ArrayList<>();
        File[] files = srcFile.listFiles();

        if (files != null){
            for (File file :
                    files) {
                if (file.isDirectory()){
                    //是文件夹就遍历添加所有下属文件
                    result.addAll(getAllFilePath(file));
                }else {
                    result.add(file);
                }
            }
        }
        return result;
    }


    public Map<String, Context> getContextMap() {
        return contextMap;
    }
}
