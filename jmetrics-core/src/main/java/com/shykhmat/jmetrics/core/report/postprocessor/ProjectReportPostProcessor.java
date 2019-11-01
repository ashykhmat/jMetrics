package com.shykhmat.jmetrics.core.report.postprocessor;

import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class that is used to calculate different metrics after project is completely
 * parsed and primary metrics are calculated from code.
 */
public class ProjectReportPostProcessor {
	private ClassReportPostProcessor classReportPostProcessor = new ClassReportPostProcessor();
	private PackageReportPostProcessor packageReportPostProcessor = new PackageReportPostProcessor();

	public void process(ProjectReport projectReport) {
		classReportPostProcessor.process(projectReport.getClasses());
		packageReportPostProcessor.process(projectReport);
	}

}
