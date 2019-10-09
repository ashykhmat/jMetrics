package com.shykhmat.jmetrics.core.metric.halstead;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;

/**
 * Implementation of {@code MetricCalculator} to calculate Halstead Volume
 * metric. More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Halstead_complexity_measures
 */
public class HalsteadVolumeCalculator extends MetricCalculator<Double> {

	@Override
	protected String getMetricName() {
		return "Halstead Volume";
	}

	@Override
	protected Double getCalculatedMetric(ASTNode astNode) {
		return new HalsteadComplexityCalculator().calculateMetric(astNode).getVolume();
	}

}
