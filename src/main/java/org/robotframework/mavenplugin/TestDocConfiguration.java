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

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.StringUtils;


public class TestDocConfiguration {

    public String[] generateRunArguments() throws IOException {
        Arguments generatedArguments = new Arguments();
        generatedArguments.add("testdoc");
        generatedArguments.addNonEmptyStringToArguments(title, "--title");
        generatedArguments.addNonEmptyStringToArguments(name, "--name");
        generatedArguments.addNonEmptyStringToArguments(doc, "--doc");
        generatedArguments.add(getDatasourceFile());
        generatedArguments.add(getOutputPath());
        return generatedArguments.toArray();
    }

    private String getDatasourceFile() throws IOException {
        File libOrResource = new File(dataSourceFile);
        if (!libOrResource.exists()) {
            throw new IOException("Data source file " + dataSourceFile + " does not exist");
        }
        return libOrResource.getAbsolutePath();
    }

    private String getOutputPath() {
        return outputDirectory + File.separator + outputFile.getName();
    }

    public void ensureOutputDirectoryExists()
            throws IOException {
        if (outputDirectory == null) {
            String baseDir = System.getProperty("basedir");
            // FIXME: this is to get around the problem that default-value does not currently work
            if (baseDir == null)
                baseDir = ".";
            outputDirectory = new File(joinPaths(baseDir, "target", "robotframework", "testdoc"));
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Target output directory cannot be created: " + outputDirectory.getAbsolutePath());
        }
    }

    private String joinPaths(String... parts) {
        return StringUtils.join(parts, File.separator);
    }

    public void populateDefaults(TestDocMojo defaults) {
        if (this.outputDirectory == null)
            this.outputDirectory = defaults.defaultTestdocOutputDirectory;
    }

    /**
     * Specifies the directory where documentation files are written. Considered to be relative to the ${basedir} of the
     * project.
     */
    private File outputDirectory;

    /**
     * Specifies the filename of the created documentation. Considered to be relative to the {@link #outputDirectory} of
     * the project.
     */
    private File outputFile;

    /**
     * Name or path of the documented library or resource file.
     * <p/>
     * Name must be in the same format as when used in Robot Framework test data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is imported the same as when running the tests.
     * Use {@link #extraPathDirectories} to set PYTHONPATH/CLASSPATH accordingly.
     * <p/>
     * Paths are considered relative to the location of <code>pom.xml</code> and must point to a valid Python/Java
     * source file or a resource file. For example <code>src/main/java/com/test/ExampleLib.java</code>
     */
    private String dataSourceFile;

    /**
     * Set the title of the generated documentation. Underscores in the title are converted to spaces. The default title is the name of the top level suite.
     */
    private String title;

    /**
     * Override the name of the top level test suite.
     */
    private String name;
    
    /**
     * Override the documentation of the top level test suite.
     */
    private String doc;
}
