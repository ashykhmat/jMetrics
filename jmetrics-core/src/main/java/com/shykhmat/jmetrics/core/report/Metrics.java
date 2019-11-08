
package com.shykhmat.jmetrics.core.report;

/**
 * Class that contains all metrics calculated for java class/method.
 */
public class Metrics {
	private HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics();
	private Integer cyclomaticComplexity = 0;
	private Integer linesOfCode = 0;
	private Double maintainabilityIndex = 0.;

	public HalsteadComplexityMetrics getHalsteadMetrics() {
		return halsteadMetrics;
	}

	public void setHalsteadMetrics(HalsteadComplexityMetrics halsteadMetrics) {
		this.halsteadMetrics = halsteadMetrics;
	}

	public Integer getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public void setCyclomaticComplexity(Integer cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}

	public Integer getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(Integer linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public Double getMaintainabilityIndex() {
		return maintainabilityIndex;
	}

	public void setMaintainabilityIndex(Double maintainabilityIndex) {
		this.maintainabilityIndex = maintainabilityIndex;
	}

}
