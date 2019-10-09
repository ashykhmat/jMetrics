package com.shykhmat.jmetrics.core.visitor;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.report.ClassReport;

/**
 * Class that contains functionality to collect metrics for java classes,
 * interfaces, enums and annotations.
 */
public class ClassVisitor extends AbstractVisitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassVisitor.class);
	private ClassReport classReport;

	@Override
	public boolean visit(AnnotationTypeDeclaration annotationTypeDeclaration) {
		processDeclaration(annotationTypeDeclaration, CodePartType.ANNOTATION);
		return super.visit(annotationTypeDeclaration);
	}

	@Override
	public boolean visit(EnumDeclaration enumDeclaration) {
		processDeclaration(enumDeclaration, CodePartType.ENUM);
		return super.visit(enumDeclaration);
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration) {
		processDeclaration(typeDeclaration,
				typeDeclaration.isInterface() ? CodePartType.INTERFACE : CodePartType.CLASS);
		return super.visit(typeDeclaration);
	}

	@SuppressWarnings("unchecked")
	private void processDeclaration(AbstractTypeDeclaration node, CodePartType codeType) {
		PackageDeclaration packageDeclaration = ((CompilationUnit) node.getParent()).getPackage();
		String packageName = packageDeclaration != null ? packageDeclaration.getName().getFullyQualifiedName() + "."
				: "";
		String fullyQualifiedTypeName = packageName + node.getName().getFullyQualifiedName();
		LOGGER.debug("Processing {} {}", codeType, fullyQualifiedTypeName);
		classReport = new ClassReport(fullyQualifiedTypeName);
		classReport.setInterface(CodePartType.INTERFACE.equals(codeType));
		MethodVisitor methodVisitor = new MethodVisitor();
		node.bodyDeclarations().stream().filter(body -> body instanceof MethodDeclaration)
				.forEach(method -> methodVisitor.visit((MethodDeclaration) method));
		classReport.setMetrics(compositeMetricCalculator.calculateMetric(node));
		classReport.setMethods(methodVisitor.getMethodsReports());
	}

	protected ClassReport getClassReport() {
		return classReport;
	}

}
