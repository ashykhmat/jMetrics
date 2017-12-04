
package com.shykhmat.jmetrics.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.MethodReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

public class ExcelWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);

    public boolean writeMetricsToExcel(String pathToFile, ProjectReport project) {
        try {
            Workbook workbook = writeToWorkbook(project);
            writeToFile(pathToFile, workbook);
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
        Workbook workbook = new XSSFWorkbook();
        writeClassesMetrics(project, workbook);
        writeMethodsMetrics(project, workbook);
        return workbook;
    }

    private void writeToFile(String pathToFile, Workbook workbook) throws IOException {
        OutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(pathToFile);
            workbook.write(fileOut);
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    private void writeClassesMetrics(ProjectReport project, Workbook workbook) {
        Sheet worksheet = createClassesSheet(workbook);
        createClassesHeader(worksheet);
        int index = 1;
        for (ClassReport concreteClass : project.getClasses()) {
            Row row = worksheet.createRow(index++);
            row.createCell(0).setCellValue(concreteClass.getName());
            row.createCell(1).setCellValue(concreteClass.getMetrics().getMaintainabilityIndex());
            row.createCell(2).setCellValue(concreteClass.getMetrics().getCyclomaticComplexity());
            row.createCell(3).setCellValue(concreteClass.getMetrics().getLinesOfCode());
            row.createCell(4).setCellValue(concreteClass.getMetrics().getHalsteadVolume());
        }
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

    private void writeMethodsMetrics(ProjectReport project, Workbook workbook) {
        Sheet worksheet = createMethodsSheet(workbook);
        createMethodsHeader(worksheet);
        int index = 1;
        for (ClassReport concreteClass : project.getClasses()) {
            for (MethodReport method : concreteClass.getMethods()) {
                Row row = worksheet.createRow(index++);
                row.createCell(0).setCellValue(concreteClass.getName() + "." + method.getName());
                row.createCell(1).setCellValue(method.getMetrics().getMaintainabilityIndex());
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
