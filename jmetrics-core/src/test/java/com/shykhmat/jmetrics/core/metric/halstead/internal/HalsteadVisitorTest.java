package com.shykhmat.jmetrics.core.metric.halstead.internal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class HalsteadVisitorTest {

	@Test
	public void testEmptyPackageVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		assertPackageDeclaration("test-data/package/EmptyPackageName.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testSimplePackageVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put("package", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("test", 1);
		assertPackageDeclaration("test-data/package/SimplePackageName.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testComplexPackageVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 2);
		expectedOperators.put("package", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertPackageDeclaration("test-data/package/ComplexPackageName.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testPackageWithAdditionalSemicolonVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 2);
		expectedOperators.put("package", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertPackageDeclaration("test-data/package/PackageNameAndAdditionalSemicolon.test", expectedOperators,
				expectedOperands);
	}

	@Test
	public void testEmptyImportVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		assertPackageDeclaration("test-data/import/EmptyImport.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testSimpleImportVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 1);
		expectedOperators.put("import", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("test", 1);
		expectedOperands.put("Test", 1);
		assertPackageDeclaration("test-data/import/SimpleImport.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testComplexImportVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 3);
		expectedOperators.put("import", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		expectedOperands.put("Test", 1);
		assertPackageDeclaration("test-data/import/ComplexImport.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testImportAllVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 1);
		expectedOperators.put(".", 3);
		expectedOperators.put("*", 1);
		expectedOperators.put("import", 1);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 1);
		expectedOperands.put("shykhmat", 1);
		expectedOperands.put("test", 1);
		assertPackageDeclaration("test-data/import/ImportAll.test", expectedOperators, expectedOperands);
	}

	@Test
	public void testMultipleImportsVisit() {
		HashMap<String, Integer> expectedOperators = new HashMap<>();
		expectedOperators.put(";", 3);
		expectedOperators.put(".", 6);
		expectedOperators.put("*", 1);
		expectedOperators.put("import", 3);
		HashMap<String, Integer> expectedOperands = new HashMap<>();
		expectedOperands.put("com", 2);
		expectedOperands.put("shykhmat", 2);
		expectedOperands.put("test", 2);
		expectedOperands.put("Test1", 1);
		expectedOperands.put("Test2", 1);
		assertPackageDeclaration("test-data/import/MultipleImports.test", expectedOperators, expectedOperands);
	}

	private void assertPackageDeclaration(String fileName, HashMap<String, Integer> expectedOperators,
			HashMap<String, Integer> expectedOperands) {
		HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
		try {
			getCompilationUnit(fileName).accept(halsteadVisitor);
			assertThat(halsteadVisitor.getOperands().size(), is(expectedOperands.size()));
			assertThat(halsteadVisitor.getOperands(), is(expectedOperands));
			assertThat(halsteadVisitor.getOperators().size(), is(expectedOperators.size()));
			assertThat(halsteadVisitor.getOperators(), is(expectedOperators));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	private CompilationUnit getCompilationUnit(String filePath) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS11);
		parser.setSource(Resources.toString(Resources.getResource(filePath), Charsets.UTF_8).toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
