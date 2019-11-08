package com.shykhmat.jmetrics.sonar.plugin.comparator;

import java.util.Optional;

import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.MethodReport;
import com.shykhmat.jmetrics.core.report.Metrics;
import com.shykhmat.jmetrics.core.report.ProjectReport;
import com.shykhmat.jmetrics.sonar.plugin.metric.ComparedMetrics;
import com.shykhmat.jmetrics.sonar.plugin.metric.ComparedMetrics.Status;

/**
 * Class that provides logic to compare current metrics with previous and
 * indicate whether they are improved or not.
 */
public class JMetricsComparator {
	public void fillWithComparisonStatus(ProjectReport currentReport, ProjectReport previousReport) {
		currentReport.getClasses().forEach(currentClass -> {
			Optional<ClassReport> previousClass = previousReport == null ? Optional.empty()
					: previousReport.getClasses().stream()
							.filter(clazz -> currentClass.getName().equals(clazz.getName())).findFirst();
			ComparedMetrics classComparedMetrics;
			if (previousClass.isPresent()) {
				classComparedMetrics = compareMetrics(currentClass.getMetrics(), previousClass.get().getMetrics());
				currentClass.getMethods().forEach(currentMethod -> {
					Optional<MethodReport> previousMethod = previousClass.get().getMethods().stream()
							.filter(method -> currentMethod.getName().equals(method.getName())).findFirst();
					ComparedMetrics methodComparedMetrics;
					if (previousMethod.isPresent()) {
						methodComparedMetrics = compareMetrics(currentMethod.getMetrics(),
								previousMethod.get().getMetrics());
					} else {
						methodComparedMetrics = createdComparedMetrics(currentMethod.getMetrics());
					}
					currentMethod.setMetrics(methodComparedMetrics);
				});
			} else {
				classComparedMetrics = createdComparedMetrics(currentClass.getMetrics());
				currentClass.getMethods().forEach(method -> {
					method.setMetrics(createdComparedMetrics(method.getMetrics()));
				});
			}
			currentClass.setMetrics(classComparedMetrics);
		});
	}

	private ComparedMetrics compareMetrics(Metrics currentMetrics, Metrics previousMetrics) {
		ComparedMetrics comparedMetrics = createdComparedMetrics(currentMetrics);
		comparedMetrics.setCyclomaticComplexityStatus(
				compareMetrics(currentMetrics.getCyclomaticComplexity(), previousMetrics.getCyclomaticComplexity()));
		comparedMetrics.setHalsteadVolumeStatus(compareMetrics(currentMetrics.getHalsteadMetrics().getVolume(),
				previousMetrics.getHalsteadMetrics().getVolume()));
		comparedMetrics.setLinesOfCodeStatus(
				compareMetrics(currentMetrics.getLinesOfCode(), previousMetrics.getLinesOfCode()));
		comparedMetrics.setMaintainabilityIndexStatus(
				compareMetrics(currentMetrics.getMaintainabilityIndex(), previousMetrics.getMaintainabilityIndex()));
		return comparedMetrics;
	}

	private ComparedMetrics createdComparedMetrics(Metrics metrics) {
		ComparedMetrics comparedMetrics = new ComparedMetrics();
		comparedMetrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity());
		comparedMetrics.setHalsteadMetrics(metrics.getHalsteadMetrics());
		comparedMetrics.setLinesOfCode(metrics.getLinesOfCode());
		comparedMetrics.setMaintainabilityIndex(metrics.getMaintainabilityIndex());
		return comparedMetrics;
	}

	private <T> Status compareMetrics(Comparable<T> currentMetric, T previousMetric) {
		return Status.fromValue(currentMetric.compareTo(previousMetric));
	}
}
