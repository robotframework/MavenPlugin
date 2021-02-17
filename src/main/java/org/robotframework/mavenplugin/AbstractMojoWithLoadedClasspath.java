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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public abstract class AbstractMojoWithLoadedClasspath
        extends AbstractMojo {

    private static RobotMojoClassLoader currentMojoLoader;
    private static String ROBOT_ARTIFACT = join(File.separator, "org", "robotframework", "robotframework");

    /**
     * @parameter property="project.testClasspathElements"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    /**
     * @parameter property="settings.localRepository"
     * @required
     * @readonly
     */
    private String localRepository;

    /**
     * <p>Test are executed in a new process if this configuration is used.</p>
     * <p>The classpath for the new process will include by default all the test
     * scope dependencies from the pom.</p>
     *
     * <ul>
     *     <li>Environment variables can be added with <strong>environmentVariables</strong> map. CLASSPATH environment
     *     variable is added (prepended) to the default dependencies.</li>
     *     <li><strong>excludeDependencies</strong> can be used to exclude the test scope dependencies from the classpath of the new process.</li>
     *     <li><strong>jvmArgs</strong> can be used to specify JVM options</li>
     *     <li><strong>runnerClass</strong> can be used to specify runner class</li>
     *     <li><strong>runWithPython</strong> can be used to specify running with Python Robot Framework</li>
     * </ul>
     *
     * Example:
     * <pre>{@code <externalRunner>
     *      <environmentVariables>
     *          <foo>bar</foo>
     *          <CLASSPATH>this-should-be-seen-by-external-process.jar</CLASSPATH>
     *      </environmentVariables>
     *      <jvmArgs>
     *          <jvmArg>-XX:PermSize=128m</jvmArg>
     *          <jvmArg>-XX:MaxPermSize=256m</jvmArg>
     *          <jvmArg>-Xmx512m</jvmArg>
     *      </jvmArgs>
     *      <excludeDependencies>true</excludeDependencies>
     *      <runnerClass>org.robotframework.RobotFramework</runnerClass>
     *      <runWithPython>false</runWithPython>
     * </externalRunner>}</pre>
     * @parameter
     */
    protected ExternalRunnerConfiguration externalRunner;

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
        String configured = currentVersion();
        return join(File.separator, robots.toString(), configured, "robotframework-"+configured+".jar");
    }

    static String currentVersion() {
        PythonInterpreter interp = new PythonInterpreter();
        try {
            interp.exec("from robot import version");
            interp.exec("rf_version = version.VERSION");
            return interp.get("rf_version").toString();
        } finally {
            interp.cleanup();
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
