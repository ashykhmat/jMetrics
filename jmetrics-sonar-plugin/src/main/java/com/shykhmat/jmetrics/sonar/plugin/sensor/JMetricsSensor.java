package com.shykhmat.jmetrics.sonar.plugin.sensor;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.scanner.sensor.ProjectSensor;

import com.shykhmat.jmetrics.core.api.JMetricsApi;
import com.shykhmat.jmetrics.core.report.ProjectReport;
import com.shykhmat.jmetrics.sonar.plugin.html.JMetricsHTMLGenerator;
import com.shykhmat.jmetrics.sonar.plugin.metric.JMetrics;

/**
 * Sonar sensor to process a project and calculate metrics.
 */
public class JMetricsSensor implements ProjectSensor {
	private static final Logger LOGGER = LoggerFactory.getLogger(JMetricsSensor.class);
	private JMetricsApi jMetricsApi;
	private JMetricsHTMLGenerator jMetricsHTMLGenerator;

	public JMetricsSensor() {
		jMetricsApi = new JMetricsApi();
		jMetricsHTMLGenerator = new JMetricsHTMLGenerator(jMetricsApi);
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Calculates different quality metrics");
	}

	@Override
	public void execute(SensorContext context) {
		File projectDir = context.fileSystem().baseDir();
		LOGGER.info("Calculation Maintainability Index for project: " + projectDir.getAbsolutePath());
		ProjectReport project = jMetricsApi.calculateMetrics(projectDir.getAbsolutePath());
		LOGGER.info("Calculation done");
		LOGGER.info("Preparing Maintainability Index report");
		context.<String>newMeasure().forMetric(JMetrics.JMETRICS).on(context.project())
				.withValue(jMetricsHTMLGenerator.toHTML(project)).save();
		context.<String>newMeasure().forMetric(JMetrics.JMETRICS_EXCEL).on(context.project())
				.withValue(new String(jMetricsApi.writeMetricsToExcel(project))).save();
		LOGGER.info("Report done");
	}
}
