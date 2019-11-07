package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.report.PackageReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link AbstractExcelSheetWriter} to write Package metrics.
 */
public class PackageMetricsExcelSheetWriter extends AbstractExcelSheetWriter {

	public void writePackageMetrics(ProjectReport project, XSSFWorkbook workbook) {
		XSSFSheet worksheet = createSheet(workbook, "Packages");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(concretePackage.getName());
			row.createCell(1).setCellValue(concretePackage.getMetrics().getCyclomaticComplexity());
			row.createCell(2).setCellValue(concretePackage.getMetrics().getLinesOfCode());
			row.createCell(3).setCellValue(concretePackage.getAbstractClassesInterfacesNumber());
			row.createCell(4).setCellValue(concretePackage.getNonAbstractClassesNumber());
			row.createCell(5).setCellValue(concretePackage.getAbstractness());
			row.createCell(6).setCellValue(concretePackage.getEfferentCouplingUsed().size());
			row.createCell(7).setCellValue(concretePackage.getAfferentCouplingUsed().size());
			row.createCell(8).setCellValue(concretePackage.getInstability());
			row.createCell(9).setCellValue(concretePackage.getDistance());
		}
		formatAsATable(workbook, worksheet, "Packages",
				Arrays.asList(new String[] { "Package", "Cyclomatic Complexity", "Lines Of Code",
						"Abstract Classes and Interfaces", "Non-Abstract Classes", "Abstractness", "Efferent Coupling",
						"Afferent Coupling", "Instability", "Distance" }),
				index - 1);
	}

}
