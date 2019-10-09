package com.shykhmat.jmetrics.core.metric.loc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.parser.JavaParser;

public class LinesOfCodeCalculatorTest {

	@Test
	public void testLargeClass() throws IOException {
		assertCalculations("test-data/class/LargeClass.test", CodePartType.CLASS, 46);
	}

	@Test
	public void testClass() throws IOException {
		assertCalculations("test-data/class/Class.test", CodePartType.CLASS, 7);
	}

	@Test
	public void testEnum() throws IOException {
		assertCalculations("test-data/class/Enum.test", CodePartType.ENUM, 1);
	}

	@Test
	public void testEnumWithMethod() throws IOException {
		assertCalculations("test-data/class/EnumWithMethod.test", CodePartType.ENUM, 6);
	}

	@Test
	public void testMethod() throws IOException {
		// 7 is expected, because Java parser adds class declaration for method (only in
		// tests).
		assertCalculations("test-data/class/Method.test", CodePartType.METHOD, 7);
	}

	private void assertCalculations(String filePath, CodePartType type, Integer expectedLoc) {
		LinesOfCodeCalculator linesOfCodeCalculator = new LinesOfCodeCalculator();
		try {
			ASTNode codeToTest = JavaParser.getCodePart(
					Resources.toString(Resources.getResource(filePath), Charsets.UTF_8).toCharArray(), type);
			assertEquals(expectedLoc, linesOfCodeCalculator.calculateMetric(codeToTest));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
