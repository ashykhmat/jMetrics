package com.shykhmat.jmetrics.core.metric.loc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.metric.VisitorMetric;
import com.shykhmat.jmetrics.core.metric.loc.internal.LinesOfCodeVisitor;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Implementation of {@code Metric} to calculate Lines of Code metric.
 */
public class LinesOfCodeMetric extends VisitorMetric<Integer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(LinesOfCodeMetric.class);
	private static final String METRIC_NAME = "Lines of Code";

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected String getMetricName() {
		return METRIC_NAME;
	}

	@Override
	protected VoidVisitorAdapter<Metrics> getVisitor() {
		return new LinesOfCodeVisitor();
	}

	@Override
	protected Integer getMetricValue(Metrics metrics) {
		return metrics.getLinesOfCode();
	}

}
