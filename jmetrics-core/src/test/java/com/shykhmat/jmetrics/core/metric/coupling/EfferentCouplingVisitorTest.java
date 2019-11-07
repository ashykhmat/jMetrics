package com.shykhmat.jmetrics.core.metric.coupling;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.parser.JavaParser;

public class EfferentCouplingVisitorTest {

	@Test
	public void testClassWithImportAll() {
		Set<String> expectedEfferentCouplingAll = new HashSet<>();
		expectedEfferentCouplingAll.add("com.shykhmat.a.A");
		expectedEfferentCouplingAll.add("com.shykhmat.b.B");
		expectedEfferentCouplingAll.add("com.shykhmat.c.C");
		expectedEfferentCouplingAll.add("com.shykhmat.generic.Generic");
		expectedEfferentCouplingAll.add("com.shykhmat.generic.GenericType");
		expectedEfferentCouplingAll.add("com.shykhmat.unused.Unused");
		expectedEfferentCouplingAll.add("com.shykhmat.d.D");
		expectedEfferentCouplingAll.add("com.shykhmat.hierarchy.Parent");
		expectedEfferentCouplingAll.add("com.shykhmat.hierarchy.Child");
		expectedEfferentCouplingAll.add("Undeclared");
		expectedEfferentCouplingAll.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingAll.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingAll.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingAll.add("Constants");
		expectedEfferentCouplingAll.add("Reference");
		expectedEfferentCouplingAll.add("com.shykhmat.all.a.*");
		expectedEfferentCouplingAll.add("com.shykhmat.all.b.*");
		expectedEfferentCouplingAll.add("ImportA");
		expectedEfferentCouplingAll.add("ImportB");
		expectedEfferentCouplingAll.add("org.junit.Assert");
		expectedEfferentCouplingAll.add("java.awt.Color");
		expectedEfferentCouplingAll.add("java.util.Base64");
		expectedEfferentCouplingAll.add("org.slf4j.Logger");
		expectedEfferentCouplingAll.add("org.slf4j.LoggerFactory");
		expectedEfferentCouplingAll.add("java.util.Calendar");
		expectedEfferentCouplingAll.add("ClassWithImportAll");
		expectedEfferentCouplingAll.add("com.shykhmat.common.IndexedColors");
		expectedEfferentCouplingAll.add("com.shykhmat.common.FillPatternType");
		Set<String> expectedEfferentCouplingUsed = new HashSet<>();
		expectedEfferentCouplingUsed.add("com.shykhmat.a.A");
		expectedEfferentCouplingUsed.add("com.shykhmat.b.B");
		expectedEfferentCouplingUsed.add("com.shykhmat.c.C");
		expectedEfferentCouplingUsed.add("com.shykhmat.generic.Generic");
		expectedEfferentCouplingUsed.add("com.shykhmat.generic.GenericType");
		expectedEfferentCouplingUsed.add("com.shykhmat.d.D");
		expectedEfferentCouplingUsed.add("com.shykhmat.hierarchy.Parent");
		expectedEfferentCouplingUsed.add("com.shykhmat.hierarchy.Child");
		expectedEfferentCouplingUsed.add("Undeclared");
		expectedEfferentCouplingUsed.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingUsed.add("Constants");
		expectedEfferentCouplingUsed.add("Reference");
		expectedEfferentCouplingUsed.add("ImportA");
		expectedEfferentCouplingUsed.add("ImportB");
		expectedEfferentCouplingUsed.add("org.junit.Assert");
		expectedEfferentCouplingUsed.add("java.util.Base64");
		expectedEfferentCouplingUsed.add("org.slf4j.Logger");
		expectedEfferentCouplingUsed.add("org.slf4j.LoggerFactory");
		expectedEfferentCouplingUsed.add("java.util.Calendar");
		expectedEfferentCouplingUsed.add("ClassWithImportAll");
		expectedEfferentCouplingUsed.add("com.shykhmat.common.IndexedColors");
		expectedEfferentCouplingUsed.add("com.shykhmat.common.FillPatternType");
		assertCalculations("test-data/class/ClassWithImportAll.test", "com.shykhmat.test.", CodePartType.CLASS,
				expectedEfferentCouplingAll, expectedEfferentCouplingUsed);
	}

