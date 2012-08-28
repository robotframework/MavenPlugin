package org.robotframework.mavenplugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.robotframework.mavenplugin.VerifyMojo;


public class VerifyMojoTest
        extends AbstractMojoTestCase {

    public void setup()
            throws Exception {
        super.setUp();
        File outputDirectory = getTestFile("target/robotframework-report");
        outputDirectory.delete();
    }

    public void testReportSuccess()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-success.xml");
        String xunitResult = "/xunitresults/TEST-robot-success.xml";
        executeVerifyMojo(pom, xunitResult);

    }

    public void testReportFgailure()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-fail.xml");
        String xunitResult = "/xunitresults/TEST-robot-fail.xml";
        try {
            executeVerifyMojo(pom, xunitResult);
            fail("MojoFailureException expected");
        } catch (MojoFailureException ex) {
            assertThat(ex.getMessage(), containsString("failure"));
        }
    }

    public void testReportError()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-error.xml");
        String xunitResult = "/xunitresults/TEST-nothing-here.xml";
        try {
            executeVerifyMojo(pom, xunitResult);
            fail("MojoFailureException expected");
        } catch (MojoFailureException ex) {
            assertThat(ex.getMessage(), containsString("There are acceptance test failures"));
        }
    }

    private void executeVerifyMojo(File pom, String xunitResult)
            throws MojoExecutionException, MojoFailureException {
        VerifyMojo mojo;
        try {
            mojo = (VerifyMojo) lookupMojo("verify", pom);

            File xunitFile = (File) getVariableValueFromObject(mojo, "xunitFile");
            File outputDirectory = (File) getVariableValueFromObject(mojo, "outputDirectory");

            // copy from resources:
            InputStream resultFileInputStream = this.getClass().getResourceAsStream(xunitResult);
            copyXunitReport(resultFileInputStream, outputDirectory, xunitFile);
        } catch (Exception ex) {
            throw new RuntimeException("failed to prepare mojo execution", ex);
        }
        mojo.execute();
    }

    private void copyXunitReport(InputStream resultFileInput, File outputDirectory, File targetFile)
            throws IOException {
        File xunitOutput = getXunitOutput(outputDirectory, targetFile);

        OutputStream out = new FileOutputStream(xunitOutput);
        byte[] bytes = new byte[1024];
        int len;
        while (-1 != (len = resultFileInput.read(bytes))) {
            out.write(bytes, 0, len);
        }

    }

    private File getXunitOutput(File outputDirectory, File xunitFile) {
        outputDirectory.mkdirs();
        if (!xunitFile.isAbsolute())
            return new File(outputDirectory, xunitFile.getName());
        return xunitFile;
    }

}
