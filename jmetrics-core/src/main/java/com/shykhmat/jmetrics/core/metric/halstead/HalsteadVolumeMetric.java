
package com.shykhmat.jmetrics.core.metric.halstead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.metric.Metric;
import com.shykhmat.jmetrics.core.metric.MetricException;
import com.shykhmat.jmetrics.core.metric.halstead.internal.HalsteadMetricsCalculator;

/**
 * Implementation of {@code Metric} to calculate Halstead Volume metric. More
 * details about formula can be found here:
 * https://en.wikipedia.org/wiki/Halstead_complexity_measures
 */
public class HalsteadVolumeMetric implements Metric<CodePart, Double> {
	private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadVolumeMetric.class);

	@Override
	public Double calculateMetric(CodePart codePart) throws MetricException {
		try {
			long startTime = System.nanoTime();
			LOGGER.debug("Calculating Halstead metric");
			Double result = new HalsteadMetricsCalculator().calculate(codePart).getVolume();
			long totalTime = System.nanoTime() - startTime;
			LOGGER.debug("Calculation done. Total execution time: {}. Result: {}", totalTime, result);
			return result;
		} catch (Exception e) {
			throw new MetricException(e);
		}
	}

}
