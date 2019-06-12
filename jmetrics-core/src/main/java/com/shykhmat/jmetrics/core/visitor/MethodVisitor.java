package com.shykhmat.jmetrics.core.visitor;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.metric.CompositeMetric;
import com.shykhmat.jmetrics.core.metric.MetricException;
import com.shykhmat.jmetrics.core.report.MethodReport;

/**
 * Method to visit Java Method and calculate metrics for it.
 */
public class MethodVisitor extends VoidVisitorAdapter<Set<MethodReport>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassVisitor.class);
    private CompositeMetric compositeMetric;

    public MethodVisitor(CompositeMetric compositeMetric) {
        this.compositeMetric = compositeMetric;
    }

    @Override
    public void visit(ConstructorDeclaration constructorDeclaration, Set<MethodReport> methods) {
        String parametersString = constructorDeclaration.getParameters().toString();
        parametersString.substring(1, parametersString.length() - 1);
        MethodReport method = new MethodReport(constructorDeclaration.getName() + "("
                + parametersString.substring(1, parametersString.length() - 1) + ")");
        LOGGER.debug("Processing constructor: " + method.getName());
        try {
            CodePart codePart = new CodePart(constructorDeclaration, CodePart.CodePartType.CONSTRUCTOR);
            method.setMetrics(compositeMetric.calculateMetric(codePart));
        } catch (Exception e) {
            LOGGER.error("Error during calulation class metrics", e);
        }
        methods.add(method);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Set<MethodReport> methods) {
        String parametersString = methodDeclaration.getParameters().toString();
        parametersString.substring(1, parametersString.length() - 1);
        MethodReport method = new MethodReport(
                methodDeclaration.getName() + "(" + parametersString.substring(1, parametersString.length() - 1) + ")");
        LOGGER.debug("Processing method: " + method.getName());
        try {
            CodePart codePart = new CodePart(methodDeclaration, CodePart.CodePartType.METHOD);
            method.setMetrics(compositeMetric.calculateMetric(codePart));
        } catch (MetricException e) {
            LOGGER.error("Error during calulation class metrics", e);
        }
        methods.add(method);
    }
}
