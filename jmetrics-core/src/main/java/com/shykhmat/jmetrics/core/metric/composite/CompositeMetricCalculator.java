
package com.shykhmat.jmetrics.core.metric.composite;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;
import com.shykhmat.jmetrics.core.metric.cyclomatic.CyclomaticComplexityCalculator;
import com.shykhmat.jmetrics.core.metric.halstead.HalsteadVolumeCalculator;
import com.shykhmat.jmetrics.core.metric.loc.LinesOfCodeCalculator;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexCalculator;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Composite metric that wraps all metrics supported by application.
 */
public class CompositeMetricCalculator extends MetricCalculator<Metrics> {

	@Override
	protected String getMetricName() {
		return "Composite Metric";
	}

	@Override
	protected Metrics getCalculatedMetric(ASTNode astNode) {
		Metrics metrics = new Metrics();
		metrics.setHalsteadVolume(new HalsteadVolumeCalculator().calculateMetric(astNode));
		metrics.setCyclomaticComplexity(new CyclomaticComplexityCalculator().calculateMetric(astNode));
		metrics.setLinesOfCode(new LinesOfCodeCalculator().calculateMetric(astNode));
		metrics.setMaintainabilityIndex(new MaintainabilityIndexCalculator().calculateMetric(metrics));
		return metrics;
	}

}
