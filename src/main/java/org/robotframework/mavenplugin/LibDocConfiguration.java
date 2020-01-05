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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;
import org.robotframework.mavenplugin.harvesters.ClassNameHarvester;
import org.robotframework.mavenplugin.harvesters.HarvestUtils;
import org.robotframework.mavenplugin.harvesters.ResourceNameHarvester;
import org.robotframework.mavenplugin.harvesters.SourceFileNameHarvester;

public class LibDocConfiguration {

    public List<String[]> generateRunArguments(File projectBaseDir) {
        ArrayList<String[]> result = new ArrayList<String[]>();

        // Phase I - harvest the files/classes/resources, if any
        ArrayList<String> fileArguments = harvestResourceOrFileCandidates(projectBaseDir, libraryOrResourceFile);

        // Phase II - prepare the argument lines for the harvested
        // files/classes/resources.
        boolean multipleOutputs = fileArguments.size() > 1; // with single
                                                            // argument line, we
                                                            // can use the
                                                            // original single
                                                            // entity
                                                            // parameters, so
                                                            // use this flag to
                                                            // switch.
        for (String fileArgument : fileArguments) {
            Arguments generatedArguments = generateLibdocArgumentList(projectBaseDir, multipleOutputs, fileArgument);
            result.add(generatedArguments.toArray());
        }
        return result;
    }

    private Arguments generateLibdocArgumentList(File projectBaseDir, boolean multipleOutputs, String fileArgument) {
        Arguments result = new Arguments();
        result.add("libdoc");
        if (multipleOutputs) {
            // Derive the name from the input.
            result.addNonEmptyStringToArguments(HarvestUtils.extractName(fileArgument), "--name");
        } else {
            // Preserve the original single-file behavior.
            result.addNonEmptyStringToArguments(name, "--name");
        }
        result.addNonEmptyStringToArguments(version, "--version");
        result.addFileListToArguments(getExtraPathDirectoriesWithDefault(), "--pythonpath");
        result.add(fileArgument);

        if (multipleOutputs) {
            // Derive the output file name id from the source and from the
            // output file given.
            String normalizedArgument;
            // Generate a unique name.
            if (HarvestUtils.isAbsolutePathFragment(fileArgument)) {
                // Cut out the project directory, so that we have shorter id
                // names.
                // TODO - perhaps later, we can preserve the directory structure
                // relative to the output directory.
                normalizedArgument = HarvestUtils.removePrefixDirectory(projectBaseDir, fileArgument);
            } else {
                normalizedArgument = fileArgument;
            }
            result.add(outputDirectory + File.separator + HarvestUtils.generateIdName(normalizedArgument)
                    + HarvestUtils.extractExtension(outputFile.getName()));
        } else {
            // Preserve original single-file behavior.
            if (outputFile.getName().contains("*")) {
                // We deal with a pattern, so we need to get the name from the
                // input file.
                File tf = new File(fileArgument);
                result.add(outputDirectory + File.separator + tf.getName()
                        + HarvestUtils.extractExtension(outputFile.getName()));
            } else {
                // Use the output name directly.
                result.add(outputDirectory + File.separator + outputFile.getName());
            }
        }
        return result;
    }

    private ArrayList<String> harvestResourceOrFileCandidates(File projectBaseDir, String pattern) {
        File libOrResource = new File(pattern);
        ArrayList<String> fileArguments = new ArrayList<String>();
        if (libOrResource.isFile()) {
            // Single file specification, no patterns.
            fileArguments.add(libOrResource.getAbsolutePath());
        } else {
            // Possible pattern, process further.
            if (HarvestUtils.hasDirectoryStructure(pattern)) {
                // Directory structure, no class resolution, harvest file names.
                SourceFileNameHarvester harv = new SourceFileNameHarvester(projectBaseDir);
                fileArguments.addAll(harv.harvest(pattern));
            } else {
                // A) May have files, try for harvesting file names first.
                SourceFileNameHarvester harv = new SourceFileNameHarvester(projectBaseDir);
                Set<String> harvested = harv.harvest(pattern);
                if (harvested.size() > 0) {
                    fileArguments.addAll(harvested);
                } else {
                    // B) If no files found, try harvesting classes.
                    ClassNameHarvester charv = new ClassNameHarvester();
                    harvested = charv.harvest(pattern);
                    if (harvested.size() > 0) {
                        fileArguments.addAll(harvested);
                    } else {
                        // C) If no files found, try harvesting resources.
                        ResourceNameHarvester rharv = new ResourceNameHarvester();
                        fileArguments.addAll(rharv.harvest(pattern));
                    } // resources
                } // classes
            } // files
        } // single file or pattern
        return fileArguments;
    }

    private List<File> getExtraPathDirectoriesWithDefault() {
        if (extraPathDirectories == null) {
            return Collections.singletonList(defaultExtraPath);
        } else {
            return Arrays.asList(extraPathDirectories);
        }
    }

    public void ensureOutputDirectoryExists() throws IOException {
        if (outputDirectory == null) {
            String baseDir = System.getProperty("basedir");
            // FIXME: this is to get around the problem that default-value does
            // not currently work
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
     * Specifies the directory where documentation files are written. Considered
     * to be relative to the ${basedir} of the project.
     */
    private File outputDirectory;

    /**
     * Specifies the filename of the created documentation. Considered to be
     * relative to the {@link #outputDirectory} of the project.
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
     * Name must be in the same format as when used in Robot Framework test
     * data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is
     * imported the same as when running the tests. Use
     * {@link #extraPathDirectories} to set PYTHONPATH/CLASSPATH accordingly.
     * <p/>
     * Paths are considered relative to the location of <code>pom.xml</code> and
     * must point to a valid Python/Java source file or a resource file. For
     * example <code>src/main/java/com/test/ExampleLib.java</code>
     * 
     * One may also use ant-like patterns, for example
     * <code>src/main/java/com/**{@literal /}*Lib.java</code>
     * 
     */
    private String libraryOrResourceFile;

    /**
     * A directory to be added to the PYTHONPATH/CLASSPATH when creating
     * documentation.
     * <p/>
     * e.g. src/main/java/com/test/
     */
    private File[] extraPathDirectories;

    private File defaultExtraPath;
}
