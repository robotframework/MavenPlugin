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
        try {
            verifier.executeGoals(Arrays.asList(PLUGIN + ":acceptance-test", PLUGIN + ":verify"));
            fail("verify goal should fail the build");
        } catch (VerificationException e) {
            String message = e.getMessage();
            assertThat(message, containsString("There are acceptance test failures"));
            System.out.println(message);
        }
        verifier.displayStreamBuffers();
        verifier.resetStreams();
        verifier.assertFilePresent(new File(testDir, "target/robotframework-reports/TEST-acceptance.xml").getAbsolutePath());
    }

    public void testLibdocMojo() throws IOException, VerificationException {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        List<String> cliOptions = new ArrayList<String>();
        Verifier verifier = executeGoals(Arrays.asList(PLUGIN + ":libdoc"), testDir, cliOptions);
        verifier.assertFilePresent(new File(testDir, "target/robotframework/libdoc/JustForIT.html").getAbsolutePath());
    }

    public void testOverrideFromCommandPrompt() throws IOException, VerificationException {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        List<String> cliOptions = new ArrayList<String>();
        cliOptions.add("-Dtests=successful*");
        Verifier verifier = executeGoals(Arrays.asList(PLUGIN + ":acceptance-test", PLUGIN + ":verify"), testDir, cliOptions);
        verifier.assertFilePresent(new File(testDir, "target/robotframework-reports/TEST-acceptance.xml").getAbsolutePath());
    }

    public void testOverrideListsFromCommandPrompt() throws IOException, VerificationException {
        File testDir = getTestFile("src/test/projects/acceptance-and-verify");
        List<String> cliOptions = new ArrayList<String>();
        cliOptions.add("-Dtests=foo,successful*,bar");
        cliOptions.add("-Dsuites=foo,successful*,bar");
        Verifier verifier = executeGoals(Arrays.asList(PLUGIN + ":acceptance-test", PLUGIN + ":verify"), testDir, cliOptions);
        verifier.assertFilePresent(new File(testDir, "target/robotframework-reports/TEST-acceptance.xml").getAbsolutePath());
    }

    private Verifier executeGoals(List<String> goals, File testDir, List<String> cliOptions) throws VerificationException, IOException {
        Verifier verifier = new Verifier(testDir.getAbsolutePath());
        verifier.setCliOptions(cliOptions);
        verifier.deleteDirectory("target/robotframework-reports");
        verifier.deleteDirectory("target/robotframework");
        verifier.executeGoals(goals);
        verifier.displayStreamBuffers();
        verifier.resetStreams();
        return verifier;
    }

}
