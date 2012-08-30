package org.robotframework.mavenplugin;

import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


public class LibDocConfiguration {

    public String[] generateRunArguments() {
        Arguments generatedArguments = new Arguments();
        generatedArguments.add("libdoc");
        generatedArguments.addNonEmptyStringToArguments(name, "--name");
        generatedArguments.addNonEmptyStringToArguments(format, "--format");
        generatedArguments.addFileListToArguments(getExtraPathDirectoriesWithDefault(), "--pythonpath");
        generatedArguments.add(getLibraryOrResource());
        generatedArguments.add(getOutputPath());
        return generatedArguments.toArray();
    }

    private String getLibraryOrResource() {
        File libOrResource = new File(libraryOrResourceFile);
        if (libOrResource.exists()) {
            return libOrResource.getAbsolutePath();
        } else {
            return libraryOrResourceFile;
        }
    }

    private String getOutputPath() {
        return outputDirectory + File.separator + outputFile.getName();
    }

    private List<File> getExtraPathDirectoriesWithDefault() {
        if (extraPathDirectories == null) {
            return Collections.singletonList(defaultExtraPath);
        } else {
            return Arrays.asList(extraPathDirectories);
        }
    }

    public void ensureOutputDirectoryExists()
            throws IOException {
        if (outputDirectory == null) {
            String baseDir = System.getProperty("basedir");
            // FIXME: this is to get around the problem that default-value does not currently work
            if (baseDir == null)
                baseDir = ".";
            outputDirectory = new File(joinPaths(baseDir, "target", "robotframework", "libdoc"));
        }
        if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw new IOException("Target output directory cannot be created: " + outputDirectory.getAbsolutePath());
        }
    }

    private String joinPaths(String... parts) {
        return StringUtils.join(parts, File.separator);
    }

    /**
     * The format of the created documentation. May be either <code>HTML</code> or <code>XML</code>.
     * <p/>
     * If not specified, the format is gotten from the extension of the {@link #outputFile}.
     *
     * @parameter expression="${format}"
     */

    private String format;

    /**
     * Specifies the directory where documentation files are written. Considered to be relative to the ${basedir} of the
     * project.
     *
     * @parameter expression="${output}" default-value="${project.build.directory}/robotframework/libdoc"
     */
    private File outputDirectory;

    /**
     * Specifies the filename of the created documentation. Considered to be relative to the {@link #outputDirectory} of
     * the project.
     *
     * @parameter expression="${outputFile}"
     * @required
     */
    private File outputFile;

    /**
     * Sets the name of the documented library or resource.
     *
     * @parameter expression="${name}"
     */
    private String name;

    /**
     * Name or path of the documented library or resource file.
     * <p/>
     * Name must be in the same format as when used in Robot Framework test data, for example <code>BuiltIn</code> or
     * <code>com.acme.FooLibrary</code>. When name is used, the library is imported the same as when running the tests.
     * Use {@link #extraPathDirectories} to set PYTHONPATH/CLASSPATH accordingly.
     * <p/>
     * Paths are considered relative to the location of <code>pom.xml</code> and must point to a valid Python/Java
     * source file or a resource file. For example <code>src/main/java/com/test/ExampleLib.java</code>
     *
     * @parameter expression="${libraryOrResourceFile}"
     * @required
     */
    private String libraryOrResourceFile;

    /**
     * A directory to be added to the PYTHONPATH/CLASSPATH when creating documentation.
     * <p/>
     * e.g. src/main/java/com/test/
     *
     * @parameter expression="${extraPathDirectories}"
     */
    private File[] extraPathDirectories;

    /**
     * The default location where extra packages will be searched. Effective if extraPathDirectories attribute is not
     * used. Cannot be overridden.
     *
     * @parameter default-value="${project.basedir}/src/test/resources/robotframework/libraries"
     * @required
     * @readonly
     */
    private File defaultExtraPath;
}
