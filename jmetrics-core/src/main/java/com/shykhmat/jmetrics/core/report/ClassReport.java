package com.shykhmat.jmetrics.core.report;

import java.util.Set;
import java.util.TreeSet;

/**
 * Class that contains report information for java class.
 */
public class ClassReport extends ExtendedCodePartReport {
	private Set<MethodReport> methods;
	private boolean isAbstractOrInterface;

	public ClassReport() {
		this(null);
	}

	public ClassReport(String name) {
		super(name);
		methods = new TreeSet<>();
	}

	public Set<MethodReport> getMethods() {
		return methods;
	}

	public void setMethods(Set<MethodReport> methods) {
		this.methods = methods;
	}

	public void addMethod(MethodReport method) {
		methods.add(method);
	}

	public boolean isAbstractOrInterface() {
		return isAbstractOrInterface;
	}

	public void setIsAbstractOrInterface(boolean isAbstractOrInterface) {
		this.isAbstractOrInterface = isAbstractOrInterface;
	}

}
