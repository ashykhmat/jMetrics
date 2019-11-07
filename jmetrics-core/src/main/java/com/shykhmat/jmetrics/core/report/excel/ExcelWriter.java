
package com.shykhmat.jmetrics.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class to write metrics report into Excel file.
 */
public class ExcelWriter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);
	private static final String EXCEL_EXTENSION = ".xlsx";

	private final ProjectMetricsExcelSheetWriter projectMetricsExcelSheetWriter;
	private final ClassMetricsExcelSheetWriter classMetricsExcelSheetWriter;
	private final MethodMetricsExcelSheetWriter methodMetricsExcelSheetWriter;
	private final ClassCouplingMetricsExcelSheetWriter classCouplingMetricsExcelSheetWriter;
	private final PackageMetricsExcelSheetWriter packageMetricsExcelSheetWriter;
	private final PackageCouplingMetricsExcelSheetWriter packageCouplingMetricsExcelSheetWriter;
	private final ClassCircularDependenciesExcelSheetWriter classCircularDependenciesExcelSheetWriter;
	private final PackageCircularDependenciesExcelSheetWriter packageCircularDependenciesExcelSheetWriter;

	public ExcelWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		projectMetricsExcelSheetWriter = new ProjectMetricsExcelSheetWriter();
		classMetricsExcelSheetWriter = new ClassMetricsExcelSheetWriter(maintainabilityIndexStatusResolver);
		methodMetricsExcelSheetWriter = new MethodMetricsExcelSheetWriter(maintainabilityIndexStatusResolver);
		classCouplingMetricsExcelSheetWriter = new ClassCouplingMetricsExcelSheetWriter();
		packageMetricsExcelSheetWriter = new PackageMetricsExcelSheetWriter();
		packageCouplingMetricsExcelSheetWriter = new PackageCouplingMetricsExcelSheetWriter();
		classCircularDependenciesExcelSheetWriter = new ClassCircularDependenciesExcelSheetWriter();
		packageCircularDependenciesExcelSheetWriter = new PackageCircularDependenciesExcelSheetWriter();
	}

	public boolean writeMetricsToExcel(String pathToFile, ProjectReport project) {
		try (XSSFWorkbook workbook = writeToXSSFWorkbook(project)) {
			writeToFile(fixReportPath(project, pathToFile), workbook);
		} catch (IOException e) {
			LOGGER.error("Error during writing metrics to Excel file: ", e);
			return false;
		}
		return true;
	}

	public byte[] writeMetricsToExcel(ProjectReport project) {
		XSSFWorkbook workbook = writeToXSSFWorkbook(project);
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

	private XSSFWorkbook writeToXSSFWorkbook(ProjectReport project) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Map<Status, CellStyle> statusCellStyles = prepareStatusCellStyles(workbook);
		projectMetricsExcelSheetWriter.writeProjectMetrics(project, workbook, statusCellStyles);
		classMetricsExcelSheetWriter.writeClassesMetrics(project, workbook, statusCellStyles);
		methodMetricsExcelSheetWriter.writeMethodsMetrics(project, workbook, statusCellStyles);
		classCouplingMetricsExcelSheetWriter.writeClassesCouplings(project, workbook);
		packageMetricsExcelSheetWriter.writePackageMetrics(project, workbook);
		packageCouplingMetricsExcelSheetWriter.writePackagesCouplings(project, workbook);
		classCircularDependenciesExcelSheetWriter.writeClassesCircularDependencies(project, workbook);
		packageCircularDependenciesExcelSheetWriter.writePackagesCircularDependencies(project, workbook);
		return workbook;
	}

	private Map<Status, CellStyle> prepareStatusCellStyles(XSSFWorkbook workbook) {
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

	private void writeToFile(String pathToFile, XSSFWorkbook workbook) throws IOException {
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

}
