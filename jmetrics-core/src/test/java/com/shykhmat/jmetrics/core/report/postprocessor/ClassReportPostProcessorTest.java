package com.shykhmat.jmetrics.core.report.postprocessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.shykhmat.jmetrics.core.report.ClassReport;

public class ClassReportPostProcessorTest {
	private ClassReportPostProcessor classReportPostProcessor = new ClassReportPostProcessor();

	@Test
	public void testProcessing() {
		ClassReport a = new ClassReport("com.shykhmat.A");
		a.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.B", "default.C")));
		ClassReport b = new ClassReport("com.shykhmat.B");
		b.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("C", "com.shykhmat.all.*")));
		ClassReport c = new ClassReport("default.C");
		ClassReport d = new ClassReport("com.shykhmat.D");
		d.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("All", "com.shykhmat.all.*")));
		ClassReport e = new ClassReport("com.shykhmat.E");
		e.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("All", "com.shykhmat.all.*", "Unknown",
				"com.shykhmat.unknown.*", "D", "com.shykhmat.*")));
		Set<ClassReport> inputData = new HashSet<>(Arrays.asList(a, b, c, d, e));
		classReportPostProcessor.process(inputData);
		ClassReport expectedA = new ClassReport("com.shykhmat.A");
		expectedA.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.B", "default.C")));
		expectedA.setInstability(1.);
		ClassReport expectedB = new ClassReport("com.shykhmat.B");
		expectedB.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("default.C")));
		expectedB.setAfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.A")));
		expectedB.setInstability(0.5);
		ClassReport expectedC = new ClassReport("default.C");
		expectedC.setAfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.A", "com.shykhmat.B")));
		expectedC.setInstability(0.);
		ClassReport expectedD = new ClassReport("com.shykhmat.D");
		expectedD.setAfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.E")));
		expectedD.setEfferentCouplingUsed(new HashSet<>(Arrays.asList("com.shykhmat.all.All")));
		expectedD.setInstability(0.5);
		ClassReport expectedE = new ClassReport("com.shykhmat.E");
		expectedE.setEfferentCouplingUsed(
				new HashSet<>(Arrays.asList("com.shykhmat.all.All", "Unknown", "com.shykhmat.D")));
		expectedE.setInstability(1.);
		Set<ClassReport> expectedData = new HashSet<>(
				Arrays.asList(expectedA, expectedB, expectedC, expectedD, expectedE));
		assertThat(inputData.size(), is(expectedData.size()));
		assertReports(inputData, expectedData);
	}

	private void assertReports(Set<ClassReport> inputData, Set<ClassReport> expectedData) {
		expectedData.forEach(expectedClassReport -> {
			ClassReport actualClassReport = inputData.stream()
					.filter(classReport -> expectedClassReport.getName().equals(classReport.getName())).findFirst()
					.orElse(null);
			assertNotNull(actualClassReport);
			Set<String> actualEfferentCouplingUsed = actualClassReport.getEfferentCouplingUsed();
			assertThat(actualEfferentCouplingUsed.size(), is(expectedClassReport.getEfferentCouplingUsed().size()));
			assertThat(actualEfferentCouplingUsed, is(expectedClassReport.getEfferentCouplingUsed()));
			Set<String> actualAfferentCouplingUsed = actualClassReport.getAfferentCouplingUsed();
			assertThat(actualAfferentCouplingUsed.size(), is(expectedClassReport.getAfferentCouplingUsed().size()));
			assertThat(actualAfferentCouplingUsed, is(expectedClassReport.getAfferentCouplingUsed()));
			assertThat(actualClassReport.getInstability(), is(expectedClassReport.getInstability()));
		});
	}

}
