
package com.shykhmat.jmetrics.core.metric;

import org.slf4j.Logger;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Template class for all metrics that requires visit different code parts to be calculated.
 * 
 * @param <T>
 *            - metric return type
 */
public abstract class VisitorMetric<T extends Number> implements Metric<CodePart, T> {
    @Override
    public T calculateMetric(CodePart codePart) {
        long startTime = System.nanoTime();
        getLogger().debug("Calculating {} metric", getMetricName());
        Metrics metrics = new Metrics();
        switch (codePart.getCodeType()) {
        case METHOD: {
            getVisitor().visit((MethodDeclaration) codePart.getCodeToAnalyze(), metrics);
            break;
        }
        case CONSTRUCTOR: {
            getVisitor().visit((ConstructorDeclaration) codePart.getCodeToAnalyze(), metrics);
            break;
        }
        case ENUM: {
            getVisitor().visit((EnumDeclaration) codePart.getCodeToAnalyze(), metrics);
            break;
        }
        case ANNOTATION: {
            getVisitor().visit((AnnotationDeclaration) codePart.getCodeToAnalyze(), metrics);
            break;
        }
        default: {
            getVisitor().visit((ClassOrInterfaceDeclaration) codePart.getCodeToAnalyze(), metrics);
        }
        }
        long totalTime = System.nanoTime() - startTime;
        getLogger().debug("Calculation done. Total execution time: {}. Result: {} ", totalTime,
                getMetricValue(metrics));
        return getMetricValue(metrics);
    }

    /**
     * Method to retrieve concrete logger that should be used to log messages.
     */
    protected abstract Logger getLogger();

    /**
     * Method to retrieve metric name.
     */
    protected abstract String getMetricName();

    /**
     * Method to retrieve concrete visitor that contains logic to calculate metric.
     */
    protected abstract VoidVisitorAdapter<Metrics> getVisitor();

    /**
     * Method to retrieve calculated metric value.
     */
    protected abstract T getMetricValue(Metrics metrics);
}
