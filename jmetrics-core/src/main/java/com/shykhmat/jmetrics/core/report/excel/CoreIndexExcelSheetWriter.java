package com.shykhmat.jmetrics.core.report.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.HalsteadComplexityMetrics;

/**
 * Common implementation of {@link AbstractExcelSheetWriter} for Excel writers,
 * that work with core metrics.
 */
public abstract class CoreIndexExcelSheetWriter extends AbstractExcelSheetWriter {
	private MetricStatusResolver<Double> maintainabilityIndexStatusResolver;

	public CoreIndexExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		this.maintainabilityIndexStatusResolver = maintainabilityIndexStatusResolver;
	}

	protected void createMaintainabilityIndexCell(Map<Status, CellStyle> statusCellStyles, Double maintainabilityIndex,
			Row row, int columnNumber) {
		Cell maintainabilityIndexCell = row.createCell(columnNumber);
		maintainabilityIndexCell.setCellValue(maintainabilityIndex);
		maintainabilityIndexCell
				.setCellStyle(statusCellStyles.get(maintainabilityIndexStatusResolver.getStatus(maintainabilityIndex)));
	}

	protected void writeHalsteadMetrics(Row row, HalsteadComplexityMetrics halsteadMetrics, int startColumn) {
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getn1());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getn2());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getN1());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getN2());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getProgramLength());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getProgramVocabulary());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getEstimatedLength());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getPurityRatio());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getVolume());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getDifficulty());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getProgramEffort());
		row.createCell(startColumn++).setCellValue(halsteadMetrics.getProgrammingTime());
	}
}
