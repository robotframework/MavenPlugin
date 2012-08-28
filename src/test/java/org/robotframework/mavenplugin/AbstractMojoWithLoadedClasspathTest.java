package org.robotframework.mavenplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.robotframework.mavenplugin.AbstractMojoWithLoadedClasspath;


public class AbstractMojoWithLoadedClasspathTest
        extends TestCase {
    public void testFilePathParsing() {
        AbstractMojoWithLoadedClasspath mojo = createEmptyMojo();
        List<String> args = new ArrayList<String>();
        mojo.addFileToArguments(args, new File(new File("NONE").getAbsolutePath()), "-l");
        assertEquals("NONE", args.get(1));
    }

    private AbstractMojoWithLoadedClasspath createEmptyMojo() {
        return new AbstractMojoWithLoadedClasspath() {
            @Override
            protected void subclassExecute()
                    throws MojoExecutionException, MojoFailureException {
            }
        };
    }
}
