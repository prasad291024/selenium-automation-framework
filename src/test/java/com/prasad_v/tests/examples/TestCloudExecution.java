package com.prasad_v.tests.examples;

import com.prasad_v.driver.DriverManagerCloud;
import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

@Epic("Cloud Execution")
@Feature("BrowserStack & LambdaTest")
public class TestCloudExecution {

    @Test(enabled = false) // Disabled by default - enable when you have credentials
    @Description("Test execution on BrowserStack cloud")
    public void testBrowserStack() throws Exception {
        LoggerUtil.info("Starting BrowserStack cloud test");
        
        DriverManagerCloud.initBrowserStack();
        WebDriver driver = DriverManagerCloud.getDriver();
        
        driver.get("https://app.vwo.com");
        String title = driver.getTitle();
        
        LoggerUtil.info("Page title: " + title);
        assertThat(title).contains("VWO");
        
        DriverManagerCloud.quit();
        LoggerUtil.info("BrowserStack test completed");
    }

    @Test(enabled = false) // Disabled by default - enable when you have credentials
    @Description("Test execution on LambdaTest cloud")
    public void testLambdaTest() throws Exception {
        LoggerUtil.info("Starting LambdaTest cloud test");
        
        DriverManagerCloud.initLambdaTest();
        WebDriver driver = DriverManagerCloud.getDriver();
        
        driver.get("https://app.vwo.com");
        String title = driver.getTitle();
        
        LoggerUtil.info("Page title: " + title);
        assertThat(title).contains("VWO");
        
        DriverManagerCloud.quit();
        LoggerUtil.info("LambdaTest test completed");
    }
}
