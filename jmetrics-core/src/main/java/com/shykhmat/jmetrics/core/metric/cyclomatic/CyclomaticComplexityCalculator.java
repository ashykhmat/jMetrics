package com.shykhmat.jmetrics.core.metric.cyclomatic;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;

/**
 * Implementation of {@code MetricCalculator} to calculate Cyclomatic Complexity
 * metric using McCabe formula. More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Cyclomatic_complexity
 */
public class CyclomaticComplexityCalculator extends MetricCalculator<Integer> {

	@Override
	protected String getMetricName() {
		return "Cyclomatic Complexity";
	}

	@Override
	protected Integer getCalculatedMetric(ASTNode astNode) {
		McCabeVisitor mcCabeVisitor = new McCabeVisitor();
		astNode.accept(mcCabeVisitor);
		return mcCabeVisitor.getCyclomaticComplexity();
	}

}
