package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Class to visit code nodes and calculate Halstead metrics for them.
 */
public class HalsteadVisitor extends ASTVisitor {
	private static final String ASTERIX = "*";
	private static final String PACKAGE = "package";
	private static final String IMPORT = "import";
	private static final String DOT = ".";
	private static final String DOT_REGEX = "\\" + DOT;
	private static final String SEMICOLON = ";";

	private HashMap<String, Integer> operators = new HashMap<String, Integer>();
	private HashMap<String, Integer> operands = new HashMap<String, Integer>();

	public HashMap<String, Integer> getOperators() {
		return operators;
	}

	public HashMap<String, Integer> getOperands() {
		return operands;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		processPackage(node.getName().getFullyQualifiedName());
		addOperator(PACKAGE);
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		String importPackage = node.getName().getFullyQualifiedName();
		if (node.toString().contains(ASTERIX)) {
			importPackage = importPackage + DOT + ASTERIX;
		}
		processPackage(importPackage);
		addOperator(IMPORT);
		return super.visit(node);
	}
	

	private void processPackage(String fullyQualifiedName) {
		String[] nameParts = fullyQualifiedName.replaceAll(SEMICOLON, "").split(DOT_REGEX);
		if (nameParts.length == 1) {
			addOperand(fullyQualifiedName);
		} else {
			for (String namePart : nameParts) {
				if (ASTERIX.equals(namePart)) {
					addOperator(ASTERIX);
				} else {
					addOperand(namePart);
				}
			}
			addOperator(DOT, StringUtils.countMatches(fullyQualifiedName, DOT));
		}
		addOperator(SEMICOLON);
	}

	private void addOperand(String operand) {
		incrementCountInMap(operand, operands);
	}

	private void addOperator(String operator) {
		incrementCountInMap(operator, operators);
	}

	private void addOperand(String operand, Integer count) {
		incrementCountInMap(operand, count, operands);
	}

	private void addOperator(String operator, Integer count) {
		incrementCountInMap(operator, count, operators);
	}

	private void incrementCountInMap(String key, Map<String, Integer> map) {
		Integer count = map.get(key);
		if (count == null) {
			map.put(key, 1);
		} else {
			map.put(key, count + 1);
		}
	}

	private void incrementCountInMap(String key, Integer count, Map<String, Integer> map) {
		Integer currentCount = map.get(key);
		if (currentCount == null) {
			map.put(key, count);
		} else {
			map.put(key, currentCount + count);
		}
	}

}