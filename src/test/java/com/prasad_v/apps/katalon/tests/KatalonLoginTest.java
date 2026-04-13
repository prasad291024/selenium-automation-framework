package com.prasad_v.apps.katalon.tests;

import com.prasad_v.apps.katalon.pages.AppointmentPage;
import com.prasad_v.apps.katalon.pages.HomePage;
import com.prasad_v.apps.katalon.pages.LoginPage;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ConfigManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Katalon Application")
@Feature("Login")
public class KatalonLoginTest extends CommonToAllTest {

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify valid credentials reach appointment page")
    public void testValidLogin() {
        HomePage homePage = new HomePage(DriverManagerTL.getDriver());
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());
        AppointmentPage appointmentPage = new AppointmentPage(DriverManagerTL.getDriver());

        homePage.goToLogin();
        loginPage.loginWithCreds(
                ConfigManager.get("username"),
                ConfigManager.get("password"));

        String header = appointmentPage.getHeader();
        assertThat(header).isEqualTo(ConfigManager.get("expected_header"));
        Allure.addAttachment("Katalon Header", header);
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify invalid credentials show authentication error")
    public void testInvalidLogin() {
        HomePage homePage = new HomePage(DriverManagerTL.getDriver());
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());

        homePage.goToLogin();
        String error = loginPage.loginWithInvalidCreds(
                ConfigManager.get("invalid_username"),
                ConfigManager.get("invalid_password"));

        assertThat(error).contains(ConfigManager.get("error_message"));
        Allure.addAttachment("Katalon Error", error);
    }
}
