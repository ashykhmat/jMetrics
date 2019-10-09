package com.shykhmat.jmetrics.core.metric.halstead;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.MetricCalculator;

/**
 * Implementation of {@code MetricCalculator} to calculate Halstead Complexity
 * metric. More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Halstead_complexity_measures
 */
class HalsteadComplexityCalculator extends MetricCalculator<HalsteadComplexityMetrics> {

	private HalsteadComplexityMetrics calculateMetric(Map<String, Integer> operators, Map<String, Integer> operands) {
		double N1 = operators.size();
		double N2 = operands.size();
		double n1 = 0.;
		double n2 = 0.;

		for (Integer operatorsCount : operators.values()) {
			n1 += operatorsCount;
		}
		for (Integer operandsCount : operands.values()) {
			n2 += operandsCount;
		}
		double programLength = N1 + N2;
		if (programLength == 0) {
			programLength = 1;
		}
		double programVocabulary = n1 + n2;
		double estimatedLength = (((n1) * (Math.log(n1) / Math.log(2))) + ((n2) * (Math.log(n2) / Math.log(2))));
		double purityRatio = estimatedLength / programLength;
		double volume = ((programLength) * (Math.log(programLength) / Math.log(2)));
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
