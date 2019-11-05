package com.shykhmat.jmetrics.core.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;

import com.shykhmat.jmetrics.core.metric.composite.CompositeMetricCalculator;

/**
 * Common class for all visitors that process source code files.
 */
public abstract class AbstractVisitor extends ASTVisitor {
	protected CompositeMetricCalculator compositeMetricCalculator = new CompositeMetricCalculator();
}
