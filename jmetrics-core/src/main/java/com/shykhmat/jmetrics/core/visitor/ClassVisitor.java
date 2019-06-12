package com.shykhmat.jmetrics.core.visitor;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.metric.CompositeMetric;
import com.shykhmat.jmetrics.core.metric.MetricException;
import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.metric.CodePart;

/**
 * Class that contains functionality to collect metrics for java classes, interfaces, enums and annotations.
 */
public class ClassVisitor extends VoidVisitorAdapter<Set<ClassReport>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassVisitor.class);
    private CompositeMetric compositeMetric;

    public ClassVisitor(CompositeMetric compositeMetric) {
        this.compositeMetric = compositeMetric;
    }

    // TODO refactor

    @Override
    public void visit(AnnotationDeclaration annotationDeclaration, Set<ClassReport> classes) {
        PackageDeclaration packageDeclaration = ((CompilationUnit) annotationDeclaration.getParentNode().orElse(null))
                .getPackageDeclaration().orElse(null);
        ClassReport classToAdd = new ClassReport(packageDeclaration == null ? annotationDeclaration.getName().asString()
                : packageDeclaration.getNameAsString() + "." + annotationDeclaration.getName().asString());
        classToAdd.setInterface(false);
        LOGGER.debug("Processing annotation: " + classToAdd.getName());
        new MethodVisitor(compositeMetric).visit(annotationDeclaration, classToAdd.getMethods());
        try {
            CodePart codePart = new CodePart(annotationDeclaration, CodePart.CodePartType.ANNOTATION);
            classToAdd.setMetrics(compositeMetric.calculateMetric(codePart));
        } catch (MetricException e) {
            LOGGER.error("Error during calulation class metrics", e);
        }
        classes.add(classToAdd);
    }

    @Override
    public void visit(EnumDeclaration enumDeclaration, Set<ClassReport> classes) {
        PackageDeclaration packageDeclaration = ((CompilationUnit) enumDeclaration.getParentNode().orElse(null))
                .getPackageDeclaration().orElse(null);
        ClassReport classToAdd = new ClassReport(packageDeclaration == null ? enumDeclaration.getName().asString()
                : packageDeclaration.getNameAsString() + "." + enumDeclaration.getName().asString());
        classToAdd.setInterface(false);
        LOGGER.debug("Processing enum: " + classToAdd.getName());
        new MethodVisitor(compositeMetric).visit(enumDeclaration, classToAdd.getMethods());
        try {
            CodePart codePart = new CodePart(enumDeclaration, CodePart.CodePartType.ENUM);
            classToAdd.setMetrics(compositeMetric.calculateMetric(codePart));
        } catch (Exception e) {
            LOGGER.error("Error during calulation class metrics", e);
        }
        classes.add(classToAdd);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classDeclaration, Set<ClassReport> classes) {
        PackageDeclaration packageDeclaration = ((CompilationUnit) classDeclaration.getParentNode().orElse(null))
                .getPackageDeclaration().orElse(null);
        ClassReport classToAdd = new ClassReport(packageDeclaration == null ? classDeclaration.getName().asString()
                : packageDeclaration.getNameAsString() + "." + classDeclaration.getName().asString());
        classToAdd.setInterface(classDeclaration.isInterface());
        LOGGER.debug("Processing class/interface: " + classToAdd.getName());
        new MethodVisitor(compositeMetric).visit(classDeclaration, classToAdd.getMethods());
        try {
            CodePart codePart = new CodePart(classDeclaration,
                    classToAdd.isInterface() ? CodePart.CodePartType.INTERFACE : CodePart.CodePartType.CLASS);
            classToAdd.setMetrics(compositeMetric.calculateMetric(codePart));
        } catch (MetricException e) {
            LOGGER.error("Error during calulation class metrics", e);
        }
        classes.add(classToAdd);
    }
}
