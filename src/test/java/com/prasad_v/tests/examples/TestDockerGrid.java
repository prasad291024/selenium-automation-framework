package com.prasad_v.tests.examples;

import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.*;

@Epic("Docker Execution")
@Feature("Selenium Grid")
public class TestDockerGrid {

    private WebDriver driver;

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) throws Exception {
        LoggerUtil.info("Setting up Docker Grid test with browser: " + browser);
        
        String gridUrl = "http://localhost:4444/wd/hub";
        
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(gridUrl), options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL(gridUrl), options);
        }
        
        LoggerUtil.info("Driver initialized on Grid");
    }

    @Test
    @Description("Test execution on Docker Selenium Grid")
    public void testOnDockerGrid() {
        LoggerUtil.info("Starting Docker Grid test");
        
        driver.get("https://app.vwo.com");
        String title = driver.getTitle();
        
        LoggerUtil.info("Page title: " + title);
        assertThat(title).isNotNull().isNotEmpty();
        
        LoggerUtil.info("Docker Grid test completed successfully");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            LoggerUtil.info("Driver closed");
        }
    }
}
