
package com.shykhmat.jmetrics.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import com.shykhmat.jmetrics.core.report.PackageReport;
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
		writeProjectMetrics(project, workbook, statusCellStyles);
		writeClassesMetrics(project, workbook, statusCellStyles);
		writeMethodsMetrics(project, workbook, statusCellStyles);
		writeClassesCouplings(project, workbook);
		writePackageMetrics(project, workbook);
		writePackagesCouplings(project, workbook);
		writeClassesCircularDependencies(project, workbook);
		writePackagesCircularDependencies(project, workbook);
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

	private Sheet createSheet(Workbook workbook, String name) {
		int sheetIndex = workbook.getSheetIndex(name);
		if (sheetIndex >= 0) {
			workbook.removeSheetAt(sheetIndex);
		}
		return workbook.createSheet(name);
	}

	private void writeProjectMetrics(ProjectReport project, Workbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		Sheet worksheet = createSheet(workbook, "Project");
		createProjectHeader(worksheet);
		Row row = worksheet.createRow(1);
		row.createCell(0).setCellValue(project.getName());
		row.createCell(1).setCellValue(project.getMetrics().getCyclomaticComplexity());
		row.createCell(2).setCellValue(project.getMetrics().getLinesOfCode());
		Cell classesCircularDependenciesSizeCell = row.createCell(3);
		classesCircularDependenciesSizeCell.setCellValue(project.getClassesCircularDependenciesSize());
		if (project.getClassesCircularDependenciesSize() > 0) {
			classesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
		Cell packagesCircularDependenciesSizeCell = row.createCell(4);
		packagesCircularDependenciesSizeCell.setCellValue(project.getPackagesCircularDependenciesSize());
		if (project.getPackagesCircularDependenciesSize() > 0) {
			packagesCircularDependenciesSizeCell.setCellStyle(statusCellStyles.get(Status.ERROR));
		}
	}

	private void createProjectHeader(Sheet worksheet) {
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue("Project");
		header.createCell(1).setCellValue("Cyclomatic Complexity");
		header.createCell(2).setCellValue("Lines Of Code");
		header.createCell(3).setCellValue("Number of classes cyclomatic dependencies");
		header.createCell(4).setCellValue("Number of packages cyclomatic dependencies");
	}

	private void writeClassesMetrics(ProjectReport project, Workbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		Sheet worksheet = createSheet(workbook, "Classes");
		createClassesHeader(worksheet);
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
		header.createCell(5).setCellValue("Efferent Coupling");
		header.createCell(6).setCellValue("Afferent Coupling");
		header.createCell(7).setCellValue("Instability");
	}

	private void writeMethodsMetrics(ProjectReport project, Workbook workbook,
			Map<Status, CellStyle> statusCellStyles) {
		Sheet worksheet = createSheet(workbook, "Methods");
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

	private void writePackageMetrics(ProjectReport project, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, "Packages");
		createPackagesHeader(worksheet);
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
		;
	}

	private void createPackagesHeader(Sheet worksheet) {
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue("Package");
		header.createCell(1).setCellValue("Cyclomatic Complexity");
		header.createCell(2).setCellValue("Lines Of Code");
		header.createCell(3).setCellValue("Abstract Classes and Interfaces");
		header.createCell(4).setCellValue("Non-Abstract Classes");
		header.createCell(5).setCellValue("Abstractness");
		header.createCell(6).setCellValue("Efferent Coupling");
		header.createCell(7).setCellValue("Afferent Coupling");
		header.createCell(8).setCellValue("Instability");
		header.createCell(9).setCellValue("Distance");
	}

	private void writeClassesCouplings(ProjectReport project, Workbook workbook) {
		writeClassesEfferentCouplings(project, workbook);
		writeClassesAfferentCouplings(project, workbook);
	}

	private void writeClassesEfferentCouplings(ProjectReport project, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, "Classes Efferent Couplings");
		createCouplingsHeader(worksheet, "Class", "Uses");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			String className = concreteClass.getName();
			for (String coupling : concreteClass.getEfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(className);
				row.createCell(1).setCellValue(coupling);
			}
		}
	}

	private void writeClassesAfferentCouplings(ProjectReport project, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, "Classes Afferent Couplings");
		createCouplingsHeader(worksheet, "Class", "Used By");
		int index = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			String className = concreteClass.getName();
			for (String coupling : concreteClass.getAfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(className);
				row.createCell(1).setCellValue(coupling);
			}
		}
	}

	private void writePackagesCouplings(ProjectReport project, Workbook workbook) {
		writePackagesEfferentCouplings(project, workbook);
		writePackagesAfferentCouplings(project, workbook);
	}

	private void writePackagesEfferentCouplings(ProjectReport project, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, "Packages Efferent Couplings");
		createCouplingsHeader(worksheet, "Package", "Uses");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			String packageName = concretePackage.getName();
			for (String coupling : concretePackage.getEfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(packageName);
				row.createCell(1).setCellValue(coupling);
			}
		}
	}

	private void writePackagesAfferentCouplings(ProjectReport project, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, "Packages Afferent Couplings");
		createCouplingsHeader(worksheet, "Package", "Used By");
		int index = 1;
		for (PackageReport concretePackage : project.getPackages()) {
			String packageName = concretePackage.getName();
			for (String coupling : concretePackage.getAfferentCouplingUsed()) {
				Row row = worksheet.createRow(index++);
				row.createCell(0).setCellValue(packageName);
				row.createCell(1).setCellValue(coupling);
			}
		}
	}

	private void createCouplingsHeader(Sheet worksheet, String unitType, String couplingLabel) {
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue(unitType);
		header.createCell(1).setCellValue(couplingLabel);
	}

	private void writeClassesCircularDependencies(ProjectReport project, Workbook workbook) {
		writeCircularDependencies(project.getClassesCircularDependencies(), "Classes", workbook);
	}

	private void writePackagesCircularDependencies(ProjectReport project, Workbook workbook) {
		writeCircularDependencies(project.getPackagesCircularDependencies(), "Packages", workbook);
	}

	private void writeCircularDependencies(Set<String> circularDependencies, String unitName, Workbook workbook) {
		Sheet worksheet = createSheet(workbook, unitName + " Circular Dependencies");
		Row header = worksheet.createRow(0);
		header.createCell(0).setCellValue("Circular Dependencies");
		int index = 1;
		for (String circularDependency : circularDependencies) {
			Row row = worksheet.createRow(index++);
			row.createCell(0).setCellValue(circularDependency);
		}
	}

}
