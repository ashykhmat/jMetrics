
package com.shykhmat.jmetrics.console;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.api.JMetricsApi;
import com.shykhmat.jmetrics.core.report.ProjectReport;
import com.shykhmat.jmetrics.core.visitor.VisitorException;

/**
 * Console application to collect metrics for java project and to generate Excel
 * report.
 */
public class JMetricsApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMetricsApplication.class);

    public static void main(String[] args) throws VisitorException {
        JMetricsApi jMetricsApi = new JMetricsApi();
        JMetricsApplicationProperties applicationProperties = new JMetricsApplicationProperties();
        if (applicationProperties.parse(args)) {
            String projectPath = applicationProperties.getProjectPath();
            String reportPath = applicationProperties.getReportPath();
            reportPath = fixReportPath(projectPath, reportPath);
            LOGGER.info("Calculating metrics for project: " + projectPath);
            ProjectReport project = jMetricsApi.calculateMetrics(projectPath);
            LOGGER.info("Writing report file: " + reportPath);
            boolean isSuccessful = jMetricsApi.writeMetricsToExcel(reportPath, project);
            if (isSuccessful) {
                LOGGER.info("Metrics were written successfully");
            }
        }
        closeApplication();
    }

    private static String fixReportPath(String projectPath, String reportPath) {
        if (!reportPath.endsWith(".xlsx")) {
            File projectRoot = new File(projectPath);
            String projectName = projectRoot.getName();
            if (!reportPath.endsWith(File.separator)) {
                projectName = File.separator + projectName;
            }
            reportPath += projectName + ".xlsx";
        }
        return reportPath;
    }

    private static void closeApplication() {
        System.out.println("Press \"ENTER\" to exit application...");
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            scanner.nextLine();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
