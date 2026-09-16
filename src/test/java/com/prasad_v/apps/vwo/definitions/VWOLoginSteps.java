package com.prasad_v.apps.vwo.definitions;

import com.prasad_v.apps.vwo.pages.DashBoardPage;
import com.prasad_v.apps.vwo.pages.LoginPage;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.SkipException;

import java.net.HttpURLConnection;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class VWOLoginSteps {

    private LoginPage loginPage;
    private DashBoardPage dashBoardPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before(order = 0)
    public void checkSiteReachability() {
        String url = ConfigManager.get("url");
        if (url == null || url.isBlank()) {
            url = "https://app.vwo.com";
        }
        try {
            URI uri = URI.create(url);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            int code = conn.getResponseCode();
            if (code >= 500) {
                String warnMsg = "VWO site returned HTTP " + code + " at " + url + " - skipping scenario";
                LoggerUtil.warn(warnMsg);
                throw new SkipException(warnMsg);
            }
        } catch (SkipException se) {
            throw se;
        } catch (Exception e) {
            String warnMsg = "VWO site unreachable at " + url + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ") - skipping scenario";
            LoggerUtil.warn(warnMsg);
            throw new SkipException(warnMsg, e);
        }
    }

    @Before(order = 1)
    public void setUp() {
        LoggerUtil.info("Initializing browser for scenario");
        DriverManagerTL.init();
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
        // Resolve environment variable placeholders if present
        capturedUsername = resolveCredentials(username);
        capturedPassword = resolveCredentials(password);
    }

    private String resolveCredentials(String value) {
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVarName = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVarName);
            return envValue != null ? envValue : value;
        }
        return value;
    }

    @Then("User should be redirected to Dashboard")
    public void userShouldBeRedirectedToDashboard() {
        loginPage.loginWithValidCreds(capturedUsername, capturedPassword);
        String loggedInUser = dashBoardPage.loggedInUserName();
        assertThat(loggedInUser).as("Logged in user should not be empty").isNotNull().isNotEmpty();
        LoggerUtil.info("Login verified. User: " + LoggerUtil.redacted());
    }

    @Then("User should see error message {string}")
    public void userShouldSeeErrorMessage(String expectedError) {
        String actualError = loginPage.loginWithInvalidCreds(capturedUsername, capturedPassword);
        assertThat(actualError).as("Error message should match").contains(expectedError);
        LoggerUtil.info("Error message verified: " + actualError);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (DriverManagerTL.getDriver() != null) {
            if (scenario.isFailed()) {
                LoggerUtil.warn("Scenario FAILED: " + scenario.getName());
                byte[] screenshot = ((TakesScreenshot) DriverManagerTL.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
            }
            DriverManagerTL.quit();
        }
    }
}
