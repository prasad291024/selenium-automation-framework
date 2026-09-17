package com.prasad_v.cucumber.hooks;

import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class CucumberHooks {

    @Before(order = 1)
    public void initializeDriver() {
        LoggerUtil.info("CucumberHooks: Initializing browser for scenario");
        DriverManagerTL.init();
        LoggerUtil.info("CucumberHooks: Driver initialized: " + (DriverManagerTL.getDriver() != null));
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (DriverManagerTL.getDriver() != null) {
            if (scenario.isFailed()) {
                LoggerUtil.warn("Scenario FAILED: " + scenario.getName());
                byte[] screenshot = ((TakesScreenshot) DriverManagerTL.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
            }
            LoggerUtil.info("CucumberHooks: Quitting driver");
            DriverManagerTL.quit();
        }
    }
}