package org.robotframework.mavenplugin;

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
