package org.robotframework.mavenplugin;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;


/*
 * ...
 * @author Dietrich Schulten
 * @goal report
 * @phase site
 */
public class Report extends AbstractMavenReport {
    /**
     * Directory where reports will go.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    private String outputDirectory;

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     * @required
     * @readonly
     */
    private Renderer siteRenderer;

    @Override
    protected void executeReport(Locale locale)
            throws MavenReportException {
        Sink sink = getSink();
        sink.tableCell();
        sink.text("some text");
        sink.tableCell_();

    }

    protected MavenProject getProject() {
        return project;
    }

    protected String getOutputDirectory() {
        return outputDirectory;
    }

    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    public String getDescription(Locale locale) {
        return getBundle(locale).getString("report.robotframework.description");
    }

    public String getName(Locale locale) {
        return getBundle(locale).getString("report.robotframework.name");
    }

    public String getOutputName() {
        return "robotframework-report";
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("report", locale, this.getClass().getClassLoader());
    }

    public boolean isExternalReport() {
        return false;
    }
}
