package com.prasad_v.tests.examples;

import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.DashBoardPage;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.LoginPage;
import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.EnhancedWaitUtil;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.ScreenshotUtil;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import static org.assertj.core.api.Assertions.*;

@Listeners({com.prasad_v.listeners.ScreenshotListener.class})
public class TestVWOLogin_Enhanced {

    private WebDriver driver;

    @BeforeMethod
    @Step("Setup browser and initialize driver")
    public void setUp() {
        LoggerUtil.info("=== Test Execution Started ===");
        LoggerUtil.info("Environment: " + ConfigManager.getEnvironment());
        
        DriverManagerTL.init();
        driver = DriverManagerTL.getDriver();
        
        LoggerUtil.info("Browser launched successfully");
    }

    @Test(priority = 1)
    @Epic("VWO Application")
    @Feature("Login Functionality")
    @Story("Negative Login Test")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify error message is displayed for invalid credentials")
    public void testInvalidLogin() {
        LoggerUtil.info("Starting invalid login test");
        
        LoginPage loginPage = new LoginPage(driver);
        String errorMsg = loginPage.loginToVWOLoginInvalidCreds(
            ConfigManager.get("invalid_username"),
            ConfigManager.get("invalid_password")
        );
        
        LoggerUtil.info("Error message received: " + errorMsg);
        
        assertThat(errorMsg)
            .isNotBlank()
            .contains("did not match");
        
        ScreenshotUtil.captureScreenshot(driver, "InvalidLogin");
        LoggerUtil.info("Invalid login test completed successfully");
    }

    @Test(priority = 2)
    @Epic("VWO Application")
    @Feature("Login Functionality")
    @Story("Positive Login Test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can login with valid credentials")
    public void testValidLogin() {
        LoggerUtil.info("Starting valid login test");
        
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToVWOLoginValidCreds(
            ConfigManager.get("username"),
            ConfigManager.get("password")
        );
        
        // Wait for dashboard to load
        EnhancedWaitUtil.waitForPageLoad(driver);
        
        DashBoardPage dashboardPage = new DashBoardPage(driver);
        String username = dashboardPage.loggedInUserName();
        
        LoggerUtil.info("Logged in user: " + username);
        
        assertThat(username)
            .isNotBlank()
            .isEqualTo(ConfigManager.get("expected_username"));
        
        ScreenshotUtil.captureScreenshot(driver, "ValidLogin");
        LoggerUtil.info("Valid login test completed successfully");
    }

    @AfterMethod
    @Step("Cleanup and quit browser")
    public void tearDown() {
        if (driver != null) {
            LoggerUtil.info("Closing browser");
            DriverManagerTL.quit();
        }
        LoggerUtil.info("=== Test Execution Completed ===\n");
    }
}
