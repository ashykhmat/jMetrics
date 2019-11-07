package com.shykhmat.jmetrics.core.report.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link CircularDependenciesExcelSheetWriter} for package
 * unit.
 */
public class PackageCircularDependenciesExcelSheetWriter extends CircularDependenciesExcelSheetWriter {

	public void writePackagesCircularDependencies(ProjectReport project, XSSFWorkbook workbook) {
		writeCircularDependencies(project.getPackagesCircularDependencies(), "Packages", workbook);
	}
}
