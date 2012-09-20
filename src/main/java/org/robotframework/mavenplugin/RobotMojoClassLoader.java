package org.robotframework.mavenplugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Arrays;
import java.util.List;


public class RobotMojoClassLoader extends URLClassLoader {

    public RobotMojoClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public RobotMojoClassLoader(URL[] urls) {
        super(urls);
    }

    public RobotMojoClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public void addIfNotAlready(URL url) {
        URL[] urls = getURLs();
        if (!Arrays.asList(urls).contains(url))
            super.addURL(url);
    }

    public void append(List<URL> urls) {
        for (URL url: urls)
            addIfNotAlready(url);
    }
}
