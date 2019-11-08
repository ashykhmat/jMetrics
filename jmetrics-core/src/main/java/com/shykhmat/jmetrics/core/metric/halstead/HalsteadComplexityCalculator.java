package com.shykhmat.jmetrics.core.metric.halstead;

import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;
import com.shykhmat.jmetrics.core.report.HalsteadComplexityMetrics;

/**
 * Implementation of {@code MetricCalculator} to calculate Halstead Complexity
 * metric. More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Halstead_complexity_measures
 */
public class HalsteadComplexityCalculator extends MetricCalculator<HalsteadComplexityMetrics> {

	public HalsteadComplexityMetrics calculateMetric(Map<String, Integer> operators, Map<String, Integer> operands) {
		double n1 = operators.size();
		double n2 = operands.size();
		double N1 = operators.values().stream().collect(Collectors.summingInt(Integer::intValue));
		double N2 = operands.values().stream().collect(Collectors.summingInt(Integer::intValue));
		double programLength = N1 + N2;
		if (programLength == 0) {
			programLength = 1;
		}
		double programVocabulary = n1 + n2;
		double n1Log = 1.;
		if (n1 != 0) {
			n1Log = Math.log(n1) / Math.log(2);
		}
		double n2Log = 1.;
		if (n2 != 0) {
			n2Log = Math.log(n2) / Math.log(2);
		}
		double estimatedLength = n1 * n1Log + n2 * n2Log;
		double purityRatio = estimatedLength / programLength;
		double volume = programLength * (Math.log(programVocabulary) / Math.log(2));
		double difficulty = (n1 / 2) * (N2 / n2);
		double programEffort = volume * difficulty;
		double programmingTime = programEffort / 18;
		return new HalsteadComplexityMetrics(programLength, programVocabulary, estimatedLength, purityRatio, volume,
				difficulty, programEffort, programmingTime, n1, n2, N1, N2, operators, operands);
	}

	@Override
	protected String getMetricName() {
		return "Halstead Complexity";
	}

	@Override
	protected HalsteadComplexityMetrics getCalculatedMetric(ASTNode astNode) {
		HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
		astNode.accept(halsteadVisitor);
		return calculateMetric(halsteadVisitor.getOperators(), halsteadVisitor.getOperands());
	}

}
