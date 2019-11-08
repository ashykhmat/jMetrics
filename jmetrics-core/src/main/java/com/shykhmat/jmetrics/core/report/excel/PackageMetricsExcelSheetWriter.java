package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.PackageReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link MaintainabilityIndexExcelSheetWriter} to write
 * Package metrics.
 */
public class PackageMetricsExcelSheetWriter extends CoreIndexExcelSheetWriter {

	public PackageMetricsExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		super(maintainabilityIndexStatusResolver);
	}

	public void writePackageMetrics(ProjectReport project, XSSFWorkbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		XSSFSheet worksheet = createSheet(workbook, "Packages");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(concretePackage.getName());
			row.createCell(1).setCellValue(concretePackage.getMetrics().getLinesOfCode());
			createMaintainabilityIndexCell(statusCellStyles, concretePackage.getMetrics().getMaintainabilityIndex(),
					row, 2);
			row.createCell(3).setCellValue(concretePackage.getMetrics().getCyclomaticComplexity());
			row.createCell(4).setCellValue(concretePackage.getAbstractClassesInterfacesNumber());
			row.createCell(5).setCellValue(concretePackage.getNonAbstractClassesNumber());
			row.createCell(6).setCellValue(concretePackage.getAbstractness());
			row.createCell(7).setCellValue(concretePackage.getEfferentCouplingUsed().size());
			row.createCell(8).setCellValue(concretePackage.getAfferentCouplingUsed().size());
			row.createCell(9).setCellValue(concretePackage.getInstability());
			row.createCell(10).setCellValue(concretePackage.getDistance());
			row.createCell(11).setCellValue(concretePackage.getMetrics().getHalsteadMetrics().getVolume());
		}
		formatAsATable(workbook, worksheet, "Packages",
				Arrays.asList(new String[] { "Package", "Lines Of Code", "Maintainability Index",
						"Cyclomatic Complexity", "Abstract Classes and Interfaces", "Non-Abstract Classes",
						"Abstractness", "Efferent Coupling", "Afferent Coupling", "Instability", "Distance",
						"Halstead Volume" }),
				index - 1);
	}

}
