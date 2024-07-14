package com.capgemini.stk.framework.reporter.allure;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.stk.framework.basetest.StepLogger;
import com.capgemini.stk.framework.reporter.JiraCSVGenerator;
import io.qameta.allure.AllureResultsWriteException;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.*;
import io.qameta.allure.util.PropertiesUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AllureAttachmentsRemover implements TestLifecycleListener {
    private static final Properties   properties       = PropertiesUtils.loadAllureProperties();
    private static final Path         outputDirectory  = Paths
            .get(properties.getProperty("allure.results.directory", "allure-results"));
    private static final String       patternForRemove = properties.getProperty("allure.report.remove.attachments", "");
    private static final List<String> RESTRICTED_DATA  = Arrays.asList("password", "api-key");

    @Override
    public void beforeTestWrite(TestResult testResult) {
        JiraCSVGenerator.createTest(testResult);
        if (patternForRemove.equals("")) {
            return;
        }

        if (testResult.getStatus() == Status.PASSED) {
            StepLogger.info("Attachment should be removed");
            BFLogger.logInfo("==== Remove Attachments with pattern: " + patternForRemove);
            removeMatchingAttachments(testResult.getAttachments());
            testResult.getSteps().stream().flatMap(this::getSubSteps).map(StepResult::getAttachments)
                      .forEach(this::removeMatchingAttachments);

            testResult.getSteps().stream().flatMap(this::getSubSteps).forEach(this::renameScreenshotStep);
        }
        hidePasswordsRecursion(testResult.getSteps());
    }

    private void hidePasswordsRecursion(List<StepResult> steps) {
        for (StepResult step : steps) {
            steps.stream().map(StepResult::getParameters).forEach(this::hidePassword);
            if (!step.getSteps().isEmpty()) {
                hidePasswordsRecursion(step.getSteps());
            }
        }
    }

    private void hidePassword(List<Parameter> parameterList) {
        for (Parameter parameter : parameterList) {
            if (parameter.getName().contains("password")) {
                parameter.setValue("*****************");
            }
            else if (StringUtils.indexOfAny(parameter.getValue().toLowerCase(),
                                            (String[]) RESTRICTED_DATA.toArray()) != -1) {
                parameter.setValue("#CONTAINS RESTRICTED DATA#");
            }
        }
    }

    private void renameScreenshotStep(StepResult step) {
        if (patternForRemove.equals("Screenshot")) {
            // Steps with name "--Screenshot--" will be changed into "--Screenshot removed--"
            BFLogger.logInfo("[STEP] " + step.getName());
            if (step.getName().contains("--Screenshot--")) {
                step.setName("--Screenshot removed--");
            }
        }
    }

    private Stream<StepResult> getSubSteps(StepResult step) {
        if (step.getSteps().isEmpty()) {
            return Stream.of(step);
        }
        return step.getSteps().stream().flatMap(this::getSubSteps);
    }

    private void removeMatchingAttachments(Collection<Attachment> attachments) {

        List<Attachment> attachmentsToRemove = attachments.stream().filter(attachment -> attachment.getName().matches(
                patternForRemove)).collect(toList());

        attachmentsToRemove.forEach(this::deleteAttachmentFile);
        attachments.removeAll(attachmentsToRemove);
    }

    private void deleteAttachmentFile(Attachment attachment) {
        Path filePath = outputDirectory.resolve(attachment.getSource());
        try {
            Files.delete(filePath);
        }
        catch (IOException e) {
            throw new AllureResultsWriteException("Could not remove Allure attachment", e);
        }
    }
}
