package com.prasad_v.apps.vwo.tests;

import com.prasad_v.apps.vwo.pages.DashBoardPage;
import com.prasad_v.apps.vwo.pages.LoginPage;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ConfigManager;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("VWO Application")
@Feature("Login")
public class VWOLoginTest extends CommonToAllTest {

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify valid credentials redirect to dashboard")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());
        DashBoardPage dashBoardPage = new DashBoardPage(DriverManagerTL.getDriver());

        loginPage.loginWithValidCreds(
                ConfigManager.get("username"),
                ConfigManager.get("password"));

        String loggedInUser = dashBoardPage.loggedInUserName();
        assertThat(loggedInUser).isNotNull().isNotEmpty();
        Allure.addAttachment("Logged In User", loggedInUser);
    }

    @Test
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify invalid credentials show error message")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());

        String errorMsg = loginPage.loginWithInvalidCreds(
                ConfigManager.get("invalid_username"),
                ConfigManager.get("invalid_password"));

        assertThat(errorMsg).contains(ConfigManager.get("error_message"));
        Allure.addAttachment("Error Message", errorMsg);
    }
}
