package com.shykhmat.jmetrics.core.parser;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import com.shykhmat.jmetrics.core.metric.CodePartType;

/**
 * Class that provides logic to parse Java source code into Java objects tree.
 */
public class JavaParser {
	private static final String JAVA_VERSION = JavaCore.VERSION_12;
	private static final Map<String, String> COMPILER_OPTIONS = initCompilerOptions();

	private JavaParser() {

	}

	public static ASTNode getCodePart(char[] codeSource, CodePartType codePartType) throws IOException {
		ASTParser parser = ASTParser.newParser(Integer.parseInt(JAVA_VERSION));
		parser.setCompilerOptions(COMPILER_OPTIONS);
		parser.setSource(codeSource);
		parser.setKind(codePartType.equals(CodePartType.METHOD) ? ASTParser.K_CLASS_BODY_DECLARATIONS
				: ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		return parser.createAST(null);
	}

	private static Map<String, String> initCompilerOptions() {
		Map<String, String> compilerOptions = JavaCore.getOptions();
		compilerOptions.put(JavaCore.COMPILER_COMPLIANCE, JAVA_VERSION);
		compilerOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JAVA_VERSION);
		compilerOptions.put(JavaCore.COMPILER_SOURCE, JAVA_VERSION);
		return compilerOptions;
	}
}
