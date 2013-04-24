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

import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LibDocConfiguration {

    public String[] generateRunArguments() {
        Arguments generatedArguments = new Arguments();
        generatedArguments.add("libdoc");
        generatedArguments.addNonEmptyStringToArguments(name, "--name");
        generatedArguments.addNonEmptyStringToArguments(version, "--version");
        generatedArguments.addFileListToArguments(getExtraPathDirectoriesWithDefault(), "--pythonpath");
        generatedArguments.add(getLibraryOrResource());
        generatedArguments.add(getOutputPath());
        return generatedArguments.toArray();
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

    public void ensureOutputDirectoryExists()
            throws IOException {
        if (outputDirectory == null) {
            String baseDir = System.getProperty("basedir");
            // FIXME: this is to get around the problem that default-value does not currently work
            if (baseDir == null)
                baseDir = ".";
            outputDirectory = new File(joinPaths(baseDir, "target", "robotframework", "libdoc"));
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Target output directory cannot be created: " + outputDirectory.getAbsolutePath());
        }
    }

    private String joinPaths(String... parts) {
        return StringUtils.join(parts, File.separator);
    }

    public void populateDefaults(LibDocMojo defaults) {
        if (this.outputDirectory == null)
            this.outputDirectory = defaults.defaultLibdocOutputDirectory;
        this.defaultExtraPath = defaults.libdocDefaultExtraPath;
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
     * Sets the name of the documented library or resource.
     */
    private String name;

    /**
     * Sets the version of the documented library or resource.
     */
    private String version;

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
    private String libraryOrResourceFile;

    /**
     * A directory to be added to the PYTHONPATH/CLASSPATH when creating documentation.
     * <p/>
     * e.g. src/main/java/com/test/
     */
    private File[] extraPathDirectories;

    private File defaultExtraPath;
}
