package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.metric.CodePart.CodePartType;
/**
 * Class to calculate operators and operands metrics, that are required for Halstead metrics calculation.
 */
public class HalsteadMetricsCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadMetricsCalculator.class);

    public HalsteadComplexityMetrics calculate(CodePart codePart) throws Exception {
    	HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
    	getCompilationUnit(codePart).accept(halsteadVisitor);
        LOGGER.debug("Calculating final Halstead Metrics");
        HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics(halsteadVisitor.getOperators(), halsteadVisitor.getOperands());
        return halsteadMetrics.calculateMetric(codePart);
    }
    
	private CompilationUnit getCompilationUnit(CodePart codePart) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS11);
		parser.setSource(codePart.getCodeToAnalyze().toString().toCharArray());
		parser.setKind(codePart.getCodeType().equals(CodePartType.METHOD) ? ASTParser.K_CLASS_BODY_DECLARATIONS : ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
