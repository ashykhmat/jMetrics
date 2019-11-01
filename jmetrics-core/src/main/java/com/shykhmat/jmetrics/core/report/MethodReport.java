package com.shykhmat.jmetrics.core.report;

/**
 * Class that contains report information for java method.
 */
public class MethodReport extends CodePartReport implements Comparable<MethodReport> {

	public MethodReport() {
		this(null);
	}

	public MethodReport(String name) {
		super(name);
	}

	@Override
	public int compareTo(MethodReport codeToCompare) {
		int result = getMetrics().getMaintainabilityIndex()
				.compareTo(codeToCompare.getMetrics().getMaintainabilityIndex());
		if (result == 0) {
			return getName().compareTo(codeToCompare.getName());
		}
		return result;
	}
}
