package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Common implementation of {@link AbstractExcelSheetWriter} for Excel writers,
 * that work with circular dependencies metric.
 */
public abstract class CircularDependenciesExcelSheetWriter extends AbstractExcelSheetWriter {

	protected void writeCircularDependencies(Set<String> circularDependencies, String unitName, XSSFWorkbook workbook) {
		String worksheetName = unitName + " Circular Dependencies";
		XSSFSheet worksheet = createSheet(workbook, worksheetName);
		int index = 1;
		for (String circularDependency : circularDependencies) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(circularDependency);
		}
		formatAsATable(workbook, worksheet, worksheetName.replaceAll(" ", ""),
				Arrays.asList(new String[] { "Circular Dependencies" }), index == 1 ? 1 : index - 1);
	}
}
