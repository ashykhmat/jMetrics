package com.shykhmat.jmetrics.core.visitor;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.shykhmat.jmetrics.core.report.ClassReport;

public class CompilationUnitVisitor extends AbstractVisitor {
	private Set<ClassReport> classesReports = new TreeSet<>();

	@SuppressWarnings("unchecked")
	public void visit(ASTNode compilationUnit) {
		((CompilationUnit) compilationUnit).types().stream().forEach(type -> {
			ClassVisitor classVisitor = new ClassVisitor();
			switch (type.getClass().getCanonicalName()) {
			case "org.eclipse.jdt.core.dom.EnumDeclaration": {
				classVisitor.visit((EnumDeclaration) type);
				break;
			}
			case "org.eclipse.jdt.core.dom.AnnotationTypeDeclaration": {
				classVisitor.visit((AnnotationTypeDeclaration) type);
				break;
			}
			default: {
				classVisitor.visit((TypeDeclaration) type);
			}
			}
			classesReports.add(classVisitor.getClassReport());
		});
	}

	public Set<ClassReport> getClassesReports() {
		return classesReports;
	}
}
