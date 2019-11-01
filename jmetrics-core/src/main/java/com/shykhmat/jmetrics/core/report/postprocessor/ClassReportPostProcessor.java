package com.shykhmat.jmetrics.core.report.postprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.shykhmat.jmetrics.core.metric.coupling.CouplingMetricsCalculator;
import com.shykhmat.jmetrics.core.report.ClassReport;

/**
 * Class that is used to calculate different class level metrics after project
 * is completely parsed and primary metrics are calculated from code.
 */
public class ClassReportPostProcessor {
	private CouplingMetricsCalculator couplingMetricsCalculator = new CouplingMetricsCalculator();

	public void process(Set<ClassReport> classesReports) {
		Set<String> knownClassNames = classesReports.stream().map(ClassReport::getName).collect(Collectors.toSet());
		Map<String, Set<String>> classEfferentCouplingAll = new HashMap<>();
		Map<String, Set<String>> classEfferentCouplingUsed = new HashMap<>();
		Map<String, Set<String>> classAfferentCouplingAll = new HashMap<>();
		Map<String, Set<String>> classAfferentCouplingUsed = new HashMap<>();
		classesReports.stream().forEach(classReport -> {
			String className = classReport.getName();
			processClassCoupling(knownClassNames, classEfferentCouplingAll, classAfferentCouplingAll,
					classReport.getEfferentCouplingAll(), className);
			processClassCoupling(knownClassNames, classEfferentCouplingUsed, classAfferentCouplingUsed,
					classReport.getEfferentCouplingUsed(), className);
		});
		classesReports.stream().forEach(classReport -> {
			String className = classReport.getName();
			classReport.setAfferentCouplingAll(classAfferentCouplingAll.get(className));
			classReport.setAfferentCouplingUsed(classAfferentCouplingUsed.get(className));
			classReport.setEfferentCouplingAll(classEfferentCouplingAll.get(className));
			classReport.setEfferentCouplingUsed(classEfferentCouplingUsed.get(className));
			classReport.setInstability(couplingMetricsCalculator.calculateInstability(
					classReport.getEfferentCouplingUsed().size(), classReport.getAfferentCouplingUsed().size()));
		});
	}

	private void processClassCoupling(Set<String> knownClassNames, Map<String, Set<String>> processedEfferentCouplings,
			Map<String, Set<String>> processedAfferentCouplings, Set<String> existingEfferentCouplings,
			String className) {
		Set<String> efferentCouplings = new TreeSet<>();
		processedEfferentCouplings.put(className, efferentCouplings);
		Set<String> classAfferentCouplings = processedAfferentCouplings.get(className);
		if (classAfferentCouplings == null) {
			classAfferentCouplings = new TreeSet<>();
			processedAfferentCouplings.put(className, classAfferentCouplings);
		}
		Set<String> importAll = existingEfferentCouplings.stream()
				.filter(efferentCoupling -> efferentCoupling.contains("*"))
				.map(efferentCoupling -> efferentCoupling.replaceAll("\\*", "")).collect(Collectors.toSet());
		existingEfferentCouplings.stream().filter(efferentCoupling -> !efferentCoupling.contains("*"))
				.forEach(efferentCoupling -> {
					String processedEfferentCoupling = efferentCoupling;
					if (!efferentCoupling.contains(".")) {
						String defaultPackageClassName = "default." + efferentCoupling;
						if (knownClassNames.contains(defaultPackageClassName)) {
							processedEfferentCoupling = defaultPackageClassName;
						} else {
							if (importAll.size() == 1) {
								processedEfferentCoupling = importAll.stream().findFirst().get() + efferentCoupling;
								knownClassNames.add(processedEfferentCoupling);
							} else if (importAll.size() > 1) {
								processedEfferentCoupling = importAll.stream()
										.map(importDeclaration -> importDeclaration + efferentCoupling)
										.filter(importDeclaration -> knownClassNames.contains(importDeclaration))
										.findFirst().orElse(processedEfferentCoupling);
							}
						}
					}
					if (!className.equals(processedEfferentCoupling)) {
						efferentCouplings.add(processedEfferentCoupling);
						Set<String> afferentCouplings = processedAfferentCouplings.get(processedEfferentCoupling);
						if (afferentCouplings == null) {
							afferentCouplings = new TreeSet<>();
							processedAfferentCouplings.put(processedEfferentCoupling, afferentCouplings);
						}
						afferentCouplings.add(className);
					}
				});
	}
}
