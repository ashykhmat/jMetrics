
package com.shykhmat.jmetrics.core.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.ASTNode;

import com.shykhmat.jmetrics.core.metric.CodePartType;
import com.shykhmat.jmetrics.core.parser.JavaParser;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Method to visit Java Project and calculate metrics for it.
 */
public class ProjectVisitor {
	public ProjectReport visit(String pathToProject) throws VisitorException {
		ProjectReport project = new ProjectReport(Paths.get(pathToProject).getFileName().toString());
		try {
			processJavaClasses(new File(pathToProject), project);
		} catch (IOException e) {
			throw new VisitorException(e);
		}
		return project;
	}

	private void processJavaClasses(File rootFile, ProjectReport project) throws IOException {
		for (File childFile : rootFile.listFiles()) {
			if (childFile.isDirectory()) {
				processJavaClasses(childFile, project);
			} else if (childFile.getName().endsWith(".java")) {
				ASTNode compilationUnit = JavaParser.getCodePart(
						new String(Files.readAllBytes(Paths.get(childFile.getAbsolutePath()))).toCharArray(),
						CodePartType.CLASS);
				CompilationUnitVisitor compilationUnitVisitor = new CompilationUnitVisitor();
				compilationUnitVisitor.visit(compilationUnit);
				project.getClasses().addAll(compilationUnitVisitor.getClassesReports());
			}
		}
	}
}
