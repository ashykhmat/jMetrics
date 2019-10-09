package com.shykhmat.jmetrics.core.metric.cyclomatic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.parser.JavaParser;

public class CyclomaticComplexityCalculatorTest {
	@Test
	public void testLargeClass() throws IOException {
		assertCalculations("test-data/class/LargeClass.test", CodePartType.CLASS, 17);
	}
	
	private void assertCalculations(String filePath, CodePartType type, Integer expectedCyclomaticComplexity) {
		CyclomaticComplexityCalculator cyclomaticComplexityCalculator = new CyclomaticComplexityCalculator();
		try {
			ASTNode codeToTest = JavaParser.getCodePart(
					Resources.toString(Resources.getResource(filePath), Charsets.UTF_8).toCharArray(), type);
			assertEquals(expectedCyclomaticComplexity, cyclomaticComplexityCalculator.calculateMetric(codeToTest));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
