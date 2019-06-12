package com.shykhmat.jmetrics.core.metric.maintainability;

import com.shykhmat.jmetrics.core.metric.MetricStatusResolver;
import com.shykhmat.jmetrics.core.metric.Status;

/**
 * Implementation of {@link MetricStatusResolver} for Maintainability Index metric. Microsoft statuses are used. More
 * details about statuses can be found here:
 * https://blogs.msdn.microsoft.com/zainnab/2011/05/26/code-metrics-maintainability-index
 */
public class MaintainabilityIndexStatusResolver implements MetricStatusResolver<Double> {

    @Override
    public Status getStatus(Double maintainabilityIndex) {
        if (maintainabilityIndex < 10) {
            return Status.ERROR;
        } else if (maintainabilityIndex >= 10 && maintainabilityIndex < 20) {
            return Status.WARNING;
        }
        return Status.OK;
    }

}
