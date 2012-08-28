package org.robotframework.mavenplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.robotframework.RobotFramework;
import org.robotframework.mavenplugin.RobotFrameworkMojo;


import java.io.File;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RobotFramework.class)
public class RobotFrameworkSkipTest {

    private RobotFrameworkMojo robotFrameworkMojo;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(RobotFramework.class);
        robotFrameworkMojo = new RobotFrameworkMojo();
    }

    @Test
    public void testSkipTests()
            throws MojoExecutionException, MojoFailureException {
        Whitebox.setInternalState(robotFrameworkMojo, "skipTests", true);
        robotFrameworkMojo.execute();
    }

    @Test
    public void testSkip()
            throws MojoExecutionException, MojoFailureException {
        Whitebox.setInternalState(robotFrameworkMojo, "skip", true);
        robotFrameworkMojo.execute();
    }

    @Test
    public void testSkipATs()
            throws MojoExecutionException, MojoFailureException {
        Whitebox.setInternalState(robotFrameworkMojo, "skipATs", true);
        robotFrameworkMojo.execute();
    }

    @Test
    public void testSkipITs()
            throws MojoExecutionException, MojoFailureException {
        Whitebox.setInternalState(robotFrameworkMojo, "skipITs", true);
        robotFrameworkMojo.execute();
    }

    @Test
    public void testDontSkip()
            throws MojoExecutionException, MojoFailureException {
        String testsFolder = "tests";
        Whitebox.setInternalState(robotFrameworkMojo, "testCasesDirectory", new File(testsFolder));
        robotFrameworkMojo.execute();

        PowerMockito.verifyStatic();
        RobotFramework.run(new String[]{"-x", "TEST-tests.xml", testsFolder});
    }
}
