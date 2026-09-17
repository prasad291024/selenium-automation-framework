package com.prasad_v.apps.katalon.definitions;

import com.prasad_v.apps.katalon.pages.AppointmentPage;
import com.prasad_v.apps.katalon.pages.HomePage;
import com.prasad_v.apps.katalon.pages.LoginPage;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.CredentialResolver;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static org.assertj.core.api.Assertions.assertThat;

public class KatalonLoginSteps {

    private HomePage homePage;
    private LoginPage loginPage;
    private AppointmentPage appointmentPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before(order = 2)
    public void setUp() {
        LoggerUtil.info("Initializing browser for Katalon scenario");
        homePage = new HomePage(DriverManagerTL.getDriver());
        loginPage = new LoginPage(DriverManagerTL.getDriver());
        appointmentPage = new AppointmentPage(DriverManagerTL.getDriver());
    }

    @Given("User is on Katalon home page")
    public void userIsOnKatalonHomePage() {
        LoggerUtil.info("Navigating to Katalon home page");
        homePage.goToLogin();
    }

    @When("User logs in to Katalon with username {string} and password {string}")
    public void userLogsInToKatalon(String username, String password) {
        String resolvedUsername = CredentialResolver.resolveCredentials(username);
        String resolvedPassword = CredentialResolver.resolveCredentials(password);

        // If the resolved value is an unresolved placeholder (environment variable not set),
        // fall back to ConfigManager
        if (isUnresolvedPlaceholder(username, resolvedUsername)) {
            capturedUsername = ConfigManager.get("username");
        } else {
            capturedUsername = resolvedUsername;
        }

        if (isUnresolvedPlaceholder(password, resolvedPassword)) {
            capturedPassword = ConfigManager.get("password");
        } else {
            capturedPassword = resolvedPassword;
        }
    }

    /**
     * Checks if the original string was a placeholder that remains unresolved.
     * @param original the original string from the feature file
     * @param resolved the string after CredentialResolver resolution
     * @return true if original matches ${...} pattern and resolved equals original (unresolved)
     */
    private boolean isUnresolvedPlaceholder(String original, String resolved) {
        return original != null
            && original.matches("\\$\\{[^}]+\\}")
            && resolved.equals(original);
    }

    @Then("User should see the Make Appointment header")
    public void userShouldSeeTheMakeAppointmentHeader() {
        loginPage.loginWithCreds(capturedUsername, capturedPassword);
        String header = appointmentPage.getHeader();
        assertThat(header).as("Appointment header should be visible").isEqualTo("Make Appointment");
        LoggerUtil.info("Login verified. Header: " + header);
    }

    @Then("User should see Katalon error message {string}")
    public void userShouldSeeKatalonErrorMessage(String expectedError) {
        String actualError = loginPage.loginWithInvalidCreds(capturedUsername, capturedPassword);
        assertThat(actualError).as("Error message should match").contains(expectedError);
        LoggerUtil.info("Error message verified: " + actualError);
    }
}
