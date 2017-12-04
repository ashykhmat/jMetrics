
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
    private double programLength, programVocabulary, estimatedLength, purityRatio, volume, difficulty, programEffort, programmingTime;
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
        this.N1 = operators.size();
        this.N2 = operands.size();
        for (Integer operatorsCount : operators.values()) {
            this.n1 += operatorsCount;
        }
        for (Integer operandsCount : operands.values()) {
            this.n2 += operandsCount;
        }
        this.programLength = this.N1 + this.N2;
        this.programVocabulary = this.n1 + this.n2;
        this.estimatedLength = (((this.n1) * (Math.log(this.n1) / Math.log(2))) + ((this.n2) * (Math.log(this.n2) / Math.log(2))));
        this.purityRatio = this.estimatedLength / this.programLength;
        this.volume = ((this.programLength) * (Math.log(this.programLength) / Math.log(2)));
        this.difficulty = (this.n1 / 2) * (this.N2 / this.n2);
        this.programEffort = this.volume * this.difficulty;
        this.programmingTime = this.programmingTime / 18;
        return this;
    }

}