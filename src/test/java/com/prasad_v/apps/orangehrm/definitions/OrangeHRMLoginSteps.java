package com.prasad_v.apps.orangehrm.definitions;

import com.prasad_v.apps.orangehrm.pages.EmployeeListPage;
import com.prasad_v.apps.orangehrm.pages.LoginPage;
import com.prasad_v.driver.DriverManagerTL;
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

public class OrangeHRMLoginSteps {

    private LoginPage loginPage;
    private EmployeeListPage employeeListPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before
    public void setUp() {
        LoggerUtil.info("Initializing browser for OrangeHRM scenario");
        DriverManagerTL.init();
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

    @Then("User should see the PIM menu header")
    public void userShouldSeeThePIMMenuHeader() {
        loginPage.loginWithCreds(capturedUsername, capturedPassword);
        String menuHeader = employeeListPage.getMenuHeader();
        assertThat(menuHeader).as("PIM menu header should be visible").isEqualTo("PIM");
        LoggerUtil.info("Login verified. Menu header: " + menuHeader);
    }

    @Then("User should see OrangeHRM error message {string}")
    public void userShouldSeeOrangeHRMErrorMessage(String expectedError) {
        String actualError = loginPage.loginWithInvalidCreds(capturedUsername, capturedPassword);
        assertThat(actualError).as("Error message should match").contains(expectedError);
        LoggerUtil.info("Error message verified: " + actualError);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            LoggerUtil.warn("Scenario FAILED: " + scenario.getName());
            byte[] screenshot = ((TakesScreenshot) DriverManagerTL.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
        DriverManagerTL.quit();
    }
}
