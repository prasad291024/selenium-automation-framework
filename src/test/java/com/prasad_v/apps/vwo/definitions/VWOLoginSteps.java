package com.prasad_v.apps.vwo.definitions;

import com.prasad_v.apps.vwo.pages.DashBoardPage;
import com.prasad_v.apps.vwo.pages.LoginPage;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.CredentialResolver;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class VWOLoginSteps {

    private LoginPage loginPage;
    private DashBoardPage dashBoardPage;
    private String capturedUsername;
    private String capturedPassword;

    @Given("User is on VWO login page")
    public void userIsOnVWOLoginPage() {
        LoggerUtil.info("Navigating to application login page");
        if (loginPage == null) {
            loginPage = new LoginPage(DriverManagerTL.getDriver());
            dashBoardPage = new DashBoardPage(DriverManagerTL.getDriver());
        }
        loginPage.openAppUrl();
    }

    @When("User logs in with username {string} and password {string}")
    public void userLogsInWithUsernameAndPassword(String username, String password) {
        // Resolve environment variable placeholders if present
        capturedUsername = CredentialResolver.resolveCredentials(username);
        capturedPassword = CredentialResolver.resolveCredentials(password);
        loginPage.loginWithValidCreds(capturedUsername, capturedPassword);
    }

    @Then("User should be redirected to Dashboard")
    public void userShouldBeRedirectedToDashboard() {
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
}
