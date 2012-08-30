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
        libdoc.ensureOutputDirectoryExists();
        RobotFramework.run(libdoc.generateRunArguments());
    }



    /**
     * Settings for libdoc.
     *
     * @parameter
     */
    private LibDocConfiguration libdoc;

}
