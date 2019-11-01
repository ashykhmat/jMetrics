package com.shykhmat.jmetrics.core.visitor;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.shykhmat.jmetrics.core.report.ClassReport;

public class CompilationUnitVisitor extends AbstractVisitor {
	private Set<ClassReport> classesReports = new TreeSet<>();

	@SuppressWarnings("unchecked")
	public boolean visit(CompilationUnit compilationUnit) {
		compilationUnit.types().stream().forEach(type -> {
			ClassVisitor classVisitor = new ClassVisitor();
			compilationUnit.accept(classVisitor);
			classesReports.add(classVisitor.getClassReport());
		});
		return super.visit(compilationUnit);
	}

	public Set<ClassReport> getClassesReports() {
		return classesReports;
	}
}
