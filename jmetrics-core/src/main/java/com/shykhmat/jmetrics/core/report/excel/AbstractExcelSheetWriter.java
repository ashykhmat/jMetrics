package com.shykhmat.jmetrics.core.report.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Common class for all Excel Sheet writers.
 */
public abstract class AbstractExcelSheetWriter {

	protected XSSFSheet createSheet(XSSFWorkbook workbook, String name) {
		int sheetIndex = workbook.getSheetIndex(name);
		if (sheetIndex >= 0) {
			workbook.removeSheetAt(sheetIndex);
		}
		return workbook.createSheet(name);
	}

	protected void formatAsATable(XSSFWorkbook workbook, XSSFSheet worksheet, String tableName, List<String> columns,
			int rowsNumber) {
		AreaReference reference = workbook.getCreationHelper().createAreaReference(new CellReference(0, 0),
				new CellReference(rowsNumber, columns.size() - 1));
		XSSFTable table = worksheet.createTable(reference);
		table.setName(tableName);
		table.setDisplayName(tableName);
		table.getCTTable().addNewTableStyleInfo();
		table.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");
		Row headerRow = worksheet.createRow(0);
		int columnIndex = 0;
		for (String column : columns) {
			Cell cell = headerRow.createCell(columnIndex);
			cell.setCellValue(column);
			worksheet.autoSizeColumn(columnIndex++);
		}
	}

}
