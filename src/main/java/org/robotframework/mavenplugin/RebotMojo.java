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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;
import org.robotframework.RobotFramework;

/**
 * Creates report files from output.xml using the Robot Framework <code>rebot</code> tool.
 * 
 * Uses the <code>rebot</code> bundled in Robot Framework jar distribution. For more help see
 * <a href="http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#using-robot-and-rebot-scripts">rebot documentation</a>.
 *
 * @goal rebot
 * @requiresDependencyResolution test
 */
public class RebotMojo
        extends AbstractMojoWithLoadedClasspath {

    protected void subclassExecute()
            throws MojoExecutionException, MojoFailureException {
        try {
            runRebot();
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to execute rebot script: " + e.getMessage());
        }
    }

    public void runRebot()
            throws IOException {
        this.ensureOutputDirectoryExists();
        RobotFramework.run(this.generateRunArguments());
    }
    
    
    public String[] generateRunArguments() {
        Arguments generatedArguments = new Arguments();
        generatedArguments.add("rebot");
        generatedArguments.addFileToArguments(outputDirectory, "-d");
        generatedArguments.addFileToArguments(output, "-o");
        generatedArguments.addFileToArguments(log, "-l");
        generatedArguments.addNonEmptyStringToArguments(logTitle, "--logtitle");
        generatedArguments.addFileToArguments(report, "-r");
        generatedArguments.addNonEmptyStringToArguments(reportTitle, "--reporttitle");
        generatedArguments.addNonEmptyStringToArguments(splitOutputs, "--splitoutputs");
        generatedArguments.addFlagToArguments(merge, "--merge");
        generatedArguments.addFileToArguments(xunitFile, "-x");
        generatedArguments.addNonEmptyStringToArguments(logLevel, "-L");
        generatedArguments.addFlagToArguments(true, "--xunitskipnoncritical");
        generatedArguments.addFlagToArguments(rpa, "--rpa");
        generatedArguments.add(getOutputPath());
        return generatedArguments.toArray();
    }
    
    private String getOutputPath() {
        return outputDirectory + File.separator + "output*.xml";
    }

    public void ensureOutputDirectoryExists()
            throws IOException {
        if (outputDirectory == null) {
            String baseDir = System.getProperty("basedir");
            // FIXME: this is to get around the problem that default-value does not currently work
            if (baseDir == null)
                baseDir = ".";
            outputDirectory = new File(joinPaths(baseDir, "target", "robotframework-reports"));
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Target output directory cannot be created: " + outputDirectory.getAbsolutePath());
        }
    }

    private String joinPaths(String... parts) {
        return StringUtils.join(parts, File.separator);
    }
    
    /**
     * Configures where generated reports are to be placed.
     *
     * @parameter default-value="${project.build.directory}/robotframework-reports"
     */
    private File outputDirectory;
    
    /**
     * 
     * When combining results, merge outputs together
     * instead of putting them under a new top level suite.
     * 
     * @parameter default-value="false"
     */
    private boolean merge;
    
    /**
     * Sets the threshold level for logging.
     *
     * @parameter
     */
    private String logLevel;
    
    /**
     * Sets the path to the generated output file.
     *
     * @parameter
     */
    private File output;
    
    /**
     * Sets the path to the generated log file.
     *
     * @parameter
     */
    private File log;

    /**
     * Sets a title for the generated tests log.
     *
     * @parameter
     */
    private String logTitle;

    /**
     * Sets the path to the generated report file.
     *
     * @parameter
     */
    private File report;

    /**
     * Sets a title for the generated tests report.
     *
     * @parameter
     */
    private String reportTitle;

    /**
     * Splits output and log files.
     *
     * @parameter
     */
    private String splitOutputs;
    
    /**
     * Sets the path to the generated XUnit compatible result file, relative to outputDirectory. The
     * file is in xml format. By default, the file name is derived from the testCasesDirectory
     * parameter, replacing blanks in the directory name by underscores.
     *
     * @parameter default-value="${project.build.directory}/robotframework-reports/rebot-xunit-results.xml"
     */
    private File xunitFile;

    /**
     * Turn on generic automation mode.
     *
     * @parameter default-value="false"
     */
    private boolean rpa;

}
