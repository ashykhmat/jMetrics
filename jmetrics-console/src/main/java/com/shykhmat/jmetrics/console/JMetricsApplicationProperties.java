package com.shykhmat.jmetrics.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command line arguments that application can process.
 */
public class JMetricsApplicationProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(JMetricsApplicationProperties.class);
    private static final String PROJECT_PATH_OPTION = "projectPath";
    private static final String REPORT_PATH_OPTION = "reportPath";
    private static final String HELP_OPTION = "help";

    private Options requiredOptions;
    private Options additionalOptions;

    private String projectPath;
    private String reportPath;

    public JMetricsApplicationProperties() {
        requiredOptions = new Options();
        Option projectPath = new Option(PROJECT_PATH_OPTION, true,
                "Specifies path to the folder, that contains java application to analyze");
        projectPath.setRequired(true);
        Option reportPath = new Option(REPORT_PATH_OPTION, true,
                "Specifies path to the folder, that will be used to store generated Excel report");
        reportPath.setRequired(true);
        requiredOptions.addOption(projectPath);
        requiredOptions.addOption(reportPath);
        additionalOptions = new Options();
        additionalOptions.addOption(new Option(HELP_OPTION, false, "Command to see application help information"));
    }

    /**
     * Method to collect application properties from command line arguments
     * 
     * @param args
     *            - specified command line arguments
     * @return true if arguments were parsed successfully, false in another case
     */
    public boolean parse(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(additionalOptions, args, true);
            if (line.hasOption(HELP_OPTION)) {
                printHelp();
                return false;
            }
            line = parser.parse(requiredOptions, args);
            projectPath = line.getOptionValue(PROJECT_PATH_OPTION);
            reportPath = line.getOptionValue(REPORT_PATH_OPTION);
            return true;
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            return false;
        }

    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options helpOptions = new Options();
        for (Option option : requiredOptions.getOptions()) {
            helpOptions.addOption(option);
        }
        for (Option option : additionalOptions.getOptions()) {
            helpOptions.addOption(option);
        }
        formatter.printHelp("Maintainability index calculator",
                "Read following instructions to work with the application", helpOptions, "Developed by Anton Shykhmat");
    }
}
