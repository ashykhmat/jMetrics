package com.shykhmat.jmetrics.core.metric.loc;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;

/**
 * Implementation of {@link MetricCalculator} to calculate Lines of Code metric.
 */
public class LinesOfCodeCalculator extends MetricCalculator<Integer> {

	@Override
	protected String getMetricName() {
		return "Lines of Code";
	}

	@Override
	protected Integer getCalculatedMetric(ASTNode astNode) {
		String codeString = astNode.toString();
		return calculateLOC(codeString);
	}

	private int calculateLOC(String codeString) {
		String[] tokenLines = codeString.replaceAll("/\\*\\*(?s:(?!\\*/).)*\\*/", "").split("\n", -1);
		int loc = 0;
		for (String token : tokenLines) {
			if (!token.replaceAll(" ", "").replaceAll("\t", "").isEmpty()) {
				loc++;
			}
		}
		return loc;
	}

}
