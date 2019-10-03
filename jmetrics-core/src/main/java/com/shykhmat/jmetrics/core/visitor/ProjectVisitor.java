
package com.shykhmat.jmetrics.core.visitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.shykhmat.jmetrics.core.metric.CompositeMetric;
import com.shykhmat.jmetrics.core.metric.cyclomatic.CyclomaticComplexityMetric;
import com.shykhmat.jmetrics.core.metric.halstead.HalsteadVolumeMetric;
import com.shykhmat.jmetrics.core.metric.loc.LinesOfCodeMetric;
import com.shykhmat.jmetrics.core.metric.maintainability.MaintainabilityIndexMetric;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Method to visit Java Project and calculate metrics for it.
 */
public class ProjectVisitor {
	public ProjectReport visit(String pathToProject) throws VisitorException {
		ProjectReport project = new ProjectReport(Paths.get(pathToProject).getFileName().toString());
		try {
			processJavaClasses(new File(pathToProject), project);
		} catch (IOException | ParseException e) {
			throw new VisitorException(e);
		}
		return project;
	}

	private void processJavaClasses(File rootFile, ProjectReport project) throws IOException, ParseException {
		for (File childFile : rootFile.listFiles()) {
			if (childFile.isDirectory()) {
				processJavaClasses(childFile, project);
			} else if (childFile.getName().endsWith(".java")) {
				FileInputStream fileInputStream = new FileInputStream(childFile);
				try {
					CompilationUnit compilationUnit = JavaParser.parse(fileInputStream);
					new ClassVisitor(createCompositeMetric()).visit(compilationUnit, project.getClasses());
				} finally {
					fileInputStream.close();
				}
			}
		}
	}

	private CompositeMetric createCompositeMetric() {
		HalsteadVolumeMetric halsteadVolumeMetric = new HalsteadVolumeMetric();
		CyclomaticComplexityMetric cyclomaticComplexityMetric = new CyclomaticComplexityMetric();
		LinesOfCodeMetric linesOfCodeMetric = new LinesOfCodeMetric();
		MaintainabilityIndexMetric maintainabilityIndexMetric = new MaintainabilityIndexMetric();
		return new CompositeMetric(halsteadVolumeMetric, cyclomaticComplexityMetric, linesOfCodeMetric,
				maintainabilityIndexMetric);
	}
}
