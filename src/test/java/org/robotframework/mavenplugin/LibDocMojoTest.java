package org.robotframework.mavenplugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;


public class LibDocMojoTest
        extends AbstractMojoTestCase {

    private final String outputDirectory = "target/robotframework/libdoc/";
    private final String htmlResourceLibDoc = outputDirectory + "html_resource.html";
    private final String javalibLibDoc = outputDirectory + "ExampleLib.html";
    private final String mylibLibDoc = outputDirectory + "mylib.html";
    private final String mypackageMylibLibDoc = outputDirectory + "mypackage.mylib.html";

    protected void setUp()
            throws Exception {
        super.setUp();
        File outputDir = new File(outputDirectory);
        outputDir.mkdirs();
        deleteDocument(htmlResourceLibDoc);
        deleteDocument(javalibLibDoc);
        deleteDocument(mylibLibDoc);
        deleteDocument(mypackageMylibLibDoc);
    }

    public void testLibDocForJavaResource()
            throws Exception {
        executeLibdocWithPom("src/test/resources/pom-libdoc.xml");
        assertTrue(javalibLibDoc + " not found", new File(javalibLibDoc).exists());

    }

    public void testLibDocForTxtResource()
            throws Exception {
        executeLibdocWithPom("src/test/resources/pom-libdoc-txtfile.xml");
        assertTrue(htmlResourceLibDoc + " not found", new File(htmlResourceLibDoc).exists());

    }

    public void testLibDocForLibraryNamePython()
            throws Exception {

        executeLibdocWithPom("src/test/resources/pom-libdoc-libraryname-python.xml");
        assertTrue(mylibLibDoc + " not found", new File(mylibLibDoc).exists());

    }

    public void testLibDocForLibraryNamePythonWithPackage()
            throws Exception {
        executeLibdocWithPom("src/test/resources/pom-libdoc-libraryname-python-subpackage.xml");
        assertTrue(mypackageMylibLibDoc + " not found", new File(mypackageMylibLibDoc).exists());

    }

    public void testLibDocForLibraryNameJava()
            throws Exception {
        executeLibdocWithPom("src/test/resources/pom-libdoc-libraryname-java.xml");
        assertTrue(javalibLibDoc + " not found", new File(javalibLibDoc).exists());

    }

    private void executeLibdocWithPom(String pathToPom) throws Exception, MojoExecutionException,
            MojoFailureException {
        File pom = getTestFile(pathToPom);
        LibDocMojo mojo = (LibDocMojo) lookupMojo("libdoc", pom);
        mojo.execute();
    }

    private void deleteDocument(String documentation)
            throws Exception {
        File document = new File(documentation);
        if (document.exists()) {
            boolean deleted = document.delete();
            if (!deleted) {
                throw new Exception("Cannot delete existing document.");
            }
        }
    }

}
