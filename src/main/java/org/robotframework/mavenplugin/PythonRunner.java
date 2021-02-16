package org.robotframework.mavenplugin;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

public class PythonRunner{
    public static void run(String[] commandWithRunArguments) {
        try {
            exec(commandWithRunArguments);
        } catch (Exception e) {
            // Nothing to do
        }
    }

    public static int exec(String command, String[] runArguments) throws IOException,
    InterruptedException {
        return exec(ArrayUtils.insert(0, runArguments, command));
    }

    public static int exec(String[] commandWithRunArguments) throws IOException,
    InterruptedException {
        Process process = Runtime.getRuntime().exec(String.join(" ", commandWithRunArguments));
        StreamReader stdout = new StreamReader(process.getInputStream());
        StreamReader stderr = new StreamReader(process.getErrorStream());
        stdout.start();
        stderr.start();
        return process.waitFor();
    }
}
