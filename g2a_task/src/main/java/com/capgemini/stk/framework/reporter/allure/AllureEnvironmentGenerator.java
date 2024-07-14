package com.capgemini.stk.framework.reporter.allure;

import com.capgemini.mrchecker.test.core.logger.BFLogger;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;

public class AllureEnvironmentGenerator {
    private static final String environmentFilePath = "target/allure-results/environment.properties";

    public static void deleteEnvironmentFile() {
        BFLogger.logInfo("Start - Old file allure environments deleted");
        File environmentFile = new File(environmentFilePath);
        environmentFile.delete();
        assertFalse("Environment file still exists", environmentFile.exists());
        BFLogger.logInfo("Old file allure environments deleted");
    }

    public static void addEnvironmentVariable(String variable) throws IOException {
        BFLogger.logInfo("Add Allure environment variable: " + variable);
        File environmentFile = new File(environmentFilePath);
        if (!environmentFile.exists()) {
            environmentFile.getParentFile().mkdirs();
            environmentFile.createNewFile();
        }
        Writer writer;
        writer = new BufferedWriter(new FileWriter(environmentFile, true));
        writer.write(variable + "\n");
        writer.close();
    }

    public static void addEnvironmentVariableIfNotExist(String variable) throws IOException {
        BFLogger.logInfo("Add Allure environment variable: " + variable);
        File environmentFile = new File(environmentFilePath);

        if (!environmentFile.exists()) {
            environmentFile.getParentFile().mkdirs();
            environmentFile.createNewFile();
        }

        Scanner scanner = new Scanner(environmentFile);
        boolean exists  = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(variable)) {
                exists = true;
                break;
            }
        }
        scanner.close();
        if (!exists) {
            Writer writer;
            writer = new BufferedWriter(new FileWriter(environmentFile, true));
            writer.write(variable + "\n");
            writer.close();
        }
    }

    public static void addOrUpdateEnvironmentVariable(String variable) throws IOException {
        BFLogger.logInfo("Add Allure environment variable: " + variable);
        File environmentFile = new File(environmentFilePath);

        if (!environmentFile.exists()) {
            environmentFile.getParentFile().mkdirs();
            environmentFile.createNewFile();
        }

        Scanner scanner = new Scanner(environmentFile);
        boolean exists  = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(variable)) {
                line   = variable + "\n";
                exists = true;
                break;
            }
        }
        scanner.close();
        if (!exists) {
            Writer writer;
            writer = new BufferedWriter(new FileWriter(environmentFile, true));
            writer.write(variable + "\n");
            writer.close();
        }
    }
}