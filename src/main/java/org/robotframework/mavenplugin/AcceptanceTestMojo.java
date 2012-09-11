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
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;
import org.robotframework.RobotFramework;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Runs the Robot tests. Behaves like invoking the "jybot" command. The goal will not fail the maven
 * build, rather the results are evaluated by the verify goal. For details see the <a
 * href="http://maven.apache.org/plugins/maven-failsafe-plugin/">maven-failsafe-plugin</a>, which
 * uses the same strategy for integration testing.
 * <p/>
 * Robot Framework tests cases are created in files and directories, and they are executed by
 * configuring the path to the file or directory in question to the testCasesDirectory
 * configuration. The given file or directory creates the top-level tests suites, which gets its
 * name, unless overridden with the "name" option, from the file or directory name.
 *
 * @goal acceptance-test
 * @phase integration-test
 * @requiresDependencyResolution test
 */
public class AcceptanceTestMojo extends AbstractMojoWithLoadedClasspath {

    // TODO better integrate test result into eclipse failure reports
    // enable to open report from eclipse easily after test run
    // as with surefire reports?
    protected void subclassExecute() throws MojoExecutionException, MojoFailureException {

        if (shouldSkipTests()) {
            getLog().info("RobotFramework tests are skipped.");
            return;
        }
        String[] runArguments = generateRunArguments();

        getLog().debug("robotframework arguments: " + StringUtils.join(runArguments, " "));

        int robotRunReturnValue = RobotFramework.run(runArguments);
        evaluateReturnCode(robotRunReturnValue);

    }

    protected void evaluateReturnCode(int robotRunReturnValue)
        // ======== ==========================================
        // RC Explanation
        // ======== ==========================================
        // 0 All critical tests passed.
        // 1-249 Returned number of critical tests failed.
        // 250 250 or more critical failures.
        // 251 Help or version information printed.
        // 252 Invalid test data or command line options.
        // 253 Test execution stopped by user.
        // 255 Unexpected internal error.
        // ======== ==========================================
            throws MojoFailureException, MojoExecutionException {
        switch (robotRunReturnValue) {
            // case 250:
            // throw new MojoFailureException( robotRunReturnValue
            // + " or more critical test cases failed. Check the logs for details." );
            // case 251:
            // getLog().info( "Help or version information printed. No tests were executed." );
            // break;
            case 252:
                // TODO write log output into message
                writeXunitFileWithError("Invalid test data or command line options (Returncode 252).");
                break;
            case 255:
                writeXunitFileWithError("Unexpected internal error (Returncode 255).");
                break;
            // case 253:
            // getLog().info( "Test execution stopped by user." );
            // break;
            default:
                // do nothing, rely on xunit file created by rf
        }
    }

    private void writeXunitFileWithError(String message) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document document = builder.newDocument();
            // <testsuite errors="0" failures="5" tests="5" skip="0" name="Robot-Fail">
            Element testsuite = document.createElement("testsuite");
            testsuite.setAttribute("errors", "1");
            testsuite.setAttribute("failures", "0");
            testsuite.setAttribute("tests", "0");
            testsuite.setAttribute("name", getTestSuiteName());
            Element testcase = document.createElement("testcase");
            testcase.setAttribute("classname", "ExecutionError");
            testcase.setAttribute("name", message);

            Element error = document.createElement("error");
            error.setAttribute("message", message);
            testcase.appendChild(error);

            testsuite.appendChild(testcase);
            document.appendChild(testsuite);

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            Source xmlSource = new DOMSource(document);
            final File output;
            output = makeAbsolute(outputDirectory, xunitFile);
            Result outputTarget = new StreamResult(output);
            transformer.transform(xmlSource, outputTarget);

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private String getTestSuiteName() {
        final String testSuiteName;
        if (name != null) {
            testSuiteName = name;
        } else {
            String delim = " -_";
            StringTokenizer tokenizer = new StringTokenizer(testCasesDirectory.getName(), delim, true);
            StringBuilder sb = new StringBuilder();
            while (tokenizer.hasMoreTokens()) {
                String tokenOrDelim = tokenizer.nextToken();
                if (delim.contains(tokenOrDelim)) {
                    sb.append(tokenOrDelim);
                } else {
                    sb.append(StringUtils.capitalizeFirstLetter(tokenOrDelim));
                }
            }
            testSuiteName = sb.toString();
        }
        return testSuiteName;
    }

