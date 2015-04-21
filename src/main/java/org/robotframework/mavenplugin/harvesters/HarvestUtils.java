package org.robotframework.mavenplugin.harvesters;

import java.io.File;

/**
 * Some utilities for processing harvested names.
 */
public class HarvestUtils {

    public static final String JAVA_FILE_EXT = ".java";

    /**
     * Extracts the name component of a string representing a file name.
     */
    public static String extractName(String harvestedName) {
        String result = "";

        int indexOfSlash = harvestedName.lastIndexOf('/');
        int indexOfBackSlash = harvestedName.lastIndexOf('\\');
        if (indexOfSlash >= 0 || indexOfBackSlash >= 0) {
            // we deal with a file path
            int index = Math.max(indexOfSlash, indexOfBackSlash);
            if (index + 1 != harvestedName.length()) {
                if (harvestedName.endsWith(JAVA_FILE_EXT))
                    result = harvestedName.substring(index + 1, harvestedName.length() - JAVA_FILE_EXT.length());
                else
                    result = harvestedName.substring(index + 1);
            }
        } else {
            if (harvestedName.endsWith(JAVA_FILE_EXT))
                result = harvestedName.substring(0, harvestedName.length() - JAVA_FILE_EXT.length());
            else {
                // Dealing with a class, so use the name.
                int indexOfDot = harvestedName.lastIndexOf('.');
                if (indexOfDot + 1 != harvestedName.length())
                    result = harvestedName.substring(indexOfDot + 1);
            }
        }
        return result;
    }

    public static int calculateMinimumPatternIndex(String antLikePattern) {
        int indexOfStar = antLikePattern.indexOf('*');
        int indexOfQuestionMark = antLikePattern.indexOf('?');
        int minPatternIndex;
        if (indexOfStar >= 0) {
            if (indexOfQuestionMark >= 0) 
                minPatternIndex = Math.min(indexOfStar, indexOfQuestionMark);
            else 
                minPatternIndex = indexOfStar;
        } else {
            if (indexOfQuestionMark >= 0) 
                minPatternIndex = indexOfQuestionMark;
            else 
                minPatternIndex = -1;
        }
        return minPatternIndex;
    }

    /**
     * Prepares an id name from a full path or fully qualified file, by
     * replacing various chars with '_'.
     */
    public static String generateIdName(String harvestedName) {
        return harvestedName.replaceAll("/|\\.|\\\\", "_");
    }

    /**
     * Checks whether the given parameter seems to start with an absolute path
     * fragment according to the current file system.
     */
    public static boolean isAbsolutePathFragment(String fragment) {
        // Need to find out whether we have a pattern/path that starts as
        // absolute path according to the current file system.
        return new File(fragment).isAbsolute();
    }

    /**
     * Whether the fragment hints to a directory structure, supporting Windows
     * or *nix file systems.
     * 
     * @param fragment
     * @return
     */
    public static boolean hasDirectoryStructure(String fragment) {
        // occurrence of '/' or '\' hints at a directory structure, hence files,
        // so try that one.
        return fragment.indexOf('/') >= 0 || fragment.indexOf('\\') >= 0;
    }

    /**
     * Extracts from the filename what could serve as extension.
     * 
     * @param name
     * @return
     */
    public static String extractExtension(String filename) {
        String result = "";
        int indexOfDot = filename.lastIndexOf('.');
        if (indexOfDot >= 0) {
            result = filename.substring(indexOfDot);
        }
        return result;
    }

    public static String removePrefixDirectory(File projectBaseDir, String fileArgument) {
        String result = fileArgument;
        String prefix = projectBaseDir.getAbsolutePath() + File.separator;
        if (fileArgument.startsWith(prefix)) {
            result = fileArgument.substring(prefix.length());
        }
        return result;
    }

}
