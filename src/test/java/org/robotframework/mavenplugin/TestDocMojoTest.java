package org.robotframework.mavenplugin;

import java.io.File;

import org.apache.maven.it.util.FileUtils;

import static org.mockito.Mockito.*;


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
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-txtfile.xml");
        String txtResourceTestDoc = outputDirectory + "invalid.html";
        assertTrue(txtResourceTestDoc + " not found", new File(txtResourceTestDoc).exists());
    }

    public void testTestDocForTxtResourceWithTitle()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-txtfile-title.xml");
        String txtResourceWithTitleTestDoc = outputDirectory + "invalid_login_title.html";
        assertTrue(txtResourceWithTitleTestDoc + " not found", new File(txtResourceWithTitleTestDoc).exists());
        String contents =  FileUtils.fileRead(txtResourceWithTitleTestDoc);
        System.out.println(contents);
        assertTrue(contents.contains("\"title\":\"Custom Title\""));
    }

    public void testTestDocForTxtResourceWithName()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-txtfile-name.xml");
        String txtResourceWithNameTestDoc = outputDirectory + "invalid_login_name.html";
        assertTrue(txtResourceWithNameTestDoc + " not found", new File(txtResourceWithNameTestDoc).exists());
        String contents =  FileUtils.fileRead(txtResourceWithNameTestDoc);
        assertTrue(contents.contains("\"fullName\":\"Custom name\""));
    }

    public void testTestDocForTxtResourceWithDoc()
            throws Exception {
        executeLibdocWithPom("testdoc", "src/test/resources/pom-testdoc-txtfile-doc.xml");
        String txtResourceWithDocTestDoc = outputDirectory + "invalid_login_doc.html";
        assertTrue(txtResourceWithDocTestDoc + " not found", new File(txtResourceWithDocTestDoc).exists());
        String contents =  FileUtils.fileRead(txtResourceWithDocTestDoc);
        assertTrue(contents.contains("\"doc\":\"<p>Custom documentation</p>\""));
    }
}
