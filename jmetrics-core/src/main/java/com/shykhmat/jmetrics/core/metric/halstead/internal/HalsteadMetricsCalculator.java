package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePart;
import com.shykhmat.jmetrics.core.metric.CodePart.CodePartType;

/**
 * Class to calculate operators and operands metrics, that are required for
 * Halstead metrics calculation.
 */
public class HalsteadMetricsCalculator {
	private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadMetricsCalculator.class);

	private static final String JAVA_VERSION = JavaCore.VERSION_12;

	public HalsteadComplexityMetrics calculate(CodePart codePart) throws Exception {
		HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
		getCodeNode(codePart).accept(halsteadVisitor);
		LOGGER.debug("Calculating final Halstead Metrics");
		HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics(halsteadVisitor.getOperators(),
				halsteadVisitor.getOperands());
		return halsteadMetrics.calculateMetric(codePart);
	}

	private ASTNode getCodeNode(CodePart codePart) throws IOException {
		ASTParser parser = ASTParser.newParser(Integer.parseInt(JAVA_VERSION));
		Map<String, String> compilerOptions = JavaCore.getOptions();
		compilerOptions.put(JavaCore.COMPILER_COMPLIANCE, JAVA_VERSION);
		compilerOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JAVA_VERSION);
		compilerOptions.put(JavaCore.COMPILER_SOURCE, JAVA_VERSION);
		parser.setCompilerOptions(compilerOptions);
		parser.setSource(codePart.getCodeToAnalyze().toString().toCharArray());
		parser.setKind(codePart.getCodeType().equals(CodePartType.METHOD) ? ASTParser.K_CLASS_BODY_DECLARATIONS
				: ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		return parser.createAST(null);
	}
}
