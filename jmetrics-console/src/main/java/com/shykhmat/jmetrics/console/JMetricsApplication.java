
package com.shykhmat.jmetrics.console;

import java.util.Scanner;

import com.shykhmat.jmetrics.core.api.JMetricsApi;
import com.shykhmat.jmetrics.core.visitor.VisitorException;

/**
 * Console application to collect metrics for java project and to generate Excel
 * report.
 */
public class JMetricsApplication {
    public static void main(String[] args) throws VisitorException {
        JMetricsApi jMetricsApi = new JMetricsApi();
        JMetricsApplicationProperties applicationProperties = new JMetricsApplicationProperties();
        if (applicationProperties.parse(args)) {
            String projectPath = applicationProperties.getProjectPath();
            String reportPath = applicationProperties.getReportPath();
            jMetricsApi.writeMetricsToExcel(projectPath, reportPath);
        }
        closeApplication();
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
