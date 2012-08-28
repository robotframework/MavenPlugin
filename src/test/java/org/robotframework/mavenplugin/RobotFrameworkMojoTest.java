package org.robotframework.mavenplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.robotframework.mavenplugin.RobotFrameworkMojo;


import java.io.File;

public class RobotFrameworkMojoTest
        extends AbstractMojoTestCase {

    public void setup()
            throws Exception {
        super.setUp();
    }

    public void testShouldSucceed()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-success.xml");
        RobotFrameworkMojo mojo = (RobotFrameworkMojo) lookupMojo("run", pom);
        mojo.execute();
    }

    public void testShouldFail()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-fail.xml");
        RobotFrameworkMojo mojo = (RobotFrameworkMojo) lookupMojo("run", pom);
        try {
            mojo.execute();
            fail("robot tests should have failed");
        } catch (MojoFailureException e) {
            assertTrue(true);
        }
    }

    public void testHsqlShouldPass()
            throws Exception {
        File pom = getTestFile("src/test/resources/pom-hsqldb.xml");
        RobotFrameworkMojo mojo = (RobotFrameworkMojo) lookupMojo("run", pom);
        mojo.execute();
    }

    public void testShouldHaveErrors()
            throws Exception {

        File pom = getTestFile("src/test/resources/pom-error.xml");
        RobotFrameworkMojo mojo = (RobotFrameworkMojo) lookupMojo("run", pom);
        try {
            mojo.execute();
            fail("robot tests should have errors");
        } catch (MojoExecutionException e) {
            assertTrue(true);
        }

    }

}
