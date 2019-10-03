package com.shykhmat.jmetrics.core.report;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class that contains report information for java project.
 */
public class ProjectReport extends CodePartReport implements Serializable {

	private static final long serialVersionUID = -5055487412966922425L;

	private Set<ClassReport> classes;

	public ProjectReport(String name) {
		super(name);
		classes = new TreeSet<>();
	}

	public Set<ClassReport> getClasses() {
		return classes;
	}

	public void setClasses(Set<ClassReport> classes) {
		this.classes = classes;
	}

	public void addClass(ClassReport classToAdd) {
		classes.add(classToAdd);
	}
}
