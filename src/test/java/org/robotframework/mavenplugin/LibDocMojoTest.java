package org.robotframework.mavenplugin;

import java.io.File;


public class LibDocMojoTest
        extends AbstractRFMojoTestCase {

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
        executeLibdocWithPom("libdoc", "src/test/resources/pom-libdoc.xml");
        assertTrue(javalibLibDoc + " not found", new File(javalibLibDoc).exists());
    }

    public void testLibDocForTxtResource()
            throws Exception {
        executeLibdocWithPom("libdoc", "src/test/resources/pom-libdoc-txtfile.xml");
        assertTrue(htmlResourceLibDoc + " not found", new File(htmlResourceLibDoc).exists());

    }

    public void testLibDocForLibraryNamePython()
            throws Exception {

        executeLibdocWithPom("libdoc", "src/test/resources/pom-libdoc-libraryname-python.xml");
        assertTrue(mylibLibDoc + " not found", new File(mylibLibDoc).exists());

    }

    public void testLibDocForLibraryNamePythonWithPackage()
            throws Exception {
        executeLibdocWithPom("libdoc", "src/test/resources/pom-libdoc-libraryname-python-subpackage.xml");
        assertTrue(mypackageMylibLibDoc + " not found", new File(mypackageMylibLibDoc).exists());

    }

    public void testLibDocForLibraryNameJava()
            throws Exception {
        executeLibdocWithPom("libdoc", "src/test/resources/pom-libdoc-libraryname-java.xml");
        assertTrue(javalibLibDoc + " not found", new File(javalibLibDoc).exists());

    }
}
