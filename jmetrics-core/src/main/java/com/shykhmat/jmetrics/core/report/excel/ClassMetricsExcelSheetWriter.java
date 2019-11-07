package com.shykhmat.jmetrics.core.report.excel;

import java.util.Arrays;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link MaintainabilityIndexExcelSheetWriter} to write Class
 * metrics.
 */
public class ClassMetricsExcelSheetWriter extends MaintainabilityIndexExcelSheetWriter {

	public ClassMetricsExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		super(maintainabilityIndexStatusResolver);
	}

	public void writeClassesMetrics(ProjectReport project, XSSFWorkbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		XSSFSheet worksheet = createSheet(workbook, "Classes");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(concreteClass.getName());
			createMaintainabilityIndexCell(statusCellStyles, concreteClass.getMetrics().getMaintainabilityIndex(), row);
			row.createCell(2).setCellValue(concreteClass.getMetrics().getCyclomaticComplexity());
			row.createCell(3).setCellValue(concreteClass.getMetrics().getLinesOfCode());
			row.createCell(4).setCellValue(concreteClass.getMetrics().getHalsteadVolume());
			row.createCell(5).setCellValue(concreteClass.getEfferentCouplingUsed().size());
			row.createCell(6).setCellValue(concreteClass.getAfferentCouplingUsed().size());
			row.createCell(7).setCellValue(concreteClass.getInstability());
		}
		formatAsATable(workbook, worksheet, "Classes",
				Arrays.asList(new String[] { "Class", "Maintainability Index", "Cyclomatic Complexity", "Lines Of Code",
						"Halstead Volume", "Efferent Coupling", "Afferent Coupling", "Instability" }),
				index - 1);
	}
}
