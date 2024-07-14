package com.capgemini.stk.framework.reporter.allure;

import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.stk.framework.reporter.JiraCSVGenerator;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;

public class StepListener implements StepLifecycleListener {
    @Override
    public void beforeStepStart(StepResult result) {
        JiraCSVGenerator.stepsList.add(result.getName());
        BFLogger.logInfo("[Allure Step Start] ::: " + result.getName());
        StepLifecycleListener.super.beforeStepStart(result);
    }
}