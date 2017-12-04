
package com.shykhmat.jmetrics.core.metric.cyclomatic.internal;

import java.lang.reflect.Modifier;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;

/**
 * Utility class that contains methods to check method visibility modifiers.
 */
public final class AccessorsUtils {
    private AccessorsUtils() {

    }

    public static boolean isAccessor(MethodDeclaration methodDeclaration) {
        return isPublicMethod(methodDeclaration) && methodDeclaration.getBody() != null
                && (isGetter(methodDeclaration) || isSetter(methodDeclaration));
    }

    private static boolean isPublicMethod(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getModifiers().contains(Modifier.PUBLIC);
    }

    private static boolean isGetter(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getParameters().isEmpty() && hasOneReturnStatement(methodDeclaration)
                && (isValidGetter(methodDeclaration) || isBooleanGetter(methodDeclaration))
                && referencePrivateProperty(getReturnVariable(methodDeclaration),
                        (TypeDeclaration) methodDeclaration.getParentNode().orElse(null));
    }

    private static boolean isSetter(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getName().getIdentifier().startsWith("set")
                && methodDeclaration.getParameters().size() == 1 && returnTypeIs(methodDeclaration, "void")
                && hasOneAssignementStatement(methodDeclaration)
                && referencePrivateProperty(getAssignVariable(methodDeclaration),
                        (TypeDeclaration) methodDeclaration.getParentNode().orElse(null));
    }

    private static boolean hasOneAssignementStatement(MethodDeclaration methodDeclaration) {
        BlockStmt blockStmt = methodDeclaration.getBody().orElse(null);
        if (blockStmt.getStatements() != null) {
            Statement statement = blockStmt.getStatements().get(0);
            if (statement instanceof ExpressionStmt) {
                ExpressionStmt expressionStmt = (ExpressionStmt) statement;
                return expressionStmt.getExpression() instanceof AssignExpr;
            }
        }
        return false;
    }

    private static String getAssignVariable(MethodDeclaration methodDeclaration) {
        BlockStmt blockStmt = methodDeclaration.getBody().orElse(null);
        if (blockStmt.getStatements() != null) {
            ExpressionStmt stmt = (ExpressionStmt) blockStmt.getStatements().get(0);
            AssignExpr assignExpr = (AssignExpr) stmt.getExpression();
            if (assignExpr.getTarget() instanceof FieldAccessExpr) {
                return ((FieldAccessExpr) assignExpr.getTarget()).getName().getIdentifier();
            } else if (assignExpr.getTarget() instanceof NameExpr) {
                return ((NameExpr) assignExpr.getTarget()).getNameAsString();
            }
        }
        return null;
    }

    private static boolean isBooleanGetter(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getName().getIdentifier().startsWith("is")
                && returnTypeIs(methodDeclaration, "boolean");
    }

    private static boolean isValidGetter(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getName().getIdentifier().startsWith("get");
    }

    private static boolean hasOneReturnStatement(MethodDeclaration methodDeclaration) {
        int returnsCount = 0;
        BlockStmt blockStmt = methodDeclaration.getBody().orElse(null);
        if (blockStmt.getStatements() != null) {
            for (Statement statement : blockStmt.getStatements()) {
                if (statement instanceof ReturnStmt) {
                    returnsCount++;
                }
            }
        }
        return returnsCount == 1;
    }

    private static String getReturnVariable(MethodDeclaration methodDeclaration) {
        String returnVariable = null;
        BlockStmt blockStmt = methodDeclaration.getBody().orElse(null);
        for (Statement statement : blockStmt.getStatements()) {
            if (statement instanceof ReturnStmt) {
                returnVariable = ((ReturnStmt) statement).getExpression().toString();
            }
        }
        return returnVariable;
    }

    private static boolean referencePrivateProperty(String variableName, TypeDeclaration classOrInterfaceDeclaration) {
        for (Object member : classOrInterfaceDeclaration.getMembers()) {
            if (member instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) member;
                if (field.getModifiers().contains(Modifier.PRIVATE)
                        && ((VariableDeclarator) field.getVariables().get(0)).getName().toString()
                                .equals(variableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean returnTypeIs(MethodDeclaration methodDeclaration, String expectedReturnType) {
        Type returnType = methodDeclaration.getType();
        return returnType != null && expectedReturnType.equals(returnType.toString());
    }

}
