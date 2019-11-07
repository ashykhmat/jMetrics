package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link AbstractExcelSheetWriter} to write Class coupling
 * metrics.
 */
public class ClassCouplingMetricsExcelSheetWriter extends AbstractExcelSheetWriter {

	public void writeClassesCouplings(ProjectReport project, XSSFWorkbook workbook) {
		writeClassesEfferentCouplings(project, workbook);
		writeClassesAfferentCouplings(project, workbook);
	}

	private void writeClassesEfferentCouplings(ProjectReport project, XSSFWorkbook workbook) {
		XSSFSheet worksheet = createSheet(workbook, "Classes Efferent Couplings");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			String className = concreteClass.getName();
			for (String coupling : concreteClass.getEfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(className);
				row.createCell(1).setCellValue(coupling);
			}
		}
		formatAsATable(workbook, worksheet, "ClassesEfferentCouplings", Arrays.asList(new String[] { "Class", "Uses" }),
				index - 1);
	}

	private void writeClassesAfferentCouplings(ProjectReport project, XSSFWorkbook workbook) {
		XSSFSheet worksheet = createSheet(workbook, "Classes Afferent Couplings");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			String className = concreteClass.getName();
			for (String coupling : concreteClass.getAfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(className);
				row.createCell(1).setCellValue(coupling);
			}
		}
		formatAsATable(workbook, worksheet, "ClassesAfferentCouplings",
				Arrays.asList(new String[] { "Class", "Used By" }), index - 1);
	}
}
