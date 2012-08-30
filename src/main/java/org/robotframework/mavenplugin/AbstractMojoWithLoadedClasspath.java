package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMojoWithLoadedClasspath
        extends AbstractMojo {

    /**
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    public void execute()
            throws MojoExecutionException, MojoFailureException {
        loadClassPath();
        subclassExecute();
    }

    protected abstract void subclassExecute()
            throws MojoExecutionException, MojoFailureException;

    private void loadClassPath()
            throws MojoExecutionException {
        List<URL> urls = new ArrayList<URL>();

        if (classpathElements != null) {
            for (String element : classpathElements) {
                File elementFile = new File(element);
                try {
                    urls.add(elementFile.toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException("Classpath loading error: " + element);
                }
            }
        }

        if (urls.size() > 0) {
            ClassLoader realm = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
            Thread.currentThread().setContextClassLoader(realm);
        }
    }

    public File makeAbsolute(File folder, File file) {
        final File output;
        if (file.isAbsolute()) {
            output = file;
        } else {
            output = new File(folder, file.getName());
        }
        return output;
    }



}
