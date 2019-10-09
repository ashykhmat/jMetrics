
package com.shykhmat.jmetrics.core.metric.cyclomatic;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * Implementation of Visitor pattern to navigate through code part and to
 * calculate cyclomatic cyclomaticComplexity using McCabe formula.
 */
class McCabeVisitor extends ASTVisitor {
	private Integer cyclomaticComplexity = 0;

	public Integer getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	@Override
	public boolean visit(final CatchClause node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final ConditionalExpression node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final DoStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final EnhancedForStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final ForStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final IfStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final MethodDeclaration node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(final SwitchCase node) {
		if (!node.toString().startsWith("default")) {
			cyclomaticComplexity++;
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(final WhileStatement node) {
		cyclomaticComplexity++;
		return super.visit(node);
	}

	@Override
	public boolean visit(InfixExpression node) {
		if (!(node.getParent() instanceof InfixExpression)) {
			char[] chars = node.toString().toCharArray();
			int cyclomaticComplexityToAdd = 0;
			for (int i = 0; i < chars.length - 1; i++) {
				char next = chars[i];
				if ((next == '&' || next == '|') && (next == chars[i + 1])) {
					cyclomaticComplexityToAdd++;
				}
			}
			cyclomaticComplexity += cyclomaticComplexityToAdd;
		}
		return super.visit(node);
	}

}