    private boolean shouldSkipTests() {
        return skipTests || skipITs || skipATs || skip;
    }

    private String[] generateRunArguments() {
        Arguments generatedArguments = new Arguments();

        generatedArguments.addFileToArguments(outputDirectory, "-d");
        generatedArguments.addFileToArguments(output, "-o");
        generatedArguments.addFileToArguments(log, "-l");
        generatedArguments.addFileToArguments(report, "-r");
        generatedArguments.addFileToArguments(debugFile, "-b");
        generatedArguments.addFileToArguments(argumentFile, "-A");

        generatedArguments.addNonEmptyStringToArguments(name, "-N");
        generatedArguments.addNonEmptyStringToArguments(document, "-D");
        generatedArguments.addNonEmptyStringToArguments(runMode, "--runmode");
        generatedArguments.addNonEmptyStringToArguments(splitOutputs, "--splitoutputs");
        generatedArguments.addNonEmptyStringToArguments(logTitle, "--logtitle");
        generatedArguments.addNonEmptyStringToArguments(reportTitle, "--reporttitle");
        generatedArguments.addNonEmptyStringToArguments(reportBackground, "--reportbackground");
        generatedArguments.addNonEmptyStringToArguments(summaryTitle, "--summarytitle");
        generatedArguments.addNonEmptyStringToArguments(logLevel, "-L");
        generatedArguments.addNonEmptyStringToArguments(suiteStatLevel, "--suitestatlevel");
        generatedArguments.addNonEmptyStringToArguments(monitorWidth, "--monitorwidth");
        generatedArguments.addNonEmptyStringToArguments(monitorColors, "--monitorcolors");
        generatedArguments.addNonEmptyStringToArguments(listener, "--listener");

        generatedArguments.addFlagToArguments(runEmptySuite, "--runemptysuite");
        generatedArguments.addFlagToArguments(noStatusReturnCode, "--nostatusrc");
        generatedArguments.addFlagToArguments(timestampOutputs, "-T");
        generatedArguments.addFlagToArguments(warnOnSkippedFiles, "--warnonskippedfiles");

        generatedArguments.addListToArguments(metadata, "-M");
        generatedArguments.addListToArguments(tags, "-G");
        if (tests_cli!=null)
            generatedArguments.addListToArguments(tests_cli, "-t");
        else
            generatedArguments.addListToArguments(tests, "-t");
        if (suites_cli!=null)
            generatedArguments.addListToArguments(suites_cli, "-s");
        else
            generatedArguments.addListToArguments(suites, "-s");
        if (includes_cli!=null)
            generatedArguments.addListToArguments(includes_cli, "-i");
        else
            generatedArguments.addListToArguments(includes, "-i");
        if (excludes_cli!=null)
            generatedArguments.addListToArguments(excludes_cli, "-e");
        else
            generatedArguments.addListToArguments(excludes, "-e");
        generatedArguments.addListToArguments(criticalTags, "-c");
        generatedArguments.addListToArguments(nonCriticalTags, "-n");
        generatedArguments.addListToArguments(variables, "-v");
        generatedArguments.addListToArguments(variableFiles, "-V");
        generatedArguments.addListToArguments(tagStatIncludes, "--tagstatinclude");
        generatedArguments.addListToArguments(tagStatExcludes, "--tagstatexclude");
        generatedArguments.addListToArguments(combinedTagStats, "--tagstatcombine");
        generatedArguments.addListToArguments(tagDocs, "--tagdoc");
        generatedArguments.addListToArguments(tagStatLinks, "--tagstatlink");
        generatedArguments.addListToArguments(listeners, "--listener");

        if (extraPathDirectories == null) {
            generatedArguments.addFileToArguments(defaultExtraPath, "-P");
        } else {
            generatedArguments.addFileListToArguments(Arrays.asList(extraPathDirectories), "-P");
        }

        if (xunitFile == null) {
            String testCasesFolderName = testCasesDirectory.getName();
            xunitFile = new File("TEST-" + testCasesFolderName.replace(' ', '_') + ".xml");
        }
        generatedArguments.addFileToArguments(xunitFile, "-x");

        generatedArguments.add(testCasesDirectory.getPath());

        return generatedArguments.toArray();
    }

