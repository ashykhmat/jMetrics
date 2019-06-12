
package com.shykhmat.jmetrics.core.metric.cyclomatic.internal;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Implementation of Visitor pattern to navigate through code part and to calculate cyclomatic complexity using McCabe
 * formula.
 */
public class McCabeVisitor extends VoidVisitorAdapter<Metrics> {

    @Override
    public void visit(MethodDeclaration methodDeclaration, Metrics metrics) {
        if (methodDeclaration.getBody() != null && !AccessorsUtils.isAccessor(methodDeclaration)) {
            metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
            super.visit(methodDeclaration, metrics);
        }
    }

    @Override
    public void visit(ConstructorDeclaration constructorDeclaration, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        super.visit(constructorDeclaration, metrics);
    }

    @Override
    public void visit(CatchClause catchClause, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        super.visit(catchClause, metrics);
    }

    @Override
    public void visit(ConditionalExpr conditionalExpr, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(conditionalExpr.getThenExpr(), metrics);
        inspectExpression(conditionalExpr.getElseExpr(), metrics);
        super.visit(conditionalExpr, metrics);
    }

    @Override
    public void visit(DoStmt doStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(doStmt.getCondition(), metrics);
        super.visit(doStmt, metrics);
    }

    @Override
    public void visit(ReturnStmt returnStmt, Metrics metrics) {
        MethodDeclaration methodDeclaration = findMethodDeclaration(returnStmt);
        BlockStmt blockStmt = methodDeclaration.getBody().orElse(null);
        if (!returnStmt.equals(blockStmt.getChildNodes().get(blockStmt.getChildNodes().size() - 1))) {
            metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
            inspectExpression(returnStmt.getExpression().orElse(null), metrics);
            super.visit(returnStmt, metrics);
        }
    }

    private MethodDeclaration findMethodDeclaration(Node node) {
        if (node instanceof MethodDeclaration) {
            return (MethodDeclaration) node;
        }
        return findMethodDeclaration(node.getParentNode().orElse(null));
    }

    @Override
    public void visit(ThrowStmt throwStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(throwStmt.getExpression(), metrics);
        super.visit(throwStmt, metrics);
    }

    @Override
    public void visit(ExpressionStmt expressionStmt, Metrics metrics) {
        inspectExpression(expressionStmt.getExpression(), metrics);
    }

    @Override
    public void visit(ForStmt forStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(forStmt.getCompare().orElse(null), metrics);
        super.visit(forStmt, metrics);
    }

    @Override
    public void visit(ForeachStmt forEachStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(forEachStmt.getIterable(), metrics);
        super.visit(forEachStmt, metrics);
    }

    @Override
    public void visit(IfStmt ifStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        inspectExpression(ifStmt.getCondition(), metrics);
        super.visit(ifStmt, metrics);
    }

    @Override
    public void visit(SwitchEntryStmt switchEntryStmt, Metrics metrics) {
        if (switchEntryStmt.getLabel() != null) {
            metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
        }
    }

    @Override
    public void visit(WhileStmt whileStmt, Metrics metrics) {
        metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
    }

    private void inspectExpression(Expression expression, Metrics metrics) {
        if (expression != null) {
            findConditionalExpr(expression, metrics);
            char[] chars = expression.toString().toCharArray();
            for (int i = 0; i < chars.length - 1; i++) {
                char next = chars[i];
                if ((next == '&' || next == '|') && (next == chars[i + 1])) {
                    metrics.setCyclomaticComplexity(metrics.getCyclomaticComplexity() + 1);
                }
            }
        }
    }

    private void findConditionalExpr(Expression expression, Metrics metrics) {
        if (expression instanceof ConditionalExpr) {
            visit((ConditionalExpr) expression, metrics);
        } else {
            for (Node node : expression.getChildNodes()) {
                if (node instanceof Expression) {
                    findConditionalExpr((Expression) node, metrics);
                }
            }
        }
    }

}
