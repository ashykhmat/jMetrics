
package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.util.Map;

import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.metric.Metric;
import com.shykhmat.jmetrics.core.metric.MetricException;

/**
 * Implementation of {@code Metric} to calculate Halstead Complexity metrics.
 * More details about formula can be found here:
 * https://en.wikipedia.org/wiki/Halstead_complexity_measures
 */
public class HalsteadComplexityMetrics implements Metric<CodePart, HalsteadComplexityMetrics> {
	private double programLength, programVocabulary, estimatedLength, purityRatio, volume, difficulty, programEffort,
			programmingTime;
	private double n1, n2, N1, N2;
	private Map<String, Integer> operators;
	private Map<String, Integer> operands;

	public HalsteadComplexityMetrics(Map<String, Integer> operators, Map<String, Integer> operands) {
		this.operators = operators;
		this.operands = operands;
	}

	public double getProgramLength() {
		return programLength;
	}

	public double getProgramVocabulary() {
		return programVocabulary;
	}

	public double getEstimatedLength() {
		return estimatedLength;
	}

	public double getPurityRatio() {
		return purityRatio;
	}

	public double getVolume() {
		return volume;
	}

	public double getDifficulty() {
		return difficulty;
	}

	public double getProgramEffort() {
		return programEffort;
	}

	public double getProgrammingTime() {
		return programmingTime;
	}

	@Override
	public HalsteadComplexityMetrics calculateMetric(CodePart codeToAnalyze) throws MetricException {
		N1 = operators.size();
		N2 = operands.size();
		for (Integer operatorsCount : operators.values()) {
			n1 += operatorsCount;
		}
		for (Integer operandsCount : operands.values()) {
			n2 += operandsCount;
		}
		programLength = N1 + N2;
		if (programLength == 0) {
			programLength = 1;
		}
		programVocabulary = n1 + n2;
		estimatedLength = (((n1) * (Math.log(n1) / Math.log(2))) + ((n2) * (Math.log(n2) / Math.log(2))));
		purityRatio = estimatedLength / programLength;
		volume = ((programLength) * (Math.log(programLength) / Math.log(2)));
		difficulty = (n1 / 2) * (N2 / n2);
		programEffort = volume * difficulty;
		programmingTime = programEffort / 18;
		return this;
	}

}