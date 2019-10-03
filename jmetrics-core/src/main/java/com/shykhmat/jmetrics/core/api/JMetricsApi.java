
package com.shykhmat.jmetrics.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexStatusResolver;
import com.shykhmat.jmetrics.core.report.ProjectReport;
import com.shykhmat.jmetrics.core.report.excel.ExcelWriter;
import com.shykhmat.jmetrics.core.visitor.ProjectVisitor;
import com.shykhmat.jmetrics.core.visitor.VisitorException;

/**
 * Public API for metrics calculation and report generation functionality.
 */
public class JMetricsApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(JMetricsApi.class);
	private ProjectVisitor projectVisitor;
	private MetricStatusResolver<Double> maintainabilityIndexStatusResolver;
	private ExcelWriter excelWriter;

	public JMetricsApi() {
		projectVisitor = new ProjectVisitor();
		maintainabilityIndexStatusResolver = new MaintainabilityIndexStatusResolver();
		excelWriter = new ExcelWriter(maintainabilityIndexStatusResolver);
	}

	/**
	 * Method to calculate metrics for a project.
	 * 
	 * @param projectPath - location of a project on a hard drive
	 * @return - project content (names of classes, methods) and their metrics
	 */
	public ProjectReport calculateMetrics(String projectPath) {
		try {
			LOGGER.info("Calculating metrics for project {}", projectPath);
			return projectVisitor.visit(projectPath);
		} catch (VisitorException e) {
			LOGGER.error("Error during metrics calculation", e);
			return null;
		}
	}

	/**
	 * Method to check if maintainability index is applicable or should be fixed.
	 * 
	 * @param maintainabilityIndex
	 * @return {@link Status}
	 */
	public Status getMaintainabilityIndexStatus(double maintainabilityIndex) {
		return maintainabilityIndexStatusResolver.getStatus(maintainabilityIndex);
	}

	/**
	 * Method to write project metrics into concrete file.
	 * 
	 * @param reportPath - location of a report file that will be used to write
	 *                   metrics
	 * @param project    - project metrics
	 * @return true if metrics were written successfully, false in another case
	 */
	public boolean writeMetricsToExcel(String reportPath, ProjectReport project) {
		return excelWriter.writeMetricsToExcel(reportPath, project);
	}

	/**
	 * Method to write project metrics into array of bytes.
	 * 
	 * @param project - project metrics
	 * @return array of bytes that contains Excel workbook with report
	 */
	public byte[] writeMetricsToExcel(ProjectReport project) {
		return excelWriter.writeMetricsToExcel(project);
	}

	/**
	 * Method to calculate metrics for project and write them into Excel report.
	 * 
	 * @param projectPath - location of a project to calculate metrics
	 * @param reportPath  - location of a report file that will be used to write
	 *                    metrics
	 * @return true if metrics were written successfully, false in another case
	 */
	public boolean writeMetricsToExcel(String projectPath, String reportPath) {
		ProjectReport project = calculateMetrics(projectPath);
		boolean isSuccessful = writeMetricsToExcel(reportPath, project);
		if (isSuccessful) {
			LOGGER.info("Metrics were written successfully");
		}
		return isSuccessful;
	}

}