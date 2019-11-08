package com.shykhmat.jmetrics.core.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import com.shykhmat.jmetrics.core.metric.cyclomatic.CyclomaticComplexityCalculator;
import com.shykhmat.jmetrics.core.metric.halstead.HalsteadComplexityCalculator;
import com.shykhmat.jmetrics.core.metric.loc.LinesOfCodeCalculator;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexCalculator;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Common class for all visitors that process source code files.
 */
public abstract class AbstractVisitor extends ASTVisitor {

	protected Metrics calculateCoreMetrics(ASTNode astNode) {
		Metrics metrics = new Metrics();
		metrics.setHalsteadMetrics(new HalsteadComplexityCalculator().calculateMetric(astNode));
		metrics.setCyclomaticComplexity(new CyclomaticComplexityCalculator().calculateMetric(astNode));
		metrics.setLinesOfCode(new LinesOfCodeCalculator().calculateMetric(astNode));
		metrics.setMaintainabilityIndex(new MaintainabilityIndexCalculator().calculateMetric(metrics));
		return metrics;
	}
}
