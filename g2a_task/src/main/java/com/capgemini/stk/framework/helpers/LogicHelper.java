package com.capgemini.stk.framework.helpers;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.stk.framework.basetest.StepLogger;
import com.capgemini.stk.framework.reporter.allure.AllureEnvironmentGenerator;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;

import static com.capgemini.stk.framework.helpers.XMLHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public class LogicHelper {
    private static final HashMap<Long, String> adminRoles = new HashMap<>();
    private static final String                debugParam = System.getProperty("debug");
    private static final boolean               isDebug    = null != debugParam && debugParam.equalsIgnoreCase("true");

    static {
        //https://devstack.vwgroup.com/jira/browse/GRP-2982
        //W - Wholesaler/Importer
        //D - Dealer
        //S - Supplier/Third Party
        //A - ALL
        adminRoles.put(1L, "A");        //1 --> all Org/Sub-Types --> Managing Director
        adminRoles.put(2L, "W");        //2 --> Importer ONLY --> Wholesale Administrator
        adminRoles.put(22L,
                       "A");       //22 --> all Org/Sub-Types --> User Administrator, Local Administrator, Sales Local Administrator
        adminRoles.put(27L, "D");       //27 --> Dealer Only --> Aftersales Local Administrator / Local Administrator
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static boolean checkUserAdminRole(long roleID, String contextTypeChar) {
        if (adminRoles.containsKey(roleID)) {
            return adminRoles.get(roleID).contains(contextTypeChar) || adminRoles.get(roleID).contains("A");
        }
        return false;
    }

    public static void setRunProperties() {
        try {
            AllureEnvironmentGenerator.addEnvironmentVariableIfNotExist("Environment=" + System.getProperty("env"));
        }
        catch (IOException ignored) {
        }
    }

    public static void handleAPIError(Response response, String errorType) {
        String faultString = getFaultString(response);
        String faultReason = getFaultReason(response);
        if (!faultString.isEmpty() && response.getStatusCode() != 200) {
            StringBuilder errorText = new StringBuilder(faultString);
            if (faultReason.contains("[Error]")) {
                String operating = faultReason;
                while (!operating.isEmpty()) {
                    if (operating.contains("[Error]")) {
                        operating = operating.substring(operating.indexOf("[Error]"));
                        String subMessage = operating.substring(0, operating.indexOf("!") + 1);
                        errorText.append("\n").append(subMessage);
                        operating = operating.replace(subMessage, "");
                    }
                    else {
                        operating = "";
                    }
                }
            }
            switch (errorType.toLowerCase()) {
                case "fail":
                    fail(errorText.toString());
                    break;
                case "skip":
                default:
                    assumeTrue(errorText.toString(), false);
                    break;
            }
        }
    }

    public static void verifyResponseSuccess(Response response) {
        handleAPIError(response, "fail");
        assertEquals("Incorrect status code", 200, response.getStatusCode());
        StepLogger.step("Status code: " + response.getStatusCode());
    }

    public static String getErrorMessage(Response response) {

        String        faultReason = getFaultReason(response);
        StringBuilder errorText   = new StringBuilder();

        if (faultReason.contains("[Error]")) {
            String operating = faultReason;
            while (!operating.isEmpty()) {
                if (operating.contains("[Error]")) {
                    operating = operating.substring(operating.indexOf("[Error]"));
                    String subMessage = operating.substring(0, operating.indexOf("!") + 1);
                    errorText.append(subMessage).append("\n");
                    operating = operating.replace(subMessage, "");
                }
                else {
                    operating = "";
                }
            }
        }

        return errorText.toString();
    }

    public static void verifyResponseElementLengthTest(Response response, String expectedMessage) {
        assertEquals("Incorrect status code:" + response.getStatusCode(), 500, response.getStatusCode());
        StepLogger.step("Status code: " + response.getStatusCode());

        String faultDescription = getFaultDescription(response);
        String faultString      = getFaultString(response);
        String faultCode        = getFaultCode(response);
        String faultClass       = getFaultClass(response);
        String faultReason      = getFaultReason(response);

        StepLogger.step("Fault description: " + faultDescription);
        StepLogger.step("Fault string: " + faultString);
        StepLogger.step("Fault code: " + faultCode);
        StepLogger.step("Fault class: " + faultClass);
        StepLogger.step("Fault reason: " + faultReason);

        String errorText = getErrorMessage(response);

        assertEquals("Incorrect message", expectedMessage, errorText.trim());
        StepLogger.step("Error message: " + errorText.trim());
    }

    public static void logRequestAsAttachment(String namedAttachment, String content) {
        String requestAttachmentName = "Request";
        if (null != namedAttachment && !namedAttachment.isEmpty()) {
            requestAttachmentName += " [" + namedAttachment + "]";
        }
        StepLogger.saveTextAttachmentToLog(requestAttachmentName, content);
        if (isDebug) {
            BFLogger.logDebug(content);
        }
    }

    public static void logResponseAsAttachment(String namedAttachment, String content) {
        String responseAttachmentName = "Response";
        if (null != namedAttachment && !namedAttachment.isEmpty()) {
            responseAttachmentName += " [" + namedAttachment + "]";
        }
        StepLogger.saveTextAttachmentToLog(responseAttachmentName, content);
        if (isDebug) {
            BFLogger.logDebug(content);
        }
    }
}