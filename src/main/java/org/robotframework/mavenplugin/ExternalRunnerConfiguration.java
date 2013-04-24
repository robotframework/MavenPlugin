package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
 * Copyright 2013 Nokia Siemens Networks Oyj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Collections;
import java.util.List;
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

    /**
     * JVM arguments for the new runner process.
     * 
     * @parameter
     */
    private List<String> jvmArgs;

    public List<String> getJvmArgs() {
        if(jvmArgs==null){
            return Collections.emptyList();
        } else {
            return jvmArgs;
        }
    }
}