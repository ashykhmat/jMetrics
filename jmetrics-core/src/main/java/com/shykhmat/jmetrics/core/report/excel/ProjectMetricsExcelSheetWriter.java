package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link MaintainabilityIndexExcelSheetWriter} to write
 * Project metrics.
 */
public class ProjectMetricsExcelSheetWriter extends CoreIndexExcelSheetWriter {

	public ProjectMetricsExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		super(maintainabilityIndexStatusResolver);
	}

	public void writeProjectMetrics(ProjectReport project, XSSFWorkbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		XSSFSheet worksheet = createSheet(workbook, "Project");
		Row row = worksheet.createRow(1);
		row.createCell(0).setCellValue(project.getName());
		row.createCell(1).setCellValue(project.getMetrics().getLinesOfCode());
		createMaintainabilityIndexCell(statusCellStyles, project.getMetrics().getMaintainabilityIndex(), row, 2);
		row.createCell(3).setCellValue(project.getMetrics().getCyclomaticComplexity());
		Cell classesCircularDependenciesSizeCell = row.createCell(4);
		classesCircularDependenciesSizeCell.setCellValue(project.getClassesCircularDependenciesSize());
		if (project.getClassesCircularDependenciesSize() > 0) {
			classesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
		Cell packagesCircularDependenciesSizeCell = row.createCell(5);
		packagesCircularDependenciesSizeCell.setCellValue(project.getPackagesCircularDependenciesSize());
		if (project.getPackagesCircularDependenciesSize() > 0) {
			packagesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
		row.createCell(6).setCellValue(project.getMetrics().getHalsteadMetrics().getVolume());
		formatAsATable(workbook, worksheet, "Project",
				Arrays.asList(
						new String[] { "Project", "Lines Of Code", "Maintainability Index", "Cyclomatic Complexity",
								"Classes Circular Dependencies", "Packages Circular Dependencies", "Halstead Volume" }),
				1);
	}
}
