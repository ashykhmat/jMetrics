package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.report.PackageReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link AbstractExcelSheetWriter} to write Package coupling
 * metrics.
 */
public class PackageCouplingMetricsExcelSheetWriter extends AbstractExcelSheetWriter {

	public void writePackagesCouplings(ProjectReport project, XSSFWorkbook workbook) {
		writePackagesEfferentCouplings(project, workbook);
		writePackagesAfferentCouplings(project, workbook);
	}

	private void writePackagesEfferentCouplings(ProjectReport project, XSSFWorkbook workbook) {
		XSSFSheet worksheet = createSheet(workbook, "Packages Efferent Couplings");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			String packageName = concretePackage.getName();
			for (String coupling : concretePackage.getEfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(packageName);
				row.createCell(1).setCellValue(coupling);
			}
		}
		formatAsATable(workbook, worksheet, "PackagesEfferentCouplings",
				Arrays.asList(new String[] { "Package", "Uses" }), index - 1);
	}

	private void writePackagesAfferentCouplings(ProjectReport project, XSSFWorkbook workbook) {
		XSSFSheet worksheet = createSheet(workbook, "Packages Afferent Couplings");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			String packageName = concretePackage.getName();
			for (String coupling : concretePackage.getAfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(packageName);
				row.createCell(1).setCellValue(coupling);
			}
		}
		formatAsATable(workbook, worksheet, "PackagesAfferentCouplings",
				Arrays.asList(new String[] { "Package", "Used By" }), index - 1);
	}
}
