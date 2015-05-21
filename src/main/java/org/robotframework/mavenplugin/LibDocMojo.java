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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.robotframework.RobotFramework;

/**
 * Create documentation of test libraries or resource files using the Robot Framework <code>libdoc</code> tool.
 * <p/>
 * Uses the <code>libdoc</code> bundled in Robot Framework jar distribution. For more help see
 * <a href="http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#library-documentation-tool-libdoc">libdoc documentation</a>.
 *
 * @goal libdoc
 * @requiresDependencyResolution test
 */
public class LibDocMojo
        extends AbstractMojoWithLoadedClasspath {

    protected void subclassExecute()
            throws MojoExecutionException, MojoFailureException {
        try {
            runLibDoc();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to execute libdoc script: " + e.getMessage());
        }
    }

    public void runLibDoc()
            throws IOException {
        libdoc.populateDefaults(this);
        libdoc.ensureOutputDirectoryExists();
        
        if (projectBaseDir == null) {
        	projectBaseDir = new File("");
        }
        List<String[]> runArgs = libdoc.generateRunArguments(projectBaseDir);
        //Run all libdoc calls one after another
        for (String[] args: runArgs) {
        	RobotFramework.run(args);
        }
    }

    /**
     * Library documentation configuration.
     *
     * Required settings:
     * <ul>
     * <li><code>outputFile</code>          The name for the output file. Documentation output format is deduced from the file extension. 
     *                                      We also support patterns like {@code *.html}, which indicates to derive the output name from the original name.</li>
     * <li><code>libraryOrResourceFile</code>     Name or path of the documented library or resource file. Supports ant-like pattern format to match multiple inputs, such as <code>src/java/**{@literal /}*.java</code>
     * <p/>
     * Name must be in the same format as when used in Robot Framework test data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is imported the same as when running the tests.
     * Use extraPathDirectories to set PYTHONPATH/CLASSPATH accordingly.
     * <p/>
     * Paths are considered relative to the location of <code>pom.xml</code> and must point to a valid Python/Java
     * source file or a resource file. For example <code>src/main/python/test/ExampleLib.py</code>
     * <p/>
     * Note that you should preferably import java classes by classname, not path. Dynamic libraries will not be compiled correctly with path.</li>
     * </ul>
     * Optional settings:
     * <ul>
     * <li><code>outputDirectory</code>     Specifies the directory where documentation files are written.
     *                                      Considered relative to the ${basedir} of the project, but also supports absolute paths.
     *                                      Defaults to ${project.build.directory}/robotframework/libdoc</li>
     * <li><code>name</code>                Sets the name of the documented library or resource.</li>
     * <li><code>version</code>             Sets the version of the documented library or resource.</li>
     * <li><code>extraPathDirectories</code> A directory to be added to the PYTHONPATH/CLASSPATH when creating documentation.
     * e.g. src/main/java/com/test/</li>
     * </ul>
     *
     * Example 1:
     * <pre><![CDATA[<libdoc>
     *      <outputFile>MyLib.html</outputFile>
     *      <libraryOrResourceFile>com.mylib.MyLib</libraryOrResourceFile>
     * </libdoc>]]></pre>
     *
     * Example 2:
     * <pre><![CDATA[<libdoc>
     *      <outputFile>*.html</outputFile>
     *      <libraryOrResourceFile>src/java/**{@literal /}*Lib.java</libraryOrResourceFile>
     * </libdoc>]]></pre>

     * Example 3:
     * <pre><![CDATA[<libdoc>
     *      <outputFile>*.html</outputFile>
     *      <libraryOrResourceFile>com.**.*Lib</libraryOrResourceFile>
     * </libdoc>]]></pre>
     * @parameter
     * @required
     */
    private LibDocConfiguration libdoc;

    /**
     * Default output directory. Effective if outputDirectory is empty. Cannot be overridden.
     *
     * @parameter default-value="${project.build.directory}/robotframework/libdoc"
     * @readonly
     */
    File defaultLibdocOutputDirectory;

    /**
     * The default location where extra packages will be searched. Effective if extraPathDirectories attribute is not
     * used. Cannot be overridden.
     *
     * @parameter default-value="${project.basedir}/src/test/resources/robotframework/libraries"
     * @readonly
     */
    File libdocDefaultExtraPath;
    
    /**
     * The base dir of the project.
     * @parameter default-value="${project.basedir}"
     * @readonly 
     */
    File projectBaseDir;

}

