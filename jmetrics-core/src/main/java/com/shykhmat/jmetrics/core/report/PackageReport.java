package com.shykhmat.jmetrics.core.report;

/**
 * Class that contains report information for java package.
 */
public class PackageReport extends ExtendedCodePartReport implements Comparable<PackageReport> {

	/**
	 * The ratio of the number of abstract classes (and interfaces) in the analyzed
	 * package to the total number of classes in the analyzed package. The range for
	 * this metric is 0 to 1, with A=0 indicating a completely concrete package and
	 * A=1 indicating a completely abstract package
	 */
	private Double abstractness = 0.;
	/**
	 * The perpendicular distance of a package from the idealized line A + I = 1. D
	 * is calculated as D = | A + I - 1 |. This metric is an indicator of the
	 * package's balance between abstractness and stability. A package squarely on
	 * the main sequence is optimally balanced with respect to its abstractness and
	 * stability. Ideal packages are either completely abstract and stable (I=0,
	 * A=1) or completely concrete and unstable (I=1, A=0). The range for this
	 * metric is 0 to 1, with D=0 indicating a package that is coincident with the
	 * main sequence and D=1 indicating a package that is as far from the main
	 * sequence as possible
	 */
	private Double distance = 0.;
	private Integer nonAbstractClassesNumber = 0;
	private Integer abstractClassesInterfacesNumber = 0;

	public PackageReport() {
		super(null);
	}

	public PackageReport(String name) {
		super(name);
	}

	public Double getAbstractness() {
		return abstractness;
	}

	public void setAbstractness(Double abstractness) {
		this.abstractness = abstractness;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getNonAbstractClassesNumber() {
		return nonAbstractClassesNumber;
	}

	public void setNonAbstractClassesNumber(Integer nonAbstractClassesNumber) {
		this.nonAbstractClassesNumber = nonAbstractClassesNumber;
	}

	public void countNonAbstractClass() {
		nonAbstractClassesNumber++;
	}

	public Integer getAbstractClassesInterfacesNumber() {
		return abstractClassesInterfacesNumber;
	}

	public void setAbstractClassesInterfacesNumber(Integer abstractClassesInterfacesNumber) {
		this.abstractClassesInterfacesNumber = abstractClassesInterfacesNumber;
	}

	public void countAbstractClasseInterface() {
		abstractClassesInterfacesNumber++;
	}

	@Override
	public int compareTo(PackageReport codeToCompare) {
		int result = distance.compareTo(codeToCompare.getDistance());
		if (result == 0) {
			return getName().compareTo(codeToCompare.getName());
		}
		return result;
	}

}
