package com.shykhmat.jmetrics.core.metric.halstead;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.parser.JavaParser;
import com.shykhmat.jmetrics.core.report.HalsteadComplexityMetrics;

public class HalsteadComplexityCalculatorTest {

	@Test
	public void testEmptyPackageVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		Map<String, Integer> expectedOperands = new HashMap<>();
		assertCalculations("test-data/package/EmptyPackageName.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testSimplePackageVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put("package", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("test", 1);
		assertCalculations("test-data/package/SimplePackageName.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testComplexPackageVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 2);
		expectedOperators.put("package", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertCalculations("test-data/package/ComplexPackageName.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testPackageWithAdditionalSemicolonVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 2);
		expectedOperators.put("package", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertCalculations("test-data/package/PackageNameAndAdditionalSemicolon.test", CodePartType.NONE,
				expectedOperators, expectedOperands);
	}

	@Test
	public void testEmptyImportVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		Map<String, Integer> expectedOperands = new HashMap<>();
		assertCalculations("test-data/import/EmptyImport.test", CodePartType.NONE, expectedOperators, expectedOperands);
	}

	@Test
	public void testSimpleImportVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 1);
		expectedOperators.put("import", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("test", 1);
		expectedOperands.put("Test", 1);
		assertCalculations("test-data/import/SimpleImport.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testComplexImportVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 3);
		expectedOperators.put("import", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		expectedOperands.put("Test", 1);
		assertCalculations("test-data/import/ComplexImport.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testImportAllVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 3);
		expectedOperators.put("*", 1);
		expectedOperators.put("import", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertCalculations("test-data/import/ImportAll.test", CodePartType.NONE, expectedOperators, expectedOperands);
	}

	@Test
	public void testMultipleImportsVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 3);
		expectedOperators.put(".", 6);
		expectedOperators.put("*", 1);
		expectedOperators.put("import", 3);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 2);
		expectedOperands.put("shykhmat", 2);
		expectedOperands.put("test", 2);
		expectedOperands.put("Test1", 1);
		expectedOperands.put("Test2", 1);
		assertCalculations("test-data/import/MultipleImports.test", CodePartType.NONE, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testMethodVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put("==", 1);
		expectedOperators.put("=", 3);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("a", 3);
		expectedOperands.put("b", 3);
		expectedOperands.put("3", 2);
		expectedOperands.put("method", 1);
		expectedOperands.put("4", 1);
		expectedOperands.put("MISSING", 1);
		assertCalculations("test-data/class/Method.test", CodePartType.METHOD, expectedOperators, expectedOperands);
	}

	@Test
	public void testClassVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put("==", 1);
		expectedOperators.put("=", 3);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("a", 3);
		expectedOperands.put("b", 3);
		expectedOperands.put("3", 2);
		expectedOperands.put("method", 1);
		expectedOperands.put("4", 1);
		expectedOperands.put("A", 1);
		assertCalculations("test-data/class/Class.test", CodePartType.CLASS, expectedOperators, expectedOperands);
	}

	@Test
	public void testEnumWVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("Enum", 1);
		expectedOperands.put("VALUE_1", 1);
		expectedOperands.put("VALUE_2", 1);
		expectedOperands.put("VALUE_3", 1);
		assertCalculations("test-data/class/Enum.test", CodePartType.ENUM, expectedOperators, expectedOperands);
	}

	@Test
	public void testEnumWithMethodVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put("==", 1);
		expectedOperators.put("=", 3);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("a", 3);
		expectedOperands.put("b", 3);
		expectedOperands.put("3", 2);
		expectedOperands.put("method", 1);
		expectedOperands.put("4", 1);
		expectedOperands.put("Enum", 1);
		expectedOperands.put("VALUE_1", 1);
		expectedOperands.put("VALUE_2", 1);
		expectedOperands.put("VALUE_3", 1);
		assertCalculations("test-data/class/EnumWithMethod.test", CodePartType.ENUM, expectedOperators,
				expectedOperands);
	}

	@Test
	public void testLambdaVisit() {
		Map<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put("=", 1);
		expectedOperators.put("->", 1);
		Map<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("Integer", 2);
		expectedOperands.put("System", 1);
		expectedOperands.put("out", 1);
		expectedOperands.put("println", 1);
		expectedOperands.put("method", 1);
		expectedOperands.put("x", 2);
		expectedOperands.put("c", 1);
		expectedOperands.put("Consumer", 1);
		expectedOperands.put("MISSING", 1);
		assertCalculations("test-data/class/Lambda.test", CodePartType.METHOD, expectedOperators, expectedOperands);
	}

	private void assertCalculations(String filePath, CodePartType type, Map<String, Integer> expectedOperators,
			Map<String, Integer> expectedOperands) {
		HalsteadComplexityCalculator halsteadComplexityCalculator = new HalsteadComplexityCalculator();
		try {
			ASTNode codeToTest = JavaParser.getCodePart(
					Resources.toString(Resources.getResource(filePath), Charsets.UTF_8).toCharArray(), type);
			HalsteadComplexityMetrics metrics = halsteadComplexityCalculator.calculateMetric(codeToTest);
			assertThat(metrics.getOperands().size(), is(expectedOperands.size()));
			assertThat(metrics.getOperands(), is(expectedOperands));
			assertThat(metrics.getOperators().size(), is(expectedOperators.size()));
			assertThat(metrics.getOperators(), is(expectedOperators));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
