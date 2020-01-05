package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
 * Copyright 2013 Nokia Siemens Networks Oyj
 * Copyright 2013 Gaurav Arora
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
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.StringUtils;
import org.robotframework.mavenplugin.harvesters.HarvestUtils;
import org.robotframework.mavenplugin.harvesters.ResourceNameHarvester;
import org.robotframework.mavenplugin.harvesters.SourceFileNameHarvester;


public class TestDocConfiguration {

    public List<String[]> generateRunArguments(File projectBaseDir) {
        ArrayList<String[]> result = new ArrayList<String[]>();

        // Phase I - harvest the files/resources, if any
        ArrayList<String> fileArguments = harvestResourceOrFileCandidates(projectBaseDir, dataSourceFile);

        // Phase II - prepare the argument lines for the harvested
        // files/resources.
        boolean multipleOutputs = fileArguments.size() > 1; // with single
                                                            // argument line, we
                                                            // can use the
                                                            // original single
                                                            // entity
                                                            // parameters, so
                                                            // use this flag to
                                                            // switch.
        for (String fileArgument : fileArguments) {
            Arguments generatedArguments = generateTestdocArgumentList(projectBaseDir, multipleOutputs, fileArgument);
            result.add(generatedArguments.toArray());
        }
        return result;
    }
    
    private Arguments generateTestdocArgumentList(File projectBaseDir, boolean multipleOutputs, String fileArgument) {
        Arguments result = new Arguments();
        result.add("testdoc");
        result.addNonEmptyStringToArguments(title, "--title");
        result.addNonEmptyStringToArguments(name, "--name");
        result.addNonEmptyStringToArguments(doc, "--doc");
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
        File entity = new File(pattern);
        ArrayList<String> fileArguments = new ArrayList<String>();
        if (entity.isFile()) {
            // Single file specification, no patterns.
            fileArguments.add(entity.getAbsolutePath());
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
                    // B) If no files found, try harvesting resources.
                    ResourceNameHarvester rharv = new ResourceNameHarvester();
                    fileArguments.addAll(rharv.harvest(pattern));
                } // resources
            } // files
        } // single file or pattern
        return fileArguments;
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
     * <p></p>
     * Name must be in the same format as when used in Robot Framework test data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is imported the same as when running the tests.
     * Use {@link #extraPathDirectories} to set PYTHONPATH/CLASSPATH accordingly.
     * <p></p>
     * Paths are considered relative to the location of <code>pom.xml</code> and must point to a valid Python/Java
     * source file or a resource file. For example <code>src/main/java/com/test/ExampleLib.java</code>
     * 
     * One may also use ant-like patterns, for example
     * <code>src/main/robot/**{@literal /}*.robot</code>
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
