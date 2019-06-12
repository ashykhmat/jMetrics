
package com.shykhmat.jmetrics.core.metric.maintainability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.Metric;
import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Implementation of {@code Metric} to calculate Maintainability Index metric using Microsoft formula. More details
 * about formula can be found here:
 * https://blogs.msdn.microsoft.com/zainnab/2011/05/26/code-metrics-maintainability-index
 */
public class MaintainabilityIndexMetric implements Metric<CodePart, Double> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaintainabilityIndexMetric.class);

    @Override
    public Double calculateMetric(CodePart codePart) {
        long startTime = System.nanoTime();
        LOGGER.debug("Calculating Maintainability Index metric");
        Metrics metrics = codePart.getMetrics();
        Double result = (171. - 5.2 * Math.log(metrics.getHalsteadVolume()) - 0.23 * metrics.getCyclomaticComplexity()
                - 16.2 * Math.log(metrics.getLinesOfCode())) * 100. / 171.;
        long totalTime = System.nanoTime() - startTime;
        LOGGER.debug("Calculation done. Total execution time: {}. Result: {}", totalTime, result);
        return result;
    }

}