    /**
     * The directory where the test cases are located.
     *
     * @parameter default-value="${project.basedir}/src/test/robotframework/acceptance" expression="${testCasesDirectory}"
     */
    private File testCasesDirectory;

    /**
     * Sets the name of the top-level tests suites.
     *
     * @parameter
     */
    private String name;

    /**
     * Sets the documentation of the top-level tests suites.
     *
     * @parameter
     */
    private String document;

    /**
     * Sets free metadata for the top level tests suites.
     *
     * @parameter
     */
    private List<String> metadata;

    /**
     * Sets the tags(s) to all executed tests cases.
     *
     * @parameter
     */
    private List<String> tags;

    /**
     * Selects the tests cases by name.
     *
     * @parameter
     */
    private List<String> tests;

    /**
     * Selects the tests suites by name.
     *
     * @parameter
     */
    private List<String> suites;

    /**
     * Selects the tests cases by tags.
     *
     * @parameter
     */
    private List<String> includes;

    /**
     * Selects the tests cases by tags.
     *
     * @parameter
     */
    private List<String> excludes;

    /**
     * Selects the tests cases by name. Given as a comma separated list.
     * This setting overrides the value for tests configuration in pom.xml.
     *
     * (This setting is needed to support overriding the configuration value from command prompt on maven 2.)
     *
     * @parameter expression="${tests}"
     */
    private String tests_cli;

    /**
     * Selects the tests suites by name. Given as a comma separated list.
     * This setting overrides the value for suites configuration in pom.xml.
     *
     * (This setting is needed to support overriding the configuration value from command prompt on maven 2.)
     *
     * @parameter expression="${suites}"
     */
    private String suites_cli;

    /**
     * Selects the tests cases by tags. Given as a comma separated list.
     * This setting overrides the value for includes configuration in pom.xml.
     *
     * (This setting is needed to support overriding the configuration value from command prompt on maven 2.)
     *
     * @parameter expression="${includes}"
     */
    private String includes_cli;

    /**
     * Selects the tests cases by tags. Given as a comma separated list.
     * This setting overrides the value for excludes configuration in pom.xml.
     *
     * (This setting is needed to support overriding the configuration value from command prompt on maven 2.)
     *
     * @parameter expression="${excludes}"
     */
    private String excludes_cli;

    /**
     * Tests that have the given tags are considered critical.
     *
     * @parameter
     */
    private List<String> criticalTags;

    /**
     * Tests that have the given tags are not critical.
     *
     * @parameter
     */
    private List<String> nonCriticalTags;

    /**
     * Sets the execution mode for this tests run. Valid modes are ContinueOnFailure, ExitOnFailure,
     * SkipTeardownOnExit, DryRun, and Random:&lt;what&gt;.
     *
     * @parameter
     */
    private String runMode;

    /**
     * Sets individual variables. Use the format "name:value"
     *
     * @parameter
     */
    private List<String> variables;

    /**
     * Sets variables using variables files. Use the format "path:args"
     *
     * @parameter
     */
    private List<String> variableFiles;

    /**
     * Configures where generated reports are to be placed.
     *
     * @parameter default-value="${project.build.directory}/robotframework-reports"
     */
    private File outputDirectory;

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
     * Sets the path to the generated report file.
     *
     * @parameter
     */
    private File report;

    /**
     * Sets the path to the generated XUnit compatible result file, relative to outputDirectory. The
     * file is in xml format. By default, the file name is derived from the testCasesDirectory
     * parameter, replacing blanks in the directory name by underscores.
     *
     * @parameter
     */
    private File xunitFile;

    /**
     * A debug file that is written during execution.
     *
     * @parameter
     */
    private File debugFile;

    /**
     * Adds a timestamp to all output files.
     *
     * @parameter
     */
    private boolean timestampOutputs;

    /**
     * Splits output and log files.
     *
     * @parameter
     */
    private String splitOutputs;

    /**
     * Sets a title for the generated tests log.
     *
     * @parameter
     */
    private String logTitle;

    /**
     * Sets a title for the generated tests report.
     *
     * @parameter
     */
    private String reportTitle;

    /**
     * Sets a title for the generated summary report.
     *
     * @parameter
     */
    private String summaryTitle;

