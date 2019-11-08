package com.shykhmat.jmetrics.core.report.postprocessor;

import com.shykhmat.jmetrics.core.metric.coupling.CircularDependenciesCalculator;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexCalculator;
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
	private MaintainabilityIndexCalculator maintainabilityIndexCalculator = new MaintainabilityIndexCalculator();

	public void process(ProjectReport projectReport) {
		classReportPostProcessor.process(projectReport.getClasses());
		packageReportPostProcessor.process(projectReport);
		Metrics metrics = processCoreMetrics(projectReport);
		projectReport.setMetrics(metrics);
		projectReport.setClassesCircularDependencies(
				circularDependenciesCalculator.calculateClassesCircularDependencies(projectReport.getClasses()));
		projectReport.setPackagesCircularDependencies(
				circularDependenciesCalculator.calculatePackagesCircularDependencies(projectReport.getPackages()));
	}

	private Metrics processCoreMetrics(ProjectReport projectReport) {
		Metrics metrics = new Metrics();
		Integer linesOfCode = 0;
		Integer cyclomaticComplexity = 0;
		Double halsteadVolume = 0.;
		for (PackageReport packageReport : projectReport.getPackages()) {
			Metrics packageMetrics = packageReport.getMetrics();
			linesOfCode += packageMetrics.getLinesOfCode();
			cyclomaticComplexity += packageMetrics.getCyclomaticComplexity();
			halsteadVolume += packageMetrics.getHalsteadMetrics().getVolume();
		}
		int classesNumber = projectReport.getClasses().size();
		classesNumber = classesNumber == 0 ? 1 : classesNumber;
		int packagesNumber = projectReport.getPackages().size();
		packagesNumber = packagesNumber == 0 ? 1 : packagesNumber;
		metrics.setCyclomaticComplexity(cyclomaticComplexity / packagesNumber);
		metrics.setLinesOfCode(linesOfCode / classesNumber);
		metrics.getHalsteadMetrics().setVolume(halsteadVolume / packagesNumber);
		metrics.setMaintainabilityIndex(maintainabilityIndexCalculator.calculateMetric(metrics));
		metrics.setLinesOfCode(linesOfCode);
		return metrics;
	}

}
