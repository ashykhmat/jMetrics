package com.shykhmat.jmetrics.sonar.plugin.metric;

import com.shykhmat.jmetrics.core.report.Metrics;

public class ComparedMetrics extends Metrics {
	private Status halsteadVolumeStatus = Status.NEW;
	private Status cyclomaticComplexityStatus = Status.NEW;
	private Status linesOfCodeStatus = Status.NEW;
	private Status maintainabilityIndexStatus = Status.NEW;

	public Status getHalsteadVolumeStatus() {
		return halsteadVolumeStatus;
	}

	public void setHalsteadVolumeStatus(Status halsteadVolumeStatus) {
		this.halsteadVolumeStatus = halsteadVolumeStatus;
	}

	public Status getCyclomaticComplexityStatus() {
		return cyclomaticComplexityStatus;
	}

	public void setCyclomaticComplexityStatus(Status cyclomaticComplexityStatus) {
		this.cyclomaticComplexityStatus = cyclomaticComplexityStatus;
	}

	public Status getLinesOfCodeStatus() {
		return linesOfCodeStatus;
	}

	public void setLinesOfCodeStatus(Status linesOfCodeStatus) {
		this.linesOfCodeStatus = linesOfCodeStatus;
	}

	public Status getMaintainabilityIndexStatus() {
		return maintainabilityIndexStatus;
	}

	public void setMaintainabilityIndexStatus(Status maintainabilityIndexStatus) {
		this.maintainabilityIndexStatus = maintainabilityIndexStatus;
	}

	public static enum Status {
		LESS(-1), SAME(0), MORE(1), NEW(2);

		private int value;

		private Status(int value) {
			this.value = value;
		}

		public static Status fromValue(int value) {
			for (Status status : Status.values()) {
				if (status.value == value) {
					return status;
				}
			}
			throw new IllegalArgumentException("Cannot get Status for value " + value);
		}
	}
}
