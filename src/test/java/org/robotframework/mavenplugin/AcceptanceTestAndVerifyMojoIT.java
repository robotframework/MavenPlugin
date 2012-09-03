package org.robotframework.mavenplugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.component.MapOrientedComponent;

public class AcceptanceTestAndVerifyMojoIT
        extends AbstractMojoTestCase {

    private static final String PLUGIN = "org.robotframework:robotframework-maven-plugin";

    public void testAcceptanceTestMojos()
            throws Exception {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        Verifier verifier = new Verifier(testDir.getAbsolutePath());
        verifier.deleteDirectory("target/robotframework-reports");
        // turns on debugging
        //List<String> cliOptions = new ArrayList<String>();
        //cliOptions.add("-X");
        //verifier.setCliOptions(cliOptions);
        try {
            verifier.executeGoals(Arrays.asList(PLUGIN + ":libdoc", PLUGIN + ":acceptance-test", PLUGIN + ":verify"));
            fail("verify goal should fail the build");
        } catch (VerificationException e) {
            String message = e.getMessage();
            assertThat(message, containsString("There are acceptance test failures"));
            System.out.println(message);
        }
        verifier.displayStreamBuffers();
        verifier.resetStreams();
        verifier.assertFilePresent(new File(testDir, "target/robotframework/libdoc/JustForIT.html").getAbsolutePath());
        verifier.assertFilePresent(new File(testDir, "target/robotframework-reports/TEST-acceptance.xml").getAbsolutePath());
    }

    public void testOverrideLibdocOutputFromCommandLine()
            throws Exception {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        List cliOptions = new ArrayList();
        cliOptions.add("-Dlibdoc.outputFile=Changed.html");
        cliOptions.add("-Dlibdoc.output=target/robotframework/changed");
        Verifier verifier = executeTargets(Arrays.asList(PLUGIN + ":libdoc"), testDir, cliOptions);
        verifier.assertFilePresent(new File(testDir, "target/robotframework/changed/Changed.html").getAbsolutePath());
    }

    public void testOverrideArgumentsForDynamicLibdoc()
            throws Exception {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        List cliOptions = new ArrayList();
        cliOptions.add("-Dlibdoc.outputFile=Dynamic.html");
        cliOptions.add("-Dlibdoc.libraryOrResourceFile=src/test/robotframework/acceptance/lib_with_arguments.py::argument");
        Verifier verifier = executeTargets(Arrays.asList(PLUGIN + ":libdoc"), testDir, cliOptions);
        verifier.assertFilePresent(new File(testDir, "target/robotframework/libdoc/Dynamic.html").getAbsolutePath());
    }

    private Verifier executeTargets(List targets,File testDir, List cliOptions) throws VerificationException, IOException {
        Verifier verifier = new Verifier(testDir.getAbsolutePath());
        verifier.deleteDirectory("target/robotframework");
        verifier.setCliOptions(cliOptions);
        verifier.executeGoals(targets);
        verifier.displayStreamBuffers();
        verifier.resetStreams();
        return verifier;
    }

}
