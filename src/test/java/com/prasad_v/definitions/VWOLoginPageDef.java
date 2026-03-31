package com.prasad_v.definitions;

import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.DashBoardPage;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.LoginPage;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static org.assertj.core.api.Assertions.assertThat;

public class VWOLoginPageDef {

    private LoginPage loginPage;
    private DashBoardPage dashBoardPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before
    public void setUp() {
        LoggerUtil.info("Initializing browser for Cucumber scenario");
        DriverManagerTL.init("chrome");
        loginPage = new LoginPage(DriverManagerTL.getDriver());
        dashBoardPage = new DashBoardPage(DriverManagerTL.getDriver());
    }

    @Given("User is on VWO login page")
    public void userIsOnVWOLoginPage() {
        LoggerUtil.info("Navigating to application login page");
        loginPage.openAppUrl();
    }

    @When("User enters username as {string} and password as {string}")
    public void userEntersCredentials(String username, String password) {
        LoggerUtil.info("Entering credentials - username: " + username);
        capturedUsername = username;
        capturedPassword = password;
    }

    @Then("User should be redirected to Dashboard")
    public void userShouldBeRedirectedToDashboard() {
        LoggerUtil.info("Attempting valid login and verifying dashboard");
        loginPage.loginToVWOLoginValidCreds(capturedUsername, capturedPassword);
        String loggedInUser = dashBoardPage.loggedInUserName();
        assertThat(loggedInUser)
                .as("Logged in username should not be empty")
                .isNotNull()
                .isNotEmpty();
        LoggerUtil.info("Login successful. User: " + loggedInUser);
    }

    @Then("User should see error message {string}")
    public void userShouldSeeErrorMessage(String expectedError) {
        LoggerUtil.info("Attempting invalid login and verifying error message");
        String actualError = loginPage.loginToVWOLoginInvalidCreds(capturedUsername, capturedPassword);
        assertThat(actualError)
                .as("Error message should match expected")
                .contains(expectedError);
        LoggerUtil.info("Error message verified: " + actualError);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            LoggerUtil.warn("Scenario FAILED: " + scenario.getName() + " - capturing screenshot");
            byte[] screenshot = ((TakesScreenshot) DriverManagerTL.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
        LoggerUtil.info("Tearing down browser after scenario: " + scenario.getName());
        DriverManagerTL.quit();
    }
}
