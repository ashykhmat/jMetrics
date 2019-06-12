package com.shykhmat.jmetrics.maven.plugin;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import com.shykhmat.jmetrics.core.api.JMetricsApi;

/**
 * Plug-in for Maven to calculate different metrics for Java project and to display appropriate report during build.
 */
@Mojo(name = "jmetrics")
public class JMetricsMojo extends AbstractMavenReport {
    private static final String OUTPUT_NAME = "jMetrics";

    @Override
    public String getDescription(Locale locale) {
        return getBundle(locale).getString("report.jmetrics.description");
    }

    @Override
    public String getName(Locale locale) {
        return getBundle(locale).getString("report.jmetrics.name");
    }

    @Override
    public String getOutputName() {
        return OUTPUT_NAME;
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        JMetricsApi jMetricsApi = new JMetricsApi();
        jMetricsApi.writeMetricsToExcel(project.getBasedir().getPath(), outputDirectory.getPath());
    }

    // helper to retrieve the right bundle
    private static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("jmetrics", locale, JMetricsMojo.class.getClassLoader());
    }

}
