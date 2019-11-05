package com.shykhmat.jmetrics.core.metric.coupling;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.ExtendedCodePartReport;
import com.shykhmat.jmetrics.core.report.PackageReport;

/**
 * Class to find and collect circular dependencies between packages. More
 * details here: https://en.wikipedia.org/wiki/Circular_dependency
 */
public class CircularDependenciesCalculator {
	public Set<String> calculatePackagesCircularDependencies(Set<PackageReport> packages) {
		return calculateCircularDependencies(packages);
	}

	public Set<String> calculateClassesCircularDependencies(Set<ClassReport> classes) {
		return calculateCircularDependencies(classes);
	}

	private Set<String> calculateCircularDependencies(Set<? extends ExtendedCodePartReport> codeParts) {
		Set<List<String>> circularDependencies = new HashSet<>();
		Map<String, Set<String>> dependenciesMap = codeParts.stream().collect(
				Collectors.toMap(ExtendedCodePartReport::getName, ExtendedCodePartReport::getEfferentCouplingUsed));
		dependenciesMap.forEach((packageName, dependencies) -> {
			List<String> currentFlow = new LinkedList<>();
			currentFlow.add(packageName);
			processCodePart(packageName, dependencies, currentFlow, circularDependencies, dependenciesMap);

		});
		return circularDependencies.stream().map(dependencies -> String.join("->", dependencies))
				.collect(Collectors.toSet());
	}

	private void processCodePart(String rootCodePartName, Set<String> dependencies, List<String> currentFlow,
			Set<List<String>> circularDependencies, Map<String, Set<String>> dependenciesMap) {
		dependencies.forEach(dependency -> {
			if (rootCodePartName.equals(dependency)) {
				List<String> foundCircularDependency = new LinkedList<>(currentFlow);
				foundCircularDependency.add(dependency);
				circularDependencies.add(foundCircularDependency);
				return;
			} else if (currentFlow.contains(dependency)) {
				return;
			}
			processDependencies(rootCodePartName, dependency, currentFlow, circularDependencies, dependenciesMap);
		});
	}

	private void processDependencies(String rootCodePartName, String dependency, List<String> currentFlow,
			Set<List<String>> circularDependencies, Map<String, Set<String>> dependenciesMap) {
		Set<String> dependencies = dependenciesMap.get(dependency);
		if (dependencies != null) {
			currentFlow.add(dependency);
			processCodePart(rootCodePartName, dependencies, currentFlow, circularDependencies, dependenciesMap);
			currentFlow.remove(dependency);
		}
	}
}
