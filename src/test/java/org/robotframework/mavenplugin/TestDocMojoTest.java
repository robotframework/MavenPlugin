package org.robotframework.mavenplugin;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;


public class TestDocMojoTest
        extends AbstractRFMojoTestCase {

    private final String outputDirectory = "target/robotframework/testdoc/";

    protected void setUp()
            throws Exception {
        super.setUp();
        File outputDir = new File(outputDirectory);
        outputDir.mkdirs();
    }

    public void testTestDocForTxtResource()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-robotfile.xml");
        String txtResourceTestDoc = outputDirectory + "invalid.html";
        assertTrue(txtResourceTestDoc + " not found", new File(txtResourceTestDoc).exists());
    }

    public void testTestDocForTxtResourceWithTitle()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-robotfile-title.xml");
        String txtResourceWithTitleTestDoc = outputDirectory + "invalid_login_title.html";
        assertTrue(txtResourceWithTitleTestDoc + " not found", new File(txtResourceWithTitleTestDoc).exists());
        String contents =  FileUtils.readFileToString(new File(txtResourceWithTitleTestDoc), Charset.defaultCharset());
        assertTrue(contents.contains("\"title\":\"Custom Title\""));
    }

    public void testTestDocForTxtResourceWithName()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-robotfile-name.xml");
        String txtResourceWithNameTestDoc = outputDirectory + "invalid_login_name.html";
        assertTrue(txtResourceWithNameTestDoc + " not found", new File(txtResourceWithNameTestDoc).exists());
        String contents =  FileUtils.readFileToString(new File(txtResourceWithNameTestDoc), Charset.defaultCharset());
        assertTrue(contents.contains("\"fullName\":\"Custom name\""));
    }

    public void testTestDocForTxtResourceWithDoc()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-robotfile-doc.xml");
        String txtResourceWithDocTestDoc = outputDirectory + "invalid_login_doc.html";
        assertTrue(txtResourceWithDocTestDoc + " not found", new File(txtResourceWithDocTestDoc).exists());
        String contents =  FileUtils.readFileToString(new File(txtResourceWithDocTestDoc), Charset.defaultCharset());
        assertTrue(contents.contains("\"doc\":\"<p>Custom documentation"));
    }
}
