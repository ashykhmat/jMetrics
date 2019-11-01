package com.shykhmat.jmetrics.core.metric.coupling;

/**
 * Class to calculate software coupling metrics. More details here:
 * https://en.wikipedia.org/wiki/Software_package_metrics
 */
public class CouplingMetricsCalculator {

	/**
	 * The ratio of efferent coupling (Ce) to total coupling (Ce + Ca) such that I =
	 * Ce / (Ce + Ca). This metric is an indicator of the package's resilience to
	 * change. The range for this metric is 0 to 1, with I=0 indicating a completely
	 * stable package and I=1 indicating a completely unstable package
	 */
	public Double calculateInstability(int efferentCoupling, int afferentCoupling) {
		if (efferentCoupling + afferentCoupling == 0) {
			return 1.;
		}
		return (efferentCoupling * 1.) / (efferentCoupling + afferentCoupling);
	}

	/**
	 * The ratio of the number of abstract classes (and interfaces) in the analyzed
	 * package to the total number of classes in the analyzed package. The range for
	 * this metric is 0 to 1, with A=0 indicating a completely concrete package and
	 * A=1 indicating a completely abstract package.
	 */
	public Double calculateAbstractness(int numberOfNonAbstractClasses, int numberOfAbstractClasses) {
		int totalNumberOfClasses = numberOfAbstractClasses + numberOfNonAbstractClasses;
		if (totalNumberOfClasses == 0) {
			return 1.;
		}
		return (numberOfAbstractClasses * 1.) / totalNumberOfClasses;
	}

	/**
	 * The perpendicular distance of a package from the idealized line A + I = 1. D
	 * is calculated as D = | A + I - 1 |. This metric is an indicator of the
	 * package's balance between abstractness and stability. A package squarely on
	 * the main sequence is optimally balanced with respect to its abstractness and
	 * stability. Ideal packages are either completely abstract and stable (I=0,
	 * A=1) or completely concrete and unstable (I=1, A=0). The range for this
	 * metric is 0 to 1, with D=0 indicating a package that is coincident with the
	 * main sequence and D=1 indicating a package that is as far from the main
	 * sequence as possible.
	 */
	public Double calculateDistance(double abstractness, double instability) {
		return Math.abs(abstractness + instability - 1);
	}
}