    /**
     * Sets background colors for the generated report and summary.
     *
     * @parameter
     */
    private String reportBackground;

    /**
     * Sets the threshold level for logging.
     *
     * @parameter
     */
    private String logLevel;

    /**
     * Defines how many levels to show in the Statistics by Suite table in outputs.
     *
     * @parameter
     */
    private String suiteStatLevel;

    /**
     * Includes only these tags in the Statistics by Tag and Test Details by Tag tables in outputs.
     *
     * @parameter
     */
    private List<String> tagStatIncludes;

    /**
     * Excludes these tags from the Statistics by Tag and Test Details by Tag tables in outputs.
     *
     * @parameter
     */
    private List<String> tagStatExcludes;

    /**
     * Creates combined statistics based on tags. Use the format "tags:title"
     *
     * @parameter
     */
    private List<String> combinedTagStats;

    /**
     * Adds documentation to the specified tags.
     *
     * @parameter
     */
    private List<String> tagDocs;

    /**
     * Adds external links to the Statistics by Tag table in outputs. Use the format
     * "pattern:link:title"
     *
     * @parameter
     */
    private List<String> tagStatLinks;

    /**
     * Sets multiple listeners for monitoring tests execution. Use the format "ListenerWithArgs:arg1:arg2" or
     * simply "ListenerWithoutArgs"
     *
     * @parameter
     */
    private List<String> listeners;

    /**
     * Sets a single listener for monitoring tests execution, can also be set via commandline using
     * -Dlistener=MyListener.
     *
     * @parameter expression="${listener}"
     */
    private String listener;

    /**
     * Show a warning when an invalid file is skipped.
     *
     * @parameter
     */
    private boolean warnOnSkippedFiles;

    /**
     * Width of the monitor output. Default is 78.
     *
     * @parameter
     */
    private String monitorWidth;

    /**
     * Using ANSI colors in console. Normally colors work in unixes but not in Windows. Default is
     * 'on'.
     * <ul>
     * <li>'on' - use colors in unixes but not in Windows</li>
     * <li>'off' - never use colors</li>
     * <li>'force' - always use colors (also in Windows)</li>
     * </ul>
     *
     * @parameter
     */
    private String monitorColors;

    /**
     * Additional locations (directories, ZIPs, JARs) where to search test libraries from when they
     * are imported. Maps to Jybot's --pythonpath option. Otherwise if no locations are declared,
     * the default location is ${project.basedir}/src/test/resources/robotframework/libraries.
     *
     * @parameter
     */
    private File[] extraPathDirectories;

    /**
     * The default location where extra packages will be searched. Effective if extraPath attribute
     * is not used. Cannot be overridden.
     *
     * @parameter default-value="${project.basedir}/src/test/resources/robotframework/libraries"
     * @required
     * @readonly
     */
    private File defaultExtraPath;

    /**
     * A text file to read more arguments from.
     *
     * @parameter
     */
    private File argumentFile;

    /**
     * Skip tests. Bound to -DskipTests. This allows to skip acceptance tests together with all
     * other tests.
     *
     * @parameter expression="${skipTests}"
     */
    private boolean skipTests;

    /**
     * Skip acceptance tests executed by this plugin. Bound to -DskipATs. This allows to run tests
     * and integration tests, but no acceptance tests.
     *
     * @parameter expression="${skipATs}"
     */
    private boolean skipATs;

    /**
     * Skip acceptance tests executed by this plugin together with other integration tests, e.g.
     * tests run by the maven-failsafe-plugin. Bound to -DskipITs
     *
     * @parameter expression="${skipITs}"
     */
    private boolean skipITs;

    /**
     * Skip tests, bound to -Dmaven.test.skip, which suppresses test compilation as well.
     *
     * @parameter default-value="false" expression="${maven.test.skip}"
     */
    private boolean skip;

    /**
     * Executes tests also if the top level test suite is empty. Useful e.g. with
     * --include/--exclude when it is not an error that no test matches the condition.
     *
     * @parameter default-value="false"
     */

    private boolean runEmptySuite;

    /**
     * If true, sets the return code to zero regardless of failures in test cases. Error codes are
     * returned normally.
     *
     * @parameter default-value="false"
     */

    private boolean noStatusReturnCode;

}
