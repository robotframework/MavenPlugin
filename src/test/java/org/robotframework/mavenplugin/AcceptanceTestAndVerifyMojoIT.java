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
    private File testDir = getTestFile("src/test/projects/acceptance-and-verify");
    private Verifier verifier;
    private List<String> cliOptions;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        verifier = new Verifier(testDir.getAbsolutePath());
        cliOptions = new ArrayList<String>();
    }

    public void testAcceptanceTestMojos()
            throws Exception {
        executeFailingGoals(PLUGIN + ":acceptance-test", PLUGIN + ":verify");
        assertFilePresent("target/robotframework-reports/TEST-acceptance.xml");
    }

    public void testLibdocMojo() throws IOException, VerificationException {
        executeGoals(PLUGIN + ":libdoc");
        assertFilePresent("target/robotframework/libdoc/JustForIT.html");
    }

    public void testOverrideFromCommandPrompt() throws IOException, VerificationException {
        cliOptions.add("-Dtests=successful*");
        executeGoals(PLUGIN + ":acceptance-test",PLUGIN + ":verify");
        assertFilePresent("target/robotframework-reports/TEST-acceptance.xml");
    }

    public void testOverrideListsFromCommandPrompt() throws IOException, VerificationException {
        cliOptions.add("-Dtests=foo,successful*,bar");
        cliOptions.add("-Dsuites=foo,successful*,bar");
        executeGoals(PLUGIN + ":acceptance-test",PLUGIN + ":verify");
        assertFilePresent("target/robotframework-reports/TEST-acceptance.xml");
    }

    public void testPomWithTestsConfigured() throws IOException, VerificationException {
        cliOptions.add("-f");
        cliOptions.add("pom_with_tests_configured.xml");
        executeGoals(PLUGIN + ":acceptance-test",PLUGIN + ":verify");
        assertFilePresent("target/robotframework-reports/TEST-acceptance.xml");
    }

    public void testPomWithTestsConfiguredOverridden() throws IOException, VerificationException {
        cliOptions.add("-f");
        cliOptions.add("pom_with_tests_configured.xml");
        cliOptions.add("-Dtests=failing*");
        executeFailingGoals(PLUGIN + ":acceptance-test", PLUGIN + ":verify");
        assertFilePresent("target/robotframework-reports/TEST-acceptance.xml");
    }

    private void assertFilePresent(String file) {
        verifier.assertFilePresent(new File(testDir, file).getAbsolutePath());
    }

    private void executeGoals(String... goals) throws VerificationException, IOException {
        verifier.setCliOptions(cliOptions);
        verifier.deleteDirectory("target/robotframework-reports");
        verifier.deleteDirectory("target/robotframework");
        verifier.executeGoals(Arrays.asList(goals));
        verifier.displayStreamBuffers();
        verifier.resetStreams();
    }

    private void executeFailingGoals(String... goals) throws IOException {
        verifier.setCliOptions(cliOptions);
        verifier.deleteDirectory("target/robotframework-reports");
        verifier.deleteDirectory("target/robotframework");
        try {
            verifier.executeGoals(Arrays.asList(goals));
            fail("verify goal should fail the build");
        } catch (VerificationException e) {
            String message = e.getMessage();
            assertThat(message, containsString("There are acceptance test failures"));
            System.out.println(message);
        }
        verifier.displayStreamBuffers();
        verifier.resetStreams();
    }

}
