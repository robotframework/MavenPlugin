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

import org.codehaus.plexus.util.StringUtils;
import org.robotframework.mavenplugin.harvesters.ClassNameHarvester;
import org.robotframework.mavenplugin.harvesters.ResourceNameHarvester;
import org.robotframework.mavenplugin.harvesters.SourceFileNameHarvester;

public class LibDocConfiguration {

    public List<String[]> generateRunArguments() {
    	ArrayList<String[]> result = new ArrayList<String[]>();
    	
        File libOrResource = new File(libraryOrResourceFile);
        ArrayList<String> fileArguments = new ArrayList<String>();
        if (libOrResource.isFile()) {
            fileArguments.add(libOrResource.getAbsolutePath());
        } else {
        	//occurrence of '/' or '\' hints at a directory structure, hence files, so try that one.
        	int indexOfSlash = libraryOrResourceFile.indexOf('/');
        	int indexOfBackSlash = libraryOrResourceFile.indexOf('\\');
        	if (indexOfSlash >=0 || indexOfBackSlash >= 0) {
        		//Directory structure, no class resolution, harvest file names.
        		SourceFileNameHarvester harv = new SourceFileNameHarvester();
        		fileArguments.addAll(harv.harvest(libraryOrResourceFile));
        	} else {
        		//A) May have files, try for harvesting file names first.
        		SourceFileNameHarvester harv = new SourceFileNameHarvester();
        		List<String> harvested = harv.harvest(libraryOrResourceFile);
        		if (harvested.size() > 0) {
        			fileArguments.addAll(harvested);
        		} else {
        			//B) If no files found, try harvesting classes.
        			ClassNameHarvester charv = new ClassNameHarvester();
        			harvested = charv.harvest(libraryOrResourceFile); 
	        		if (harvested.size() > 0) {
	        			fileArguments.addAll(harvested);
	        		} else {
	        			//C) If no files found, try harvesting resources.
	        			ResourceNameHarvester rharv = new ResourceNameHarvester();
	        			fileArguments.addAll(rharv.harvest(libraryOrResourceFile));
	        		}//resources
        		}//classes
        	}//files
        }//single file or pattern

        for (String fileArgument: fileArguments) {
	        Arguments generatedArguments = new Arguments();
	        generatedArguments.add("libdoc");
	        generatedArguments.addNonEmptyStringToArguments(name, "--name");
	        generatedArguments.addNonEmptyStringToArguments(version, "--version");
	        generatedArguments.addFileListToArguments(getExtraPathDirectoriesWithDefault(), "--pythonpath");
	        generatedArguments.add(fileArgument);
	        generatedArguments.add(getOutputPath());
	        
	        result.add( generatedArguments.toArray());
        }
        return result; 
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
