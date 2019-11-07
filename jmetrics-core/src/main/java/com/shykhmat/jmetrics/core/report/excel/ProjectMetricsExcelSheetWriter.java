package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link AbstractExcelSheetWriter} to write Project metrics.
 */
public class ProjectMetricsExcelSheetWriter extends AbstractExcelSheetWriter {

	public void writeProjectMetrics(ProjectReport project, XSSFWorkbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		XSSFSheet worksheet = createSheet(workbook, "Project");
		Row row = worksheet.createRow(1);
		row.createCell(0).setCellValue(project.getName());
		row.createCell(1).setCellValue(project.getMetrics().getCyclomaticComplexity());
		row.createCell(2).setCellValue(project.getMetrics().getLinesOfCode());
		Cell classesCircularDependenciesSizeCell = row.createCell(3);
		classesCircularDependenciesSizeCell.setCellValue(project.getClassesCircularDependenciesSize());
		if (project.getClassesCircularDependenciesSize() > 0) {
			classesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
		Cell packagesCircularDependenciesSizeCell = row.createCell(4);
		packagesCircularDependenciesSizeCell.setCellValue(project.getPackagesCircularDependenciesSize());
		if (project.getPackagesCircularDependenciesSize() > 0) {
			packagesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
		formatAsATable(workbook, worksheet, "Project", Arrays.asList(new String[] { "Project", "Cyclomatic Complexity",
				"Lines Of Code", "Classes Circular Dependencies", "Packages Circular Dependencies" }), 1);
	}
}
