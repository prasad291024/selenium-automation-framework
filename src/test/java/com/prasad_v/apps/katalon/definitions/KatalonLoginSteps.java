package com.prasad_v.apps.katalon.definitions;

import com.prasad_v.apps.katalon.pages.AppointmentPage;
import com.prasad_v.apps.katalon.pages.HomePage;
import com.prasad_v.apps.katalon.pages.LoginPage;
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

public class KatalonLoginSteps {

    private HomePage homePage;
    private LoginPage loginPage;
    private AppointmentPage appointmentPage;
    private String capturedUsername;
    private String capturedPassword;

    @Before
    public void setUp() {
        LoggerUtil.info("Initializing browser for Katalon scenario");
        DriverManagerTL.init("chrome");
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
        capturedUsername = username;
        capturedPassword = password;
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
