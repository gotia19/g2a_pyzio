package com.capgemini.stk.framework.reporter.allure;

import com.capgemini.stk.framework.basetest.StepLogger;
import io.qameta.allure.junit4.AllureJunit4;
import io.qameta.allure.model.Status;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.util.ResultsUtils.getStatus;

@RunListener.ThreadSafe
public class AllureJunit4RunListener extends AllureJunit4 {
    @Override
    public void testFailure(Failure failure) {
        Failure newFailure = failure;
        try {
            newFailure = handleKnownIssue(failure);
        }
        catch (Throwable ignored) {
        }
        if (null != newFailure.getMessage()) {
            String msg = newFailure.getMessage().toLowerCase();
            if (msg.contains("[temporary off]") || msg.contains("selenium grid")) {
                super.testAssumptionFailure(newFailure);
                return;
            }
        }
        super.testFailure(newFailure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        Failure newFailure = failure;
        try {
            newFailure = handleKnownIssue(failure);
        }
        catch (Throwable ignored) {
        }
        super.testAssumptionFailure(newFailure);
    }

    private Failure handleKnownIssue(Failure failure) {
        Throwable   exception   = failure.getException();
        Description description = failure.getDescription();
        String      message     = exception.getMessage();
        if (message == null) {
            message = exception.toString();
        }
        String test   = description.getMethodName();
        String grp    = "";
        Status status = getStatus(exception).get();
        // Check if there is JIRA
        for (Annotation annotation : description.getAnnotations()) {
            String testAnnotation = annotation.toString();
            if (testAnnotation.contains(".TmsLink(value=JIRA")) {
                grp = testAnnotation.substring(testAnnotation.indexOf("=")).replace("=", "").replace(")", "");
                break;
            }
        }
        String     bugsFileName = "src/resources/bugs/bugs.json";
        JSONParser jsonParser   = new JSONParser();
        try (FileReader reader = new FileReader(bugsFileName)) {
            JSONArray bugsList = (JSONArray) jsonParser.parse(reader);
            for (Object bug : bugsList) {
                JSONObject   bugJSON       = (JSONObject) bug;
                JSONArray    testsJSON     = (JSONArray) bugJSON.get("tests");
                List<String> testsJSONList = new ArrayList<>();
                for (Object o : testsJSON) {
                    testsJSONList.add((String) o);
                }
                String issueJSON       = (String) bugJSON.get("issue");
                String issueStatusJSON = MessageFormat.format("[{0}]", bugJSON.get("issueStatus")).toUpperCase();
                String messageJSON     = (String) bugJSON.get("message");

                if ((isStringContainsPartFromList(test, testsJSONList) || testsJSONList.contains(grp) || testsJSONList
                        .contains("*")) && message.contains(messageJSON)) {
                    StepLogger.issue(issueJSON);
                    String errorMessage = MessageFormat.format("{0} {1}", issueStatusJSON, message);
                    if (status == Status.FAILED) {
                        return new Failure(description, new AssertionError(errorMessage, exception));
                    }
                    return new Failure(description, new Exception(errorMessage, exception));
                }
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return failure;
    }

    // Fix for parametrized smoke tests which change method name by add some at the end
    private boolean isStringContainsPartFromList(String text, List<String> parts) {
        if (text != null && !parts.isEmpty()) {
            for (String part : parts) {
                if (text.contains(part)) {
                    return true;
                }
            }
        }
        return false;
    }
}