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

    protected void addFileToArguments(List<String> arguments, File file, String flag) {
        if (isFileValid(file)) {
            arguments.add(flag);
            arguments.add(!file.getName().toUpperCase().equals("NONE") ? file.getPath() : file.getName());
        }
    }

    protected boolean isFileValid(File file) {
        return file != null && file.getPath() != null && !file.getPath().equals("");
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

    protected void addNonEmptyStringToArguments(List<String> arguments, String variableToAdd, String flag) {
        if (!StringUtils.isEmpty(variableToAdd)) {
            addStringToArguments(arguments, variableToAdd, flag);
        }
    }

    protected void addFlagToArguments(ArrayList<String> arguments, boolean flag, String argument) {
        if (flag == true) {
            arguments.add(argument);
        }

    }

    protected void addStringToArguments(List<String> arguments, String variableToAdd, String flag) {
        arguments.add(flag);
        arguments.add(variableToAdd);

    }

    protected void addListToArguments(List<String> arguments, List<String> variablesToAdd, String flag) {
        if (variablesToAdd == null) {
            return;
        }

        for (String variableToAdd : variablesToAdd) {
            if (!StringUtils.isEmpty(variableToAdd)) {
                arguments.add(flag);
                arguments.add(variableToAdd);
            }
        }
    }

    protected void addFileListToArguments(List<String> arguments, List<File> variablesToAdd, String flag) {
        if (variablesToAdd == null) {
            return;
        }

        for (File variableToAdd : variablesToAdd) {
            addFileToArguments(arguments, variableToAdd, flag);
        }
    }

}
