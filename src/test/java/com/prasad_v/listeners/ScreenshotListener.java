package com.prasad_v.listeners;

import com.prasad_v.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static com.prasad_v.driver.DriverManagerTL.getDriver;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = getDriver();
        if (driver != null) {
            String testName = result.getMethod().getMethodName();
            ScreenshotUtil.captureScreenshot(driver, testName);
        }
    }
}
