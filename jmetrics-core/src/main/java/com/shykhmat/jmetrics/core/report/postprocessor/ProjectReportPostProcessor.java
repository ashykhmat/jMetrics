package com.shykhmat.jmetrics.core.report.postprocessor;

import java.util.stream.Collectors;

import com.shykhmat.jmetrics.core.metric.coupling.CircularDependenciesCalculator;
import com.shykhmat.jmetrics.core.report.Metrics;
import com.shykhmat.jmetrics.core.report.PackageReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class that is used to calculate different metrics after project is completely
 * parsed and primary metrics are calculated from code.
 */
public class ProjectReportPostProcessor {
	private ClassReportPostProcessor classReportPostProcessor = new ClassReportPostProcessor();
	private PackageReportPostProcessor packageReportPostProcessor = new PackageReportPostProcessor();
	private CircularDependenciesCalculator circularDependenciesCalculator = new CircularDependenciesCalculator();

	public void process(ProjectReport projectReport) {
		classReportPostProcessor.process(projectReport.getClasses());
		packageReportPostProcessor.process(projectReport);
		Metrics metrics = new Metrics();
		metrics.setLinesOfCode(projectReport.getPackages().stream().map(PackageReport::getMetrics)
				.map(Metrics::getLinesOfCode).collect(Collectors.summingInt(Integer::intValue)));
		metrics.setCyclomaticComplexity(projectReport.getPackages().stream().map(PackageReport::getMetrics)
				.map(Metrics::getCyclomaticComplexity).collect(Collectors.summingInt(Integer::intValue)));
		projectReport.setMetrics(metrics);
		projectReport.setClassesCircularDependencies(
				circularDependenciesCalculator.calculateClassesCircularDependencies(projectReport.getClasses()));
		projectReport.setPackagesCircularDependencies(
				circularDependenciesCalculator.calculatePackagesCircularDependencies(projectReport.getPackages()));
	}

}
