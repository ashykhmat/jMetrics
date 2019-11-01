package com.shykhmat.jmetrics.core.report;

import java.util.Set;
import java.util.TreeSet;

/**
 * Base class for all parts of code with additional metrics, like coupling, etc.
 */
public abstract class ExtendedCodePartReport extends CodePartReport {
	private Set<String> afferentCouplingAll = new TreeSet<>();
	private Set<String> afferentCouplingUsed = new TreeSet<>();
	private Set<String> efferentCouplingAll = new TreeSet<>();
	private Set<String> efferentCouplingUsed = new TreeSet<>();
	private Double instability = 0.;

	public ExtendedCodePartReport(String name) {
		super(name);
	}

	public Set<String> getAfferentCouplingAll() {
		return afferentCouplingAll;
	}

	public void setAfferentCouplingAll(Set<String> afferentCouplingAll) {
		this.afferentCouplingAll = afferentCouplingAll;
	}

	public void addAfferentCouplingAll(String afferentCoupling) {
		afferentCouplingAll.add(afferentCoupling);
	}

	public Set<String> getAfferentCouplingUsed() {
		return afferentCouplingUsed;
	}

	public void setAfferentCouplingUsed(Set<String> afferentCouplingUsed) {
		this.afferentCouplingUsed = afferentCouplingUsed;
	}

	public void addAfferentCouplingUsed(String afferentCoupling) {
		afferentCouplingAll.add(afferentCoupling);
		afferentCouplingUsed.add(afferentCoupling);
	}

	public Set<String> getEfferentCouplingAll() {
		return efferentCouplingAll;
	}

	public void setEfferentCouplingAll(Set<String> efferentCouplingAll) {
		this.efferentCouplingAll = efferentCouplingAll;
	}

	public void addEfferentCouplingAll(String efferentCoupling) {
		efferentCouplingAll.add(efferentCoupling);
	}

	public Set<String> getEfferentCouplingUsed() {
		return efferentCouplingUsed;
	}

	public void setEfferentCouplingUsed(Set<String> efferentCouplingUsed) {
		this.efferentCouplingUsed = efferentCouplingUsed;
	}

	public void addEfferentCouplingUsed(String efferentCoupling) {
		efferentCouplingAll.add(efferentCoupling);
		efferentCouplingUsed.add(efferentCoupling);
	}

	public void setInstability(Double instability) {
		this.instability = instability;
	}

	public Double getInstability() {
		return instability;
	}

}
