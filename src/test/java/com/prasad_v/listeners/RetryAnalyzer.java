package com.prasad_v.listeners;

import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.Allure;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private final int maxRetryCount = getRetryCountFromSystem();
    private int retryCount = 0;

    private static int getRetryCountFromSystem() {
        String value = System.getProperty("retryCount", "1");
        try {
            int parsed = Integer.parseInt(value);
            return Math.max(parsed, 0);
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }

    @Override
    public boolean retry(ITestResult testResult) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            String testName = testResult.getMethod().getQualifiedName();
            String message = String.format("Retrying %s (attempt %d of %d)", testName, retryCount, maxRetryCount);
            LoggerUtil.warn(message);
            Allure.addAttachment("Retry Attempt", message);
            return true;
        }
        return false;
    }
}
