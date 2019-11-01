package com.shykhmat.jmetrics.core.metric.coupling;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

/**
 * Class to visit code nodes and calculate efferent coupling metric for their
 * class. Coupling is pre-calculated and should be post-processed, to resolve
 * imports from current package and import all (import *) constructs. That
 * post-processing should go through all processed packages and check if class
 * without implicit package belongs to the same package or to import all.
 */
public class EfferentCouplingVisitor extends ASTVisitor {
	private static final String JAVA_LANG_PACKAGE = "java.lang.";
	private static final String ASTERIX = "*";
	private static final String DOT = ".";
	private static final String SEMICOLON = ";";

	private String packagePrefix;
	private Set<String> efferentCouplingAll = new TreeSet<>();
	private Set<String> efferentCouplingUsed = new TreeSet<>();
	private boolean containsImportAll;
	private Set<String> staticImports = new HashSet<>();

	public EfferentCouplingVisitor(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	public Set<String> getEfferentCouplingAll() {
		return efferentCouplingAll;
	}

	public Set<String> getEfferentCouplingUsed() {
		return efferentCouplingUsed;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		packagePrefix = node.getName().getFullyQualifiedName() + DOT;
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		String importClass = node.getName().getFullyQualifiedName().replaceAll(SEMICOLON, "");
		if (node.isStatic()) {
			String nonStaticImport = importClass.substring(0, importClass.lastIndexOf("."));
			if (!isJavaLangPackage(nonStaticImport)) {
				staticImports.add(importClass);
				efferentCouplingAll.add(nonStaticImport);
			}
		} else if (!isJavaLangPackage(importClass)) {
			if (node.toString().contains(ASTERIX)) {
				efferentCouplingAll.add(importClass + DOT + ASTERIX);
				containsImportAll = true;
			} else {
				efferentCouplingAll.add(importClass);
			}
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleType node) {
		processType(node.getName().toString());
		return super.visit(node);
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		processType(node.getTypeName().toString());
		return super.visit(node);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(NormalAnnotation node) {
		processType(node.getTypeName().toString());
		List<MemberValuePair> values = node.values();
		values.stream().filter(value -> value.getValue() instanceof QualifiedName)
				.forEach(value -> processType(((QualifiedName) value.getValue()).getQualifier().toString()));
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		processType(node.getTypeName().toString());
		if (node.getValue() instanceof QualifiedName) {
			processType(((QualifiedName) node.getValue()).getQualifier().toString());
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodInvocation node) {
		String methodName = node.getName().toString();
		Optional<String> usedStaticImport = staticImports.stream()
				.filter(staticImport -> staticImport.endsWith(DOT + methodName))
				.map(staticImport -> staticImport.replaceAll(DOT + methodName, "")).findFirst();
		if (usedStaticImport.isPresent()) {
			efferentCouplingUsed.add(usedStaticImport.get());
		}
		return super.visit(node);
	}

	private void processType(String typeName) {
		String fullyQualifiedTypeName;
		if (typeName.contains(DOT) && !isJavaLangPackage(typeName)) {
			fullyQualifiedTypeName = typeName;
			efferentCouplingAll.add(fullyQualifiedTypeName);
		} else {
			String typeNameFilter = DOT + typeName;
			fullyQualifiedTypeName = efferentCouplingAll.stream().filter(type -> type.endsWith(typeNameFilter))
					.findFirst().orElseGet(() -> {
						if (!isJavaLangClass(typeName)) {
							String nameWithPackage = containsImportAll ? typeName
									: (packagePrefix == null ? "default." : packagePrefix) + typeName;
							efferentCouplingAll.add(nameWithPackage);
							return nameWithPackage;
						}
						return null;
					});
		}
		if (fullyQualifiedTypeName != null) {
			efferentCouplingUsed.add(fullyQualifiedTypeName);
		}
	}

	private boolean isJavaLangClass(String className) {
		try {
			Class.forName(JAVA_LANG_PACKAGE + className);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isJavaLangPackage(String importName) {
		return StringUtils.countMatches(importName, DOT) == 2 && importName.startsWith(JAVA_LANG_PACKAGE);
	}

}
