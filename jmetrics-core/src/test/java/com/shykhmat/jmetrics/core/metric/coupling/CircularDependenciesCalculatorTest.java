package com.shykhmat.jmetrics.core.metric.coupling;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.PackageReport;

public class CircularDependenciesCalculatorTest {

	private CircularDependenciesCalculator circularDependenciesCalculator = new CircularDependenciesCalculator();

	@Test
	public void testPackagesDependencies() {
		PackageReport root = new PackageReport("root");
		root.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("a", "b", "c", "d", "e")));
		PackageReport a = new PackageReport("a");
		a.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f", "b")));
		PackageReport b = new PackageReport("b");
		b.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f")));
		PackageReport c = new PackageReport("c");
		c.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("root")));
		PackageReport d = new PackageReport("d");
		d.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("a")));
		PackageReport e = new PackageReport("e");
		e.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f")));
		PackageReport f = new PackageReport("f");
		f.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("b")));
		Set<PackageReport> packages = new HashSet<>(Arrays.asList(root, a, b, c, d, e, f));
		Set<String> actualCircularDependencies = circularDependenciesCalculator
				.calculatePackagesCircularDependencies(packages);
		Set<String> expectedCircularDependencies = new TreeSet<>();
		expectedCircularDependencies.add("root->c->root");
		expectedCircularDependencies.add("c->root->c");
		expectedCircularDependencies.add("b->f->b");
		expectedCircularDependencies.add("f->b->f");
		assertCircularDependencies(actualCircularDependencies, expectedCircularDependencies);
	}

	@Test
	public void testClassesDependencies() {
		ClassReport root = new ClassReport("root");
		root.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("a", "b", "c", "d", "e")));
		ClassReport a = new ClassReport("a");
		a.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f", "b")));
		ClassReport b = new ClassReport("b");
		b.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f")));
		ClassReport c = new ClassReport("c");
		c.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("root")));
		ClassReport d = new ClassReport("d");
		d.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("a")));
		ClassReport e = new ClassReport("e");
		e.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("f")));
		ClassReport f = new ClassReport("f");
		f.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("b")));
		Set<ClassReport> classes = new HashSet<>(Arrays.asList(root, a, b, c, d, e, f));
		Set<String> actualCircularDependencies = circularDependenciesCalculator
				.calculateClassesCircularDependencies(classes);
		Set<String> expectedCircularDependencies = new TreeSet<>();
		expectedCircularDependencies.add("root->c->root");
		expectedCircularDependencies.add("c->root->c");
		expectedCircularDependencies.add("b->f->b");
		expectedCircularDependencies.add("f->b->f");
		assertCircularDependencies(actualCircularDependencies, expectedCircularDependencies);
	}

	private void assertCircularDependencies(Set<String> actualCircularDependencies,
			Set<String> expectedCircularDependencies) {
		assertNotNull(actualCircularDependencies);
		assertThat(actualCircularDependencies.size(), is(expectedCircularDependencies.size()));
		assertThat(actualCircularDependencies, is(expectedCircularDependencies));
	}
}
