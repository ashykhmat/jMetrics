
package com.shykhmat.jmetrics.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.MethodReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class to write metrics report into Excel file.
 */
public class ExcelWriter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);
	private static final String EXCEL_EXTENSION = ".xlsx";
	private MetricStatusResolver<Double> maintainabilityIndexStatusResolver;

	public ExcelWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		this.maintainabilityIndexStatusResolver = maintainabilityIndexStatusResolver;
	}

	public boolean writeMetricsToExcel(String pathToFile, ProjectReport project) {
		try (Workbook workbook = writeToWorkbook(project)) {
			writeToFile(fixReportPath(project, pathToFile), workbook);
		} catch (IOException e) {
			LOGGER.error("Error during writing metrics to Excel file: ", e);
			return false;
		}
		return true;
	}

	public byte[] writeMetricsToExcel(ProjectReport project) {
		Workbook workbook = writeToWorkbook(project);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			try {
				workbook.write(outputStream);
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			LOGGER.error("Error during writing metrics to Excel stream: ", e);
		}
		return Base64.getEncoder().encode(outputStream.toByteArray());
	}

	private Workbook writeToWorkbook(ProjectReport project) {
		Workbook workbook = new SXSSFWorkbook();
		Map<Status, CellStyle> statusCellStyles = prepareStatusCellStyles(workbook);
		writeClassesMetrics(project, workbook, statusCellStyles);
		writeMethodsMetrics(project, workbook, statusCellStyles);
		return workbook;
	}

	private Map<Status, CellStyle> prepareStatusCellStyles(Workbook workbook) {
		CellStyle okCellStyle = workbook.createCellStyle();
		okCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		okCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		CellStyle warningCellStyle = workbook.createCellStyle();
		warningCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		warningCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		CellStyle errorCellStyle = workbook.createCellStyle();
		errorCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		errorCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Map<Status, CellStyle> statusCellStyles = new HashMap<>();
		statusCellStyles.put(Status.OK, okCellStyle);
		statusCellStyles.put(Status.WARNING, warningCellStyle);
		statusCellStyles.put(Status.ERROR, errorCellStyle);
		return statusCellStyles;
	}

	private String fixReportPath(ProjectReport project, String reportPath) {
		if (!reportPath.endsWith(EXCEL_EXTENSION)) {
			String projectName = project.getName();
			if (!reportPath.endsWith(File.separator)) {
				projectName = File.separator + projectName;
			}
			reportPath += projectName + EXCEL_EXTENSION;
		}
		return reportPath;
	}

	private void writeToFile(String pathToFile, Workbook workbook) throws IOException {
		LOGGER.info("Writing report file {}", pathToFile);
		OutputStream fileOutStream = null;
		File fileOut = new File(pathToFile);
		Files.createParentDirs(fileOut);
		Files.touch(fileOut);
		try {
			fileOutStream = new FileOutputStream(pathToFile);
			workbook.write(fileOutStream);
		} finally {
			if (fileOutStream != null) {
				fileOutStream.close();
			}
		}
	}

	private void writeClassesMetrics(ProjectReport project, Workbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		Sheet worksheet = createClassesSheet(workbook);
		createClassesHeader(worksheet);
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(concreteClass.getName());
			createMaintainabilityIndexCell(statusCellStyles, concreteClass.getMetrics().getMaintainabilityIndex(), row);
			row.createCell(2).setCellValue(concreteClass.getMetrics().getCyclomaticComplexity());
			row.createCell(3).setCellValue(concreteClass.getMetrics().getLinesOfCode());
			row.createCell(4).setCellValue(concreteClass.getMetrics().getHalsteadVolume());
		}
	}

	private void createMaintainabilityIndexCell(Map<Status, CellStyle> statusCellStyles, Double maintainabilityIndex,
			Row row) {
		Cell maintainabilityIndexCell = row.createCell(1);
		maintainabilityIndexCell.setCellValue(maintainabilityIndex);
		maintainabilityIndexCell
				.setCellStyle(statusCellStyles.get(maintainabilityIndexStatusResolver.getStatus(maintainabilityIndex)));
	}

	private void createClassesHeader(Sheet worksheet) {
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue("Class");
		header.createCell(1).setCellValue("Maintainability Index");
		header.createCell(2).setCellValue("Cyclomatic Complexity");
		header.createCell(3).setCellValue("Lines Of Code");
		header.createCell(4).setCellValue("Halstead Volume");
	}

	private Sheet createClassesSheet(Workbook workbook) {
		int classesSheetIndex = workbook.getSheetIndex("Classes");
		if (classesSheetIndex >= 0) {
			workbook.removeSheetAt(classesSheetIndex);
		}
		return workbook.createSheet("Classes");
	}

	private void writeMethodsMetrics(ProjectReport project, Workbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		Sheet worksheet = createMethodsSheet(workbook);
		createMethodsHeader(worksheet);
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			for (MethodReport method : concreteClass.getMethods()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(concreteClass.getName() + "." + method.getName());
				createMaintainabilityIndexCell(statusCellStyles, method.getMetrics().getMaintainabilityIndex(), row);
				row.createCell(2).setCellValue(method.getMetrics().getCyclomaticComplexity());
				row.createCell(3).setCellValue(method.getMetrics().getLinesOfCode());
				row.createCell(4).setCellValue(method.getMetrics().getHalsteadVolume());
			}
		}
	}

	private void createMethodsHeader(Sheet worksheet) {
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue("Method");
		header.createCell(1).setCellValue("Maintainability Index");
		header.createCell(2).setCellValue("Cyclomatic Complexity");
		header.createCell(3).setCellValue("Lines Of Code");
		header.createCell(4).setCellValue("Halstead Volume");
	}

	private Sheet createMethodsSheet(Workbook workbook) {
		int methodsSheetIndex = workbook.getSheetIndex("Methods");
		if (methodsSheetIndex >= 0) {
			workbook.removeSheetAt(methodsSheetIndex);
		}
		return workbook.createSheet("Methods");
	}

}
