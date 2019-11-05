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
	private Set<PackageReport> packages;
	private Set<String> classesCircularDependencies;
	private Set<String> packagesCircularDependencies;

	public ProjectReport() {
		this(null);
	}

	public ProjectReport(String name) {
		super(name);
		classes = new TreeSet<>();
		packages = new TreeSet<>();
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

	public Set<PackageReport> getPackages() {
		return packages;
	}

	public void setPackages(Set<PackageReport> packages) {
		this.packages = packages;
	}

	public void addPackage(PackageReport packageToAdd) {
		packages.add(packageToAdd);
	}

	public Set<String> getClassesCircularDependencies() {
		return classesCircularDependencies;
	}

	public void setClassesCircularDependencies(Set<String> classesCircularDependencies) {
		this.classesCircularDependencies = classesCircularDependencies;
	}

	public Integer getClassesCircularDependenciesSize() {
		return classesCircularDependencies.size();
	}

	public Set<String> getPackagesCircularDependencies() {
		return packagesCircularDependencies;
	}

	public void setPackagesCircularDependencies(Set<String> packagesCircularDependencies) {
		this.packagesCircularDependencies = packagesCircularDependencies;
	}

	public Integer getPackagesCircularDependenciesSize() {
		return packagesCircularDependencies.size();
	}

}
