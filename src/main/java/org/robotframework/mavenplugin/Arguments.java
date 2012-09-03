package org.robotframework.mavenplugin;

/*
 * Copyright 2011 Michael Mallete, Dietrich Schulten
 * Copyright 2008-2012 Nokia Siemens Networks Oyj
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

import org.codehaus.plexus.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Arguments {

    private final List<String> arguments = new ArrayList<String>();


    public void addFileToArguments(File file, String flag) {
        if (isFileValid(file)) {
            arguments.add(flag);
            arguments.add(!file.getName().toUpperCase().equals("NONE") ? file.getPath() : file.getName());
        }
    }

    protected boolean isFileValid(File file) {
        return file != null && file.getPath() != null && !file.getPath().equals("");
    }

    public void addNonEmptyStringToArguments(String variableToAdd, String flag) {
        if (!StringUtils.isEmpty(variableToAdd)) {
            addStringToArguments(variableToAdd, flag);
        }
    }

    public void addFlagToArguments( boolean flag, String argument) {
        if (flag == true) {
            arguments.add(argument);
        }
    }

    public void addStringToArguments(String variableToAdd, String flag) {
        arguments.add(flag);
        arguments.add(variableToAdd);
    }

    public void addListToArguments(List<String> variablesToAdd, String flag) {
        if (variablesToAdd == null) {
            return;
        }
        for (String variableToAdd : variablesToAdd) {
            if (!StringUtils.isEmpty(variableToAdd)) {
                arguments.add(flag);
                arguments.add(variableToAdd);
            }
        }
    }

    public void addFileListToArguments( List<File> variablesToAdd, String flag) {
        if (variablesToAdd == null) {
            return;
        }
        for (File variableToAdd : variablesToAdd) {
            addFileToArguments(variableToAdd, flag);
        }
    }

    public void add(String value) {
        arguments.add(value);
    }

    public String[] toArray() {
        return arguments.toArray(new String[arguments.size()]);
    }

}
