package org.robotframework.mavenplugin.harvesters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class HarvestUtilsTest {

    @Test
    public void testExtractName() throws Exception {

        assertEquals("Blabla", HarvestUtils.extractName("com.xxx.Blabla"));
        assertEquals("Blabla", HarvestUtils.extractName("Blabla.java"));
        assertEquals("Blabla", HarvestUtils.extractName("c:\\xxx\\Blabla.java"));
        assertEquals("Blabla", HarvestUtils.extractName("/xxx/Blabla.java"));
    }

    @Test
    public void testGenerateIdName() throws Exception {
        assertEquals("b_c_d_e", HarvestUtils.generateIdName("b/c\\d.e"));
    }

    @Test
    public void testExtractExtension() throws Exception {
        assertEquals(".java", HarvestUtils.extractExtension("Blabla.java"));
        assertEquals("", HarvestUtils.extractExtension("Blabla"));
        assertEquals(".java", HarvestUtils.extractExtension(".java"));
        assertEquals(".java", HarvestUtils.extractExtension("*.java"));

        assertEquals(".java", HarvestUtils.extractExtension(new File(".java").getName()));
        assertEquals(".java", HarvestUtils.extractExtension(new File("*.java").getName()));

    }

    @Test
    public void testIsAbsolutePathFragment() throws Exception {
        // Supports Linux and Windows only, on local file systems, so no UNC,
        // etc.

        if (File.separatorChar == '\\') {
            assertTrue(HarvestUtils.isAbsolutePathFragment("c:\\bla\\*.java"));
        } else {
            assertTrue(HarvestUtils.isAbsolutePathFragment("/bla/*.java"));
        }
    }

    @Test
    public void testRemovePrefixDirectory() throws Exception {
        if (File.separatorChar == '\\') {
            assertEquals("bla.java", HarvestUtils.removePrefixDirectory(new File("c:\\bla"), "c:\\bla\\bla.java"));
            assertEquals("bla.java", HarvestUtils.removePrefixDirectory(new File("c:\\bla\\"), "c:\\bla\\bla.java"));
        } else {
            assertEquals("bla.java", HarvestUtils.removePrefixDirectory(new File("/bla"), "/bla/bla.java"));
            assertEquals("bla.java", HarvestUtils.removePrefixDirectory(new File("/bla/"), "/bla/bla.java"));
        }
    }
}
