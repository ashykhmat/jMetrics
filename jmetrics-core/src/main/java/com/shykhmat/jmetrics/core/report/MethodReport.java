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
	public int compareTo(MethodReport methodToCompare) {
		if (getMetrics().getMaintainabilityIndex() == methodToCompare.getMetrics().getMaintainabilityIndex()) {
			return getName().compareTo(methodToCompare.getName());
		}
		if (getMetrics().getMaintainabilityIndex() > methodToCompare.getMetrics().getMaintainabilityIndex()) {
			return 1;
		}
		return -1;
	}

}
