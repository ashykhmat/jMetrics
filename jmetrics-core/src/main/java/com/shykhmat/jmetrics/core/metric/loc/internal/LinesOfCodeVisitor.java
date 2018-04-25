package com.shykhmat.jmetrics.core.metric.loc.internal;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Method to visit Java Class and calculate metrics for it.
 */
public class LinesOfCodeVisitor extends VoidVisitorAdapter<Metrics> {
    private static final PrettyPrinterConfiguration SKIP_COMMENTS_CONFIGURATION;

    static {
        SKIP_COMMENTS_CONFIGURATION = new PrettyPrinterConfiguration();
        SKIP_COMMENTS_CONFIGURATION.setPrintComments(false);
        SKIP_COMMENTS_CONFIGURATION.setPrintJavadoc(false);
    };

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Metrics metrics) {
        visit((TypeDeclaration) classOrInterfaceDeclaration, metrics);
    }

    @Override
    public void visit(EnumDeclaration enumDeclaration, Metrics metrics) {
        visit((TypeDeclaration) enumDeclaration, metrics);
    }

    @Override
    public void visit(AnnotationDeclaration enumDeclaration, Metrics metrics) {
        visit((TypeDeclaration) enumDeclaration, metrics);
    }

    private void visit(TypeDeclaration typeDeclaration, Metrics metrics) {
        metrics.setLinesOfCode(calculateLOC(typeDeclaration.toString(SKIP_COMMENTS_CONFIGURATION)));
        Node parentNode = typeDeclaration.getParentNode().orElse(null);
        if (parentNode instanceof CompilationUnit) {
            for (Node childNode : parentNode.getChildNodes()) {
                if (childNode instanceof PackageDeclaration || childNode instanceof ImportDeclaration) {
                    metrics.setLinesOfCode(metrics.getLinesOfCode() + calculateLOC(childNode.toString(SKIP_COMMENTS_CONFIGURATION)));
                }
            }
        }
    }

    @Override
    public void visit(ConstructorDeclaration constructorDeclaration, Metrics metrics) {
        metrics.setLinesOfCode(calculateLOC(constructorDeclaration.toString(SKIP_COMMENTS_CONFIGURATION)));
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Metrics metrics) {
        metrics.setLinesOfCode(calculateLOC(methodDeclaration.toString(SKIP_COMMENTS_CONFIGURATION)));
    }

    private int calculateLOC(String codeString) {
        String[] tokenLines = codeString.replaceAll("/\\*\\*(?s:(?!\\*/).)*\\*/", "").split("\n", -1);
        return calculateLines(tokenLines);
    }

    private int calculateLines(String[] tokenLines) {
        int loc = 0;
        for (String token : tokenLines) {
            if (!token.replaceAll(" ", "").replaceAll("\t", "").isEmpty()) {
                loc++;
            }
        }
        return loc;
    }

}
