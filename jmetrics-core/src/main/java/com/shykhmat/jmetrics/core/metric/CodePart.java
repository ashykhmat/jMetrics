package com.shykhmat.jmetrics.core.metric;

import com.github.javaparser.ast.Node;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Wrapper class for all parts of code, e.g. for Class, Method, etc.
 */
public class CodePart {
	private Node codeToAnalyze;
	private CodePartType codeType;
	private Metrics metrics;

	public CodePart(Node codeToAnalyze, CodePartType codeType) {
		this.codeToAnalyze = codeToAnalyze;
		this.codeType = codeType;
	}

	public Node getCodeToAnalyze() {
		return codeToAnalyze;
	}

	public void setCodeToAnalyze(Node codeToAnalyze) {
		this.codeToAnalyze = codeToAnalyze;
	}

	public CodePartType getCodeType() {
		return codeType;
	}

	public void setCodeType(CodePartType codeType) {
		this.codeType = codeType;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}

	/**
	 * All supported code parts.
	 */
	public static enum CodePartType {
		NONE, CLASS, INTERFACE, ENUM, METHOD, ANNOTATION, CONSTRUCTOR;
	}
}
