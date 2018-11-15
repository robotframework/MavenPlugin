package org.robotframework.mavenplugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasXPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.BeforeClass;
import org.robotframework.mavenplugin.VerifyMojo;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class RebotMojoTest extends AbstractMojoTestCase {

	public void setup() throws Exception {
		super.setUp();
		File outputDirectory = getTestFile("target/robotframework-report");
		outputDirectory.delete();
	}

	public void testReportSuccess() throws Exception {
		File pom = getTestFile("src/test/resources/pom-rebot.xml");
		this.executeRebotMojo(pom);
		File xunitFile = getTestFile("target/robotframework-reports/rebot/TEST-robot-success.xml");
		assertTrue("missing xunit test report " + xunitFile, xunitFile.exists());

		Document xunit = parseDocument(xunitFile);
		assertThat(xunit, hasXPath("/testsuite[@errors='0']"));
		assertThat(xunit, hasXPath("/testsuite[@failures='0']"));
		assertThat(xunit, hasXPath("/testsuite[@tests='4']"));
	}

	public void testReportSuccessMerged() throws Exception {
		File pom = getTestFile("src/test/resources/pom-rebot-merged.xml");
		this.executeRebotMojo(pom);
		File xunitFile = getTestFile("target/robotframework-reports/rebot/TEST-robot-success-merged.xml");
		assertTrue("missing xunit test report " + xunitFile, xunitFile.exists());

		Document xunit = parseDocument(xunitFile);
		assertThat(xunit, hasXPath("/testsuite[@errors='0']"));
		assertThat(xunit, hasXPath("/testsuite[@failures='0']"));
		//As same suite is runned twice, merge shows only 2 runs. Rebot knows that the cases 
		//area same, and includes results in the cases itself.
		assertThat(xunit, hasXPath("/testsuite[@tests='2']"));
	}
	
	private Document parseDocument(File xunitFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
		Document xunit = builder.parse(xunitFile);
		return xunit;
	}
	
	private void executeRebotMojo(File pom)
            throws MojoExecutionException, MojoFailureException {
        RebotMojo mojo;
        try {
            mojo = (RebotMojo) lookupMojo("rebot", pom);
            File outputDirectory = getTestFile("src/test/resources/output-for-rebot/");
            File targetDirectory = (File) getVariableValueFromObject(mojo, "outputDirectory");
            File[] files = outputDirectory.listFiles();
            for (int i = 0; i < files.length; i++) {
               FileUtils.copyFileToDirectory(files[i], targetDirectory);
            }
        } catch (Exception ex) {
            throw new RuntimeException("failed to prepare mojo execution", ex);
        }
        mojo.execute();
    }
}
