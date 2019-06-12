package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Class to visit code nodes and calculate Halstead metrics for them.
 */
public class HalsteadVisitor extends ASTVisitor {
    private static final String PACKAGE = "package";
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
    public void endVisit(PackageDeclaration node) {
        // Test with *
        String fullyQuailifiedPackageDeclaration = node.toString();
        if (fullyQuailifiedPackageDeclaration != null) {
            String packageName = node.getName().getFullyQualifiedName();
            String[] packageParts = packageName.replaceAll(SEMICOLON, "").split(DOT_REGEX);
            if (packageParts.length == 1) {
                addOperand(packageName);
            } else {
                for (String packagePart : packageParts) {
                    if ("*".equals(packagePart)) {
                        addOperator("*");
                    } else {
                        // Test variable type with full package specified
                        addOperand(packagePart);
                    }
                }
                addOperator(DOT, StringUtils.countMatches(packageName, DOT));
            }
            // Test when multiple semicolons in the end of the line
            addOperator(SEMICOLON);
            addOperator(PACKAGE);
        }
        super.endVisit(node);
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