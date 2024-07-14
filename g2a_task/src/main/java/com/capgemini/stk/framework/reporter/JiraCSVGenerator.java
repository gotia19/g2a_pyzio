package com.capgemini.stk.framework.reporter;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class JiraCSVGenerator {
    private static final String         JIRA_CSV_FILE = "target/allure-results/jira-test.csv";
    public static final  List<String>   stepsList     = new ArrayList<>();
    public static        boolean        CSVFlagWasRun;
    private static       CSVPrinter     csvPrinter;
    public static        String         summary       = "";
    private static final String         issueType     = "Test";
    public static        String         description   = "";
    private static       BufferedWriter writer;

    private static void createCSVFileWithHeaders(String[] headersList) throws IOException {
        if (System.getProperty("csv", "disabled").equals("disabled")) {
            return;
        }
        deleteOldCSVFile();
        File jiraCSVFile = new File(JIRA_CSV_FILE);
        if (!jiraCSVFile.exists()) {
            jiraCSVFile.getParentFile().mkdirs();
            jiraCSVFile.createNewFile();
            writer     = Files.newBufferedWriter(Paths.get(JIRA_CSV_FILE));
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headersList));
            csvPrinter.flush();
        }
    }

    private static void cleanAllData() {
        if (System.getProperty("csv", "disabled").equals("disabled")) {
            return;
        }
        stepsList.clear();
        summary     = "";
        description = "";
    }

    public static void createCSVFileBasicSmoke() throws IOException {
        if (System.getProperty("csv", "disabled").equals("disabled")) {
            return;
        }
        String[] listOfHeaders = {"Summary", "Issue Type", "Description"};
        createCSVFileWithHeaders(listOfHeaders);
    }

    private static void deleteOldCSVFile() {
        CSVFlagWasRun = true;
        BFLogger.logInfo("Start - Old file allure environments deleted");
        File environmentFile = new File(JIRA_CSV_FILE);
        environmentFile.delete();
        assertFalse(environmentFile.exists());
        BFLogger.logInfo("Old CSV file deleted");
    }

    public static void createTest(TestResult testResult) {

        if (System.getProperty("csv", "disabled").equals("disabled")) {
            return;
        }
        if (testResult.getStatus() == Status.PASSED) {
            String summaryReg = "Open menu";
            for (String x : stepsList) {
                if (x.contains(summaryReg)) {
                    summary = x.substring(x.lastIndexOf(summaryReg) + summaryReg.length());
                    break;
                }
            }
            description = "Open dialog" + summary + " and check if:\\\\";
            for (String x : stepsList) {
                if (x.contains("Element") || x.contains("expected")) {
                    description += x;
                    description += "\\\\";
                }
            }
            try {
                csvPrinter.printRecord("Check visibility of elements for" + summary, issueType,
                                       description.replace("[INFO]", ""));
                csvPrinter.flush();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        cleanAllData();
    }
}
