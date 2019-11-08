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
import com.shykhmat.jmetrics.core.report.MethodReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Implementation of {@link MaintainabilityIndexExcelSheetWriter} to write
 * Method metrics.
 */
public class MethodMetricsExcelSheetWriter extends CoreIndexExcelSheetWriter {

	public MethodMetricsExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		super(maintainabilityIndexStatusResolver);
	}

	public void writeMethodsMetrics(ProjectReport project, XSSFWorkbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		XSSFSheet worksheet = createSheet(workbook, "Methods");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			for (MethodReport method : concreteClass.getMethods()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(concreteClass.getName() + "." + method.getName());
				row.createCell(1).setCellValue(method.getMetrics().getLinesOfCode());
				createMaintainabilityIndexCell(statusCellStyles, method.getMetrics().getMaintainabilityIndex(), row, 2);
				row.createCell(3).setCellValue(method.getMetrics().getCyclomaticComplexity());
				writeHalsteadMetrics(row, method.getMetrics().getHalsteadMetrics(), 4);
			}
		}
		formatAsATable(workbook, worksheet, "Methods",
				Arrays.asList(new String[] { "Method", "Lines Of Code", "Maintainability Index",
						"Cyclomatic Complexity", "Distinct Operators", "Distinct Operands", "Occurences of Operators",
						"Occurences of Operands", "Program Length", "Halstead Vocabulary", "Estimated Length",
						"Purity Ratio", "Halstead Volume", "Program Difficulty", "Programming Effort",
						"Programming Time" }),
				index - 1);
	}
}
