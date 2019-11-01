package com.shykhmat.jmetrics.core.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.metric.coupling.EfferentCouplingVisitor;
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
		try {
			CompilationUnit compilationUnit = getCompilationUnit(node);
			PackageDeclaration packageDeclaration = compilationUnit.getPackage();
			String packageName = packageDeclaration != null ? packageDeclaration.getName().getFullyQualifiedName() + "."
					: "default.";
			String fullyQualifiedTypeName = packageName + node.getName().getFullyQualifiedName();
			LOGGER.debug("Processing {} {}", codeType, fullyQualifiedTypeName);
			classReport = new ClassReport(fullyQualifiedTypeName);
			boolean isAbstractClass = node.modifiers().stream()
					.filter(modifier -> modifier instanceof Modifier
							&& "abstract".equals(((Modifier) modifier).getKeyword().toString()))
					.findFirst().orElse(null) != null;
			classReport.setIsAbstractOrInterface(CodePartType.INTERFACE.equals(codeType) || isAbstractClass);
			MethodVisitor methodVisitor = new MethodVisitor();
			node.bodyDeclarations().stream().filter(body -> body instanceof MethodDeclaration)
					.forEach(method -> ((MethodDeclaration) method).accept(methodVisitor));
			classReport.setMetrics(compositeMetricCalculator.calculateMetric(node));
			classReport.setMethods(methodVisitor.getMethodsReports());
			EfferentCouplingVisitor efferentCouplingVisitor = new EfferentCouplingVisitor(packageName);
			compilationUnit.imports()
					.forEach(importDeclaration -> efferentCouplingVisitor.visit((ImportDeclaration) importDeclaration));
			node.accept(efferentCouplingVisitor);
			classReport.setEfferentCouplingAll(efferentCouplingVisitor.getEfferentCouplingAll());
			classReport.setEfferentCouplingUsed(efferentCouplingVisitor.getEfferentCouplingUsed());
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	private CompilationUnit getCompilationUnit(AbstractTypeDeclaration node) {
		ASTNode parentNode = node.getParent();
		while (!(parentNode instanceof CompilationUnit)) {
			parentNode = parentNode.getParent();
		}
		return (CompilationUnit) parentNode;
	}

	protected ClassReport getClassReport() {
		return classReport;
	}

}
