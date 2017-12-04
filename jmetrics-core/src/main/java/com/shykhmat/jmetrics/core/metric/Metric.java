
package com.shykhmat.jmetrics.core.metric;

import com.shykhmat.jmetrics.core.metric.CodePart;

/**
 * Common interface for all metrics supported by the application.
 * 
 * @param <T> - code part type to analyze
 * @param <V> - metric result type
 */
public interface Metric<T extends CodePart, V> {
    V calculateMetric(T codeToAnalyze) throws MetricException;
}
