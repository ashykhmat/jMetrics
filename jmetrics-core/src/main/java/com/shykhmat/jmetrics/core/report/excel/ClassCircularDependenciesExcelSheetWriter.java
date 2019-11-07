package com.shykhmat.jmetrics.core.report.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link CircularDependenciesExcelSheetWriter} for class
 * unit.
 */
public class ClassCircularDependenciesExcelSheetWriter extends CircularDependenciesExcelSheetWriter {

	public void writeClassesCircularDependencies(ProjectReport project, XSSFWorkbook workbook) {
		writeCircularDependencies(project.getClassesCircularDependencies(), "Classes", workbook);
	}
}
