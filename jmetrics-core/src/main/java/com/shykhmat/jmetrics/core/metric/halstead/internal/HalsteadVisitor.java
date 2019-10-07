package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Class to visit code nodes and calculate Halstead metrics for them.
 */
public class HalsteadVisitor extends ASTVisitor {
	private static final String NULL = "null";
	private static final String LAMBDA = "->";
	private static final String ASSIGNMENT = "=";
	private static final String ASTERIX = "*";
	private static final String PACKAGE = "package";
	private static final String IMPORT = "import";
	private static final String DOT = ".";
	private static final String DOT_REGEX = "\\" + DOT;
	private static final String SEMICOLON = ";";

	private Map<String, Integer> operators = new HashMap<String, Integer>();
	private Map<String, Integer> operands = new HashMap<String, Integer>();

	public Map<String, Integer> getOperators() {
		return operators;
	}

	public Map<String, Integer> getOperands() {
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

	@Override
	public boolean visit(InfixExpression node) {
		addOperator(node.getOperator().toString());
		return super.visit(node);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		addOperator(node.getOperator().toString());
		return super.visit(node);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		addOperator(node.getOperator().toString());
		return super.visit(node);
	}

	@Override
	public boolean visit(Assignment node) {
		addOperator(node.getOperator().toString());
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		if (node.getInitializer() != null) {
			addOperator(ASSIGNMENT);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		if (node.getInitializer() != null) {
			addOperator(ASSIGNMENT);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		addOperand(node.getIdentifier());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(LambdaExpression node) {
		addOperator(LAMBDA);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		addOperand(NULL);
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		addOperand(node.getLiteralValue());
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		addOperand(Character.toString(node.charValue()));
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		addOperand(Boolean.toString(node.booleanValue()));
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		addOperand(node.getToken());
		return super.visit(node);
	}

	private void processPackage(String fullyQualifiedName) {
		String[] nameParts = fullyQualifiedName.replaceAll(SEMICOLON, "").split(DOT_REGEX);
		if (nameParts.length > 1) {
			for (String namePart : nameParts) {
				if (ASTERIX.equals(namePart)) {
					addOperator(ASTERIX);
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