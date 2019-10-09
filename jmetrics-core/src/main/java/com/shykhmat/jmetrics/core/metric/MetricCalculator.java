package com.shykhmat.jmetrics.core.metric;

import org.eclipse.jdt.core.dom.ASTNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common class for metrics calculators.
 * 
 * @param <T> - metric type
 */
public abstract class MetricCalculator<T> {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public T calculateMetric(ASTNode astNode) {
		long startTime = System.nanoTime();
		LOGGER.debug("Calculating {} metric", getMetricName());
		T result = getCalculatedMetric(astNode);
		long totalTime = System.nanoTime() - startTime;
		LOGGER.debug("Calculation done. Total execution time: {}. Result: {}", totalTime, result);
		return result;
	}

	protected abstract String getMetricName();

	protected abstract T getCalculatedMetric(ASTNode astNode);

}
