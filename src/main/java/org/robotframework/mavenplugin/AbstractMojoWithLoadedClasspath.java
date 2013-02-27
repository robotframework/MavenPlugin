package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
 * Copyright 2012 Nokia Siemens Networks Oyj
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public abstract class AbstractMojoWithLoadedClasspath
        extends AbstractMojo {

    private static RobotMojoClassLoader currentMojoLoader;
    private static String ROBOT_ARTIFACT = join(File.separator, "org", "robotframework", "robotframework");

    /**
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    /**
     * @parameter expression="${settings.localRepository}"
     * @required
     * @readonly
     */
    private String localRepository;

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
            updateMojoLoader(urls);
            Thread.currentThread().setContextClassLoader(currentMojoLoader);
        }
    }

    protected String getClassPathString() {
        if (localRepository ==null || classpathElements==null) {
            // when executed outside of maven (like in unit tests)
            return System.getProperty("java.class.path");
        }
        String result= getRobotJar();
        for (String elem: classpathElements) {
            result +=File.pathSeparator + elem;
        }
        return result;
    }

    protected String getRobotJar() {
        File robots = new File(localRepository, ROBOT_ARTIFACT);
        String latest = latestVersion(robots.list());
        return join(File.separator, robots.toString(), latest, "robotframework-"+latest+".jar");
    }

    static String latestVersion(String[] strings) {
        Version[] versions = new Version[strings.length];
        for (int i=0; i<strings.length; i++)
            versions[i] = new Version(strings[i]);
        Arrays.sort(versions);
        return versions[versions.length-1].string;
    }

    static class Version implements Comparable<Version>{

        int[] version;
        String string;

        Version(String versionString) {
            string = versionString;
            String[] split = versionString.split("\\.");
            version = new int[split.length];
            for (int i = 0; i<version.length; i++) {
                try {
                    version[i] = Integer.parseInt(split[i]);
                } catch (NumberFormatException nfe) {
                    version = new int[0];
                }
            }
        }

        public boolean equals(Object other) {
            if (!(other instanceof Version))
                return false;
            return compareTo((Version)other) == 0;
        }

        public int compareTo(Version other) {
            for (int i=0; i < Math.max(version.length, other.version.length); i++) {
                int difference = numberAt(i) - other.numberAt(i);
                if (difference != 0)
                    return difference;
            }
            return 0;
        }

        private int numberAt(int index) {
            return index < version.length ? version[index] : 0;
        }
    }

    protected static String join(String joiner, String... elements) {
        StringBuilder result = new StringBuilder();
        for (String elem: elements) {
            result.append(elem).append(joiner);
        }
        return result.substring(0, result.length()-joiner.length());
    }

    private void updateMojoLoader(List<URL> urls) {
        if (currentMojoLoader==null)
            currentMojoLoader = new RobotMojoClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
        else
            currentMojoLoader.append(urls);
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
