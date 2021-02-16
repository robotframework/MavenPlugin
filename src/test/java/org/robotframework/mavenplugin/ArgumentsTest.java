package org.robotframework.mavenplugin;

import java.io.File;

import junit.framework.TestCase;


public class ArgumentsTest
        extends TestCase {

    public void testNONEFilePathParsing() {
        Arguments args = new Arguments();
        args.addFileToArguments(new File(new File("NONE").getAbsolutePath()), "-l");
        assertEquals("NONE", args.toArray()[1]);
    }
}
