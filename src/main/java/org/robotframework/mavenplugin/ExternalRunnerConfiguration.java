package org.robotframework.mavenplugin;


import java.util.Collections;
import java.util.Map;

public class ExternalRunnerConfiguration {

    /**
     * Environment variables for the new runner process.
     *
     * @parameter
     */
    private Map<String, String> environmentVariables;

    /**
     * Exclude dependencies from classpath.
     *
     * @parameter default-value="false"
     */
    private boolean excludeDependencies;

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables != null ? environmentVariables : Collections.EMPTY_MAP;
    }
    public boolean getExcludeDependencies() {
        return excludeDependencies;
    }

}
