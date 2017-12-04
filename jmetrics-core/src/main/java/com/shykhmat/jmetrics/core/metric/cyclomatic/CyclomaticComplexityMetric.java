
package com.shykhmat.jmetrics.core.metric.cyclomatic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.metric.VisitorMetric;
import com.shykhmat.jmetrics.core.metric.cyclomatic.internal.McCabeVisitor;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Implementation of {@code Metric} to calculate Cyclomatic Complexity metric
 * using McCabe formula. More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Cyclomatic_complexity
 */
public class CyclomaticComplexityMetric extends VisitorMetric<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclomaticComplexityMetric.class);
    private static final String METRIC_NAME = "Cyclomatic Complexity";

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
        return new McCabeVisitor();
    }

    @Override
    protected Integer getMetricValue(Metrics metrics) {
        return metrics.getCyclomaticComplexity();
    }

}
