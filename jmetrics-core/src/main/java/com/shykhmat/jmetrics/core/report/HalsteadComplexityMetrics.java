
package com.shykhmat.jmetrics.core.report;

import java.util.HashMap;
import java.util.Map;

/**
 * Halstead Complexity metrics.
 */
public class HalsteadComplexityMetrics {
	private double programLength, programVocabulary, estimatedLength, purityRatio, volume, difficulty, programEffort,
			programmingTime;
	private double n1, n2, N1, N2;
	private Map<String, Integer> operators;
	private Map<String, Integer> operands;

	public HalsteadComplexityMetrics() {
		operators = new HashMap<>();
		operands = new HashMap<>();
	}

	public HalsteadComplexityMetrics(double programLength, double programVocabulary, double estimatedLength,
			double purityRatio, double volume, double difficulty, double programEffort, double programmingTime,
			double n1, double n2, double N1, double N2, Map<String, Integer> operators, Map<String, Integer> operands) {
		this.programLength = programLength;
		this.programVocabulary = programVocabulary;
		this.estimatedLength = estimatedLength;
		this.purityRatio = purityRatio;
		this.volume = volume;
		this.difficulty = difficulty;
		this.programEffort = programEffort;
		this.programmingTime = programmingTime;
		this.n1 = n1;
		this.n2 = n2;
		this.N1 = N1;
		this.N2 = N2;
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

	public double getn1() {
		return n1;
	}

	public double getn2() {
		return n2;
	}

	public double getN1() {
		return N1;
	}

	public double getN2() {
		return N2;
	}

	public Map<String, Integer> getOperators() {
		return operators;
	}

	public Map<String, Integer> getOperands() {
		return operands;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

}