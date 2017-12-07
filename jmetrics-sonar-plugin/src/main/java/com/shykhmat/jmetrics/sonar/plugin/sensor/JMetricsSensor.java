package com.shykhmat.jmetrics.sonar.plugin.sensor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.Measure;

import com.shykhmat.jmetrics.core.api.JMetricsApi;
import com.shykhmat.jmetrics.core.report.ProjectReport;
import com.shykhmat.jmetrics.sonar.plugin.html.JMetricsHTMLGenerator;
import com.shykhmat.jmetrics.sonar.plugin.metric.JMetrics;

/**
 * Sonar sensor to process a project and calculate metrics.
 */
public class JMetricsSensor implements Sensor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMetricsSensor.class);
    private JMetricsApi jMetricsApi;
    private JMetricsHTMLGenerator jMetricsHTMLGenerator;
    private final FileSystem fileSystem;

    public JMetricsSensor(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        jMetricsApi = new JMetricsApi();
        jMetricsHTMLGenerator = new JMetricsHTMLGenerator(jMetricsApi);
    }

    @Override
    public boolean shouldExecuteOnProject(org.sonar.api.resources.Project project) {
        return true;
    }

    @Override
    public void analyse(org.sonar.api.resources.Project module, SensorContext context) {
        File projectDir = fileSystem.baseDir();
        LOGGER.info("Calculation Maintainability Index for project: " + projectDir.getAbsolutePath());
        ProjectReport project = jMetricsApi.calculateMetrics(projectDir.getAbsolutePath());
        LOGGER.info("Calculation done");
        LOGGER.info("Preparing Maintainability Index report");
        Measure<String> measure = new Measure<String>(JMetrics.JMETRICS, jMetricsHTMLGenerator.toHTML(project));
        context.saveMeasure(measure);
        measure = new Measure<String>(JMetrics.JMETRICS_EXCEL, new String(jMetricsApi.writeMetricsToExcel(project)));
        context.saveMeasure(measure);
        LOGGER.info("Report done");
    }

}
