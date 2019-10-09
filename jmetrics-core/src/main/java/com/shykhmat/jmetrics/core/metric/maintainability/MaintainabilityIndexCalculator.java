package com.shykhmat.jmetrics.core.metric.maintainability;

import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Class to calculate Maintainability Index metric using Microsoft formula. More
 * details about formula can be found here:
 * https://blogs.msdn.microsoft.com/zainnab/2011/05/26/code-metrics-maintainability-index
 */
public class MaintainabilityIndexCalculator {

	public Double calculateMetric(Metrics metrics) {
		return (171. - 5.2 * Math.log(metrics.getHalsteadVolume()) - 0.23 * metrics.getCyclomaticComplexity()
				- 16.2 * Math.log(metrics.getLinesOfCode())) * 100. / 171.;
	}

}
