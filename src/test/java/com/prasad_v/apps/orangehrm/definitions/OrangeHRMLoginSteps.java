package com.prasad_v.apps.orangehrm.definitions;

import com.prasad_v.apps.orangehrm.pages.EmployeeListPage;
import com.prasad_v.apps.orangehrm.pages.LoginPage;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.CredentialResolver;
import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.LoggerUtil;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static org.assertj.core.api.Assertions.assertThat;

public class OrangeHRMLoginSteps {

    private LoginPage loginPage;
    private EmployeeListPage employeeListPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before(order = 2)
    public void setUp() {
        LoggerUtil.info("Initializing browser for OrangeHRM scenario");
        loginPage = new LoginPage(DriverManagerTL.getDriver());
        employeeListPage = new EmployeeListPage(DriverManagerTL.getDriver());
    }

    @Given("User is on OrangeHRM login page")
    public void userIsOnOrangeHRMLoginPage() {
        LoggerUtil.info("Navigating to OrangeHRM login page");
        loginPage.openAppUrl();
    }

    @When("User logs in with username {string} and password {string}")
    public void userLogsInWithCredentials(String username, String password) {
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

    @Then("User should see the PIM menu header")
    public void userShouldSeeThePIMMenuHeader() {
        String user = (capturedUsername != null && !capturedUsername.isBlank()) ? capturedUsername : ConfigManager.get("username");
        String pass = (capturedPassword != null && !capturedPassword.isBlank()) ? capturedPassword : ConfigManager.get("password");
        loginPage.loginWithCreds(user, pass);
        String menuHeader = employeeListPage.getMenuHeader();
        assertThat(menuHeader).as("Menu header should be visible").isEqualTo(ConfigManager.get("expected_username"));
        LoggerUtil.info("Login verified. Menu header: " + menuHeader);
    }

    @Then("User should see OrangeHRM error message {string}")
    public void userShouldSeeOrangeHRMErrorMessage(String expectedError) {
        String actualError = loginPage.loginWithInvalidCreds(capturedUsername, capturedPassword);
        assertThat(actualError).as("Error message should match").contains(expectedError);
        LoggerUtil.info("Error message verified: " + actualError);
    }
}
