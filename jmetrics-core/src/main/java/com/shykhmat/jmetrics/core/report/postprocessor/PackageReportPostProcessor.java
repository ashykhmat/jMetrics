package com.shykhmat.jmetrics.core.report.postprocessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.shykhmat.jmetrics.core.metric.coupling.CouplingMetricsCalculator;
import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.PackageReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class that is used to calculate different package level metrics after project
 * is completely parsed and primary metrics are calculated from code.
 */
public class PackageReportPostProcessor {
	private CouplingMetricsCalculator couplingMetricsCalculator = new CouplingMetricsCalculator();

	public void process(ProjectReport projectReport) {
		Set<ClassReport> classesReports = projectReport.getClasses();
		Map<String, PackageReport> packageReports = new HashMap<>();
		classesReports.forEach(classReport -> {
			String packageName = getPackageFromClassName(classReport.getName());
			PackageReport packageReport = packageReports.get(packageName);
			if (packageReport == null) {
				packageReport = new PackageReport(packageName);
				packageReports.put(packageName, packageReport);
			}
			processCouplings(packageName, packageReport.getEfferentCouplingAll(), classReport.getEfferentCouplingAll());
			processCouplings(packageName, packageReport.getEfferentCouplingUsed(),
					classReport.getEfferentCouplingUsed());
			processCouplings(packageName, packageReport.getAfferentCouplingAll(), classReport.getAfferentCouplingAll());
			processCouplings(packageName, packageReport.getAfferentCouplingUsed(),
					classReport.getAfferentCouplingUsed());
			if (classReport.isAbstractOrInterface()) {
				packageReport.countAbstractClasseInterface();
			} else {
				packageReport.countNonAbstractClass();
			}
		});
		packageReports.values().forEach(packageReport -> {
			packageReport.setInstability(couplingMetricsCalculator.calculateInstability(
					packageReport.getEfferentCouplingUsed().size(), packageReport.getAfferentCouplingUsed().size()));
			packageReport.setAbstractness(couplingMetricsCalculator.calculateAbstractness(
					packageReport.getNonAbstractClassesNumber(), packageReport.getAbstractClassesInterfacesNumber()));
			packageReport.setDistance(couplingMetricsCalculator.calculateDistance(packageReport.getAbstractness(),
					packageReport.getInstability()));
		});
		projectReport.setPackages(new HashSet<>(packageReports.values()));
	}

	private String getPackageFromClassName(String className) {
		String packageName = "default";
		if (className.contains(".")) {
			packageName = className.substring(0, className.lastIndexOf("."));
		}
		return packageName;
	}

	private void processCouplings(String packageName, Set<String> packageEfferentCouplings,
			Set<String> classEfferentCouplings) {
		packageEfferentCouplings.addAll(
				classEfferentCouplings.stream().map(efferentCoupling -> getPackageFromClassName(efferentCoupling))
						.filter(efferentCoupling -> !packageName.equals(efferentCoupling)).collect(Collectors.toSet()));
	}
}