	@Test
	public void testClassWithoutImportAll() {
		Set<String> expectedEfferentCouplingAll = new HashSet<>();
		expectedEfferentCouplingAll.add("com.shykhmat.a.A");
		expectedEfferentCouplingAll.add("com.shykhmat.b.B");
		expectedEfferentCouplingAll.add("com.shykhmat.c.C");
		expectedEfferentCouplingAll.add("com.shykhmat.generic.Generic");
		expectedEfferentCouplingAll.add("com.shykhmat.generic.GenericType");
		expectedEfferentCouplingAll.add("com.shykhmat.unused.Unused");
		expectedEfferentCouplingAll.add("com.shykhmat.d.D");
		expectedEfferentCouplingAll.add("com.shykhmat.hierarchy.Parent");
		expectedEfferentCouplingAll.add("com.shykhmat.hierarchy.Child");
		expectedEfferentCouplingAll.add("com.shykhmat.test.Undeclared");
		expectedEfferentCouplingAll.add("com.shykhmat.test.SamePackageMarkerAnnotation");
		expectedEfferentCouplingAll.add("com.shykhmat.test.SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingAll.add("com.shykhmat.test.SamePackageNormalAnnotation");
		expectedEfferentCouplingAll.add("com.shykhmat.test.Constants");
		expectedEfferentCouplingAll.add("com.shykhmat.test.Reference");
		Set<String> expectedEfferentCouplingUsed = new HashSet<>();
		expectedEfferentCouplingUsed.add("com.shykhmat.a.A");
		expectedEfferentCouplingUsed.add("com.shykhmat.b.B");
		expectedEfferentCouplingUsed.add("com.shykhmat.c.C");
		expectedEfferentCouplingUsed.add("com.shykhmat.generic.Generic");
		expectedEfferentCouplingUsed.add("com.shykhmat.generic.GenericType");
		expectedEfferentCouplingUsed.add("com.shykhmat.d.D");
		expectedEfferentCouplingUsed.add("com.shykhmat.hierarchy.Parent");
		expectedEfferentCouplingUsed.add("com.shykhmat.hierarchy.Child");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.Undeclared");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.SamePackageMarkerAnnotation");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.SamePackageNormalAnnotation");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.Constants");
		expectedEfferentCouplingUsed.add("com.shykhmat.test.Reference");
		assertCalculations("test-data/class/ClassWithoutImportAll.test", "com.shykhmat.test.", CodePartType.CLASS,
				expectedEfferentCouplingAll, expectedEfferentCouplingUsed);
	}

	@Test
	public void testInterface() {
		Set<String> expectedEfferentCouplingAll = new HashSet<>();
		expectedEfferentCouplingAll.add("com.shykhmat.a.A");
		expectedEfferentCouplingAll.add("com.shykhmat.unused.Unused");
		expectedEfferentCouplingAll.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingAll.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingAll.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingAll.add("Constants");
		expectedEfferentCouplingAll.add("Reference");
		expectedEfferentCouplingAll.add("com.shykhmat.all.a.*");
		expectedEfferentCouplingAll.add("com.shykhmat.all.b.*");
		expectedEfferentCouplingAll.add("ImportA");
		expectedEfferentCouplingAll.add("ImportB");
		Set<String> expectedEfferentCouplingUsed = new HashSet<>();
		expectedEfferentCouplingUsed.add("com.shykhmat.a.A");
		expectedEfferentCouplingUsed.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingUsed.add("Constants");
		expectedEfferentCouplingUsed.add("Reference");
		expectedEfferentCouplingUsed.add("ImportA");
		expectedEfferentCouplingUsed.add("ImportB");
		assertCalculations("test-data/class/InterfaceWithImportAll.test", "com.shykhmat.test.", CodePartType.INTERFACE,
				expectedEfferentCouplingAll, expectedEfferentCouplingUsed);
	}

	@Test
	public void testEnum() {
		Set<String> expectedEfferentCouplingAll = new HashSet<>();
		expectedEfferentCouplingAll.add("com.shykhmat.a.A");
		expectedEfferentCouplingAll.add("com.shykhmat.unused.Unused");
		expectedEfferentCouplingAll.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingAll.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingAll.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingAll.add("Constants");
		expectedEfferentCouplingAll.add("Reference");
		expectedEfferentCouplingAll.add("com.shykhmat.all.a.*");
		expectedEfferentCouplingAll.add("com.shykhmat.all.b.*");
		expectedEfferentCouplingAll.add("ImportA");
		expectedEfferentCouplingAll.add("ImportB");
		Set<String> expectedEfferentCouplingUsed = new HashSet<>();
		expectedEfferentCouplingUsed.add("com.shykhmat.a.A");
		expectedEfferentCouplingUsed.add("SamePackageMarkerAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageSingleMemberAnnotation");
		expectedEfferentCouplingUsed.add("SamePackageNormalAnnotation");
		expectedEfferentCouplingUsed.add("Constants");
		expectedEfferentCouplingUsed.add("Reference");
		expectedEfferentCouplingUsed.add("ImportA");
		expectedEfferentCouplingUsed.add("ImportB");
		assertCalculations("test-data/class/EnumWithImportAll.test", "com.shykhmat.test.", CodePartType.ENUM,
				expectedEfferentCouplingAll, expectedEfferentCouplingUsed);
	}

	@Test
	public void testAnnotation() {
		Set<String> expectedEfferentCouplingAll = new HashSet<>();
		expectedEfferentCouplingAll.add("com.shykhmat.a.A");
		expectedEfferentCouplingAll.add("com.shykhmat.unused.Unused");
		expectedEfferentCouplingAll.add("java.lang.annotation.Retention");
		expectedEfferentCouplingAll.add("java.lang.annotation.RetentionPolicy");
		expectedEfferentCouplingAll.add("java.lang.annotation.Target");
		expectedEfferentCouplingAll.add("java.lang.annotation.ElementType");
		Set<String> expectedEfferentCouplingUsed = new HashSet<>();
		expectedEfferentCouplingUsed.add("com.shykhmat.a.A");
		expectedEfferentCouplingUsed.add("java.lang.annotation.Retention");
		expectedEfferentCouplingUsed.add("java.lang.annotation.RetentionPolicy");
		expectedEfferentCouplingUsed.add("java.lang.annotation.Target");
		expectedEfferentCouplingUsed.add("java.lang.annotation.ElementType");
		assertCalculations("test-data/class/Annotation.test", "com.shykhmat.test.", CodePartType.ANNOTATION,
				expectedEfferentCouplingAll, expectedEfferentCouplingUsed);
	}

	@Test
	public void testClassWithoutImports() {
		assertCalculations("test-data/class/Class.test", "default.", CodePartType.CLASS, Collections.emptySet(),
				Collections.emptySet());
	}

	private void assertCalculations(String filePath, String packageName, CodePartType type,
			Set<String> expectedEfferentCouplingAll, Set<String> expectedEfferentCouplingUsed) {
		try {
			ASTNode codeToTest = JavaParser.getCodePart(
					Resources.toString(Resources.getResource(filePath), Charsets.UTF_8).toCharArray(), type);
			EfferentCouplingVisitor couplingVisitor = new EfferentCouplingVisitor(packageName);
			codeToTest.accept(couplingVisitor);
			assertThat(couplingVisitor.getEfferentCouplingAll().size(), is(expectedEfferentCouplingAll.size()));
			assertThat(couplingVisitor.getEfferentCouplingAll(), is(expectedEfferentCouplingAll));
			assertThat(couplingVisitor.getEfferentCouplingUsed().size(), is(expectedEfferentCouplingUsed.size()));
			assertThat(couplingVisitor.getEfferentCouplingUsed(), is(expectedEfferentCouplingUsed));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
