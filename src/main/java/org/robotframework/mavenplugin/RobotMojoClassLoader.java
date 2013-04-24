package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
 * Copyright 2013 Nokia Siemens Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
