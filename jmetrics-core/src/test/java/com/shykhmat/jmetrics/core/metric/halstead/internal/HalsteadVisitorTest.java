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
    public void testEmptyPackageDeclarationVisit() {
        HashMap<String, Integer> expectedOperators = new HashMap<>();
        HashMap<String, Integer> expectedOperands = new HashMap<>();
        assertPackageDeclaration("test-data/ClassWithoutPackageName.test", expectedOperators, expectedOperands);
    }

    @Test
    public void testSimplePackageDeclarationVisit() {
        HashMap<String, Integer> expectedOperators = new HashMap<>();
        expectedOperators.put(";", 1);
        expectedOperators.put("package", 1);
        HashMap<String, Integer> expectedOperands = new HashMap<>();
        expectedOperands.put("test", 1);
        assertPackageDeclaration("test-data/ClassWithSimplePackageName.test", expectedOperators, expectedOperands);
    }

    @Test
    public void testRegularPackageDeclarationVisit() {
        HashMap<String, Integer> expectedOperators = new HashMap<>();
        expectedOperators.put(";", 1);
        expectedOperators.put(".", 2);
        expectedOperators.put("package", 1);
        HashMap<String, Integer> expectedOperands = new HashMap<>();
        expectedOperands.put("com", 1);
        expectedOperands.put("shykhmat", 1);
        expectedOperands.put("test", 1);
        assertPackageDeclaration("test-data/ClassWithPackageName.test", expectedOperators, expectedOperands);
    }

    @Test
    public void testPackageDeclarationWithAdditionalSemicolonVisit() {
        HashMap<String, Integer> expectedOperators = new HashMap<>();
        expectedOperators.put(";", 1);
        expectedOperators.put(".", 2);
        expectedOperators.put("package", 1);
        HashMap<String, Integer> expectedOperands = new HashMap<>();
        expectedOperands.put("com", 1);
        expectedOperands.put("shykhmat", 1);
        expectedOperands.put("test", 1);
        assertPackageDeclaration("test-data/ClassWithPackageNameAndAdditionalSemicolon.test", expectedOperators,
                expectedOperands);
    }

    private void assertPackageDeclaration(String fileName, HashMap<String, Integer> expectedOperators,
            HashMap<String, Integer> expectedOperands) {
        HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
        try {
            getCompilationUnit(fileName).accept(halsteadVisitor);
            assertThat(halsteadVisitor.getOperands().size(), is(expectedOperands.size()));
            assertThat(halsteadVisitor.getOperands(), is(expectedOperands));
            System.out.println("EXPECTED: " + expectedOperators);
            System.out.println("ACTUAL: " + halsteadVisitor.getOperators());
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
