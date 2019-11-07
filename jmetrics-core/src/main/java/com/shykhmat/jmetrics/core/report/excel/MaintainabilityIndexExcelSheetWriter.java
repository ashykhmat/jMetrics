package com.shykhmat.jmetrics.core.report.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;

/**
 * Common implementation of {@link AbstractExcelSheetWriter} for Excel writers,
 * that work with maintainability index metric.
 */
public abstract class MaintainabilityIndexExcelSheetWriter extends AbstractExcelSheetWriter {
	private MetricStatusResolver<Double> maintainabilityIndexStatusResolver;

	public MaintainabilityIndexExcelSheetWriter(MetricStatusResolver<Double> maintainabilityIndexStatusResolver) {
		this.maintainabilityIndexStatusResolver = maintainabilityIndexStatusResolver;
	}

	protected void createMaintainabilityIndexCell(Map<Status, CellStyle> statusCellStyles, Double maintainabilityIndex,
			Row row) {
		Cell maintainabilityIndexCell = row.createCell(1);
		maintainabilityIndexCell.setCellValue(maintainabilityIndex);
		maintainabilityIndexCell
				.setCellStyle(statusCellStyles.get(maintainabilityIndexStatusResolver.getStatus(maintainabilityIndex)));
	}
}
