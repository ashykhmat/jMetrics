package com.shykhmat.jmetrics.core.visitor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.report.MethodReport;

/**
 * Method to visit Java Method and calculate metrics for it.
 */
public class MethodVisitor extends AbstractVisitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassVisitor.class);
	private Set<MethodReport> methodsReports = new HashSet<>();

	public Set<MethodReport> getMethodsReports() {
		return methodsReports;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodDeclaration methodDeclaration) {
		String parametersString = (String) methodDeclaration.parameters().stream().map(Object::toString)
				.collect(Collectors.joining(","));
		String methodFullyQualifiedName = methodDeclaration.getName().getFullyQualifiedName() + "(" + parametersString
				+ ")";
		MethodReport methodReport = new MethodReport(methodFullyQualifiedName);
		LOGGER.debug("Processing method {} ", methodFullyQualifiedName);
		methodReport.setMetrics(compositeMetricCalculator.calculateMetric(methodDeclaration));
		methodsReports.add(methodReport);
		return super.visit(methodDeclaration);
	}

}
