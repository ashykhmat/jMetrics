
package com.shykhmat.jmetrics.sonar.plugin.metric;

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import com.google.common.collect.ImmutableList;

/**
 * Collection of metrics presented by plug-in.
 */
public final class JMetrics implements Metrics {
	private static final String DOMAIN = CoreMetrics.DOMAIN_GENERAL;
	@SuppressWarnings("rawtypes")
	private static final List<Metric> METRICS;

	public static final Metric<String> JMETRICS;
	public static final Metric<String> JMETRICS_HTML;
	public static final Metric<String> JMETRICS_EXCEL;

	static {
		@SuppressWarnings("rawtypes")
		final ImmutableList.Builder<Metric> builder = ImmutableList.builder();
		JMETRICS = new Metric.Builder("jmetrics", "jMetrics", Metric.ValueType.DATA).setDomain(DOMAIN)
				.setDirection(Metric.DIRECTION_WORST).create();
		builder.add(JMETRICS);
		JMETRICS_HTML = new Metric.Builder("jmetrics_html", "jMetrics HTML", Metric.ValueType.DATA).setDomain(DOMAIN)
				.setDirection(Metric.DIRECTION_WORST).create();
		builder.add(JMETRICS_HTML);
		JMETRICS_EXCEL = new Metric.Builder("jmetrics_excel", "jMetrics Excel", Metric.ValueType.DATA).setDomain(DOMAIN)
				.setDirection(Metric.DIRECTION_WORST).create();
		builder.add(JMETRICS_EXCEL);
		METRICS = builder.build();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Metric> getMetrics() {
		return METRICS;
	}
}