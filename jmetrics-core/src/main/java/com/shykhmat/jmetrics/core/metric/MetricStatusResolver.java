package com.shykhmat.jmetrics.core.metric;

/**
 * Common class for status resolvers for concrete metrics.
 * @param <T> - metric value type
 */
public interface MetricStatusResolver<T> {
    Status getStatus(T metric);
}
