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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;
import org.robotframework.RobotFramework;

/**
 * Create documentation of test libraries or resource files using the Robot Framework <code>libdoc</code> tool.
 * <p/>
 * Uses the <code>libdoc</code> bundled in Robot Framework jar distribution. For more help, run
 * <code>java -jar robotframework.jar libdoc --help</code>.
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
        ensureOutputDirectoryExists();
        RobotFramework.run(generateRunArguments());
    }

    private void ensureOutputDirectoryExists()
            throws IOException {
        if (outputDirectory == null) {
            outputDirectory =
                    new File(joinPaths(System.getProperty("basedir"), "target", "robotframework", "libdoc"));
        }

        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                throw new IOException("Target output directory cannot be created: "
                        + outputDirectory.getAbsolutePath());
            }
        }
    }

    private String joinPaths(String... parts) {
        return StringUtils.join(parts, File.separator);
    }

    private String[] generateRunArguments() {
        ArrayList<String> generatedArguments = new ArrayList<String>();
        generatedArguments.add("libdoc");
        addNonEmptyStringToArguments(generatedArguments, name, "--name");
        addNonEmptyStringToArguments(generatedArguments, format, "--format");
        addFileListToArguments(generatedArguments, getExtraPathDirectoriesWithDefault(), "--pythonpath");
        generatedArguments.add(getLibraryOrResource());
        generatedArguments.add(getOutputPath());
        return generatedArguments.toArray(new String[generatedArguments.size()]);
    }

    private String getLibraryOrResource() {
        File libOrResource = new File(libraryOrResourceFile);
        if (libOrResource.exists()) {
            return libOrResource.getAbsolutePath();
        } else {
            return libraryOrResourceFile;
        }

    }

    private String getOutputPath() {
        return outputDirectory + File.separator + outputFile.getName();
    }

    private List<File> getExtraPathDirectoriesWithDefault() {
        if (extraPathDirectories == null) {
            return Collections.singletonList(defaultExtraPath);
        } else {
            return Arrays.asList(extraPathDirectories);
        }
    }

    /**
     * The format of the created documentation. May be either <code>HTML</code> or <code>XML</code>.
     * <p/>
     * If not specified, the format is gotten from the extension of the {@link #outputFile}.
     *
     * @parameter expression="${format}"
     */

    private String format;

    /**
     * Specifies the directory where documentation files are written. Considered to be relative to the ${basedir} of the
     * project.
     *
     * @parameter expression="${output}" default-value="${project.build.directory}/robotframework/libdoc"
     */
    private File outputDirectory;

    /**
     * Specifies the filename of the created documentation. Considered to be relative to the {@link #outputDirectory} of
     * the project.
     *
     * @parameter expression="${outputFile}"
     * @required
     */
    private File outputFile;

    /**
     * Sets the name of the documented library or resource.
     *
     * @parameter expression="${name}"
     */
    private String name;

    /**
     * Name or path of the documented library or resource file.
     * <p/>
     * Name must be in the same format as when used in Robot Framework test data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is imported the same as when running the tests.
     * Use {@link #extraPathDirectories} to set PYTHONPATH/CLASSPATH accordingly.
     * <p/>
     * Paths are considered relative to the location of <code>pom.xml</code> and must point to a valid Python/Java
     * source file or a resource file. For example <code>src/main/java/com/test/ExampleLib.java</code>
     *
     * @parameter expression="${libraryOrResourceFile}"
     * @required
     */
    private String libraryOrResourceFile;

    /**
     * A directory to be added to the PYTHONPATH/CLASSPATH when creating documentation.
     * <p/>
     * e.g. src/main/java/com/test/
     *
     * @parameter expression="${extraPathDirectories}"
     */
    private File[] extraPathDirectories;

    /**
     * The default location where extra packages will be searched. Effective if extraPathDirectories attribute is not
     * used. Cannot be overridden.
     *
     * @parameter default-value="${project.basedir}/src/test/resources/robotframework/libraries"
     * @required
     * @readonly
     */
    private File defaultExtraPath;

}
