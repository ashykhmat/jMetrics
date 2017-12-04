
package com.shykhmat.jmetrics.core.metric;

import com.shykhmat.jmetrics.core.metric.cyclomatic.CyclomaticComplexityMetric;
import com.shykhmat.jmetrics.core.metric.halstead.HalsteadVolumeMetric;
import com.shykhmat.jmetrics.core.metric.loc.LinesOfCodeMetric;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexMetric;
import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Composite metric that wraps all metrics supported by application.
 */
public class CompositeMetric implements Metric<CodePart, Metrics> {
    private HalsteadVolumeMetric halsteadVolumeMetric;
    private CyclomaticComplexityMetric cyclomaticComplexityMetric;
    private LinesOfCodeMetric linesOfCodeMetric;
    private MaintainabilityIndexMetric maintainabilityIndexMetric;

    public CompositeMetric(HalsteadVolumeMetric halsteadVolumeMetric,
            CyclomaticComplexityMetric cyclomaticComplexityMetric, LinesOfCodeMetric linesOfCodeMetric,
            MaintainabilityIndexMetric maintainabilityIndexMetric) {
        this.halsteadVolumeMetric = halsteadVolumeMetric;
        this.cyclomaticComplexityMetric = cyclomaticComplexityMetric;
        this.linesOfCodeMetric = linesOfCodeMetric;
        this.maintainabilityIndexMetric = maintainabilityIndexMetric;
    }

    @Override
    public Metrics calculateMetric(CodePart codePart) throws MetricException {
        Metrics metrics = new Metrics();
        codePart.setMetrics(metrics);
        metrics.setHalsteadVolume(halsteadVolumeMetric.calculateMetric(codePart));
        metrics.setCyclomaticComplexity(cyclomaticComplexityMetric.calculateMetric(codePart));
        metrics.setLinesOfCode(linesOfCodeMetric.calculateMetric(codePart));
        metrics.setMaintainabilityIndex(maintainabilityIndexMetric.calculateMetric(codePart));
        return metrics;
    }

}
