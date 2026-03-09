package com.prasad_v.tests.examples;

import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.pages.pageObjectModel.improved_POM.DashBoardPage;
import com.prasad_v.pages.pageObjectModel.improved_POM.LoginPage;
import com.prasad_v.utils.ConfigManager;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("VWO Application")
@Feature("Login Functionality")
public class TestVWOLoginWithAllure extends CommonToAllTest {

    @Test(priority = 1)
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test login with valid credentials and verify dashboard access")
    @Owner("Prasad")
    @Link(name = "VWO Login", url = "https://app.vwo.com")
    @Issue("VWO-123")
    @TmsLink("TC-001")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        Allure.step("Navigate to VWO login page");
        loginPage.openVWOUrl();
        
        Allure.step("Enter valid credentials");
        String username = ConfigManager.get("username");
        String password = ConfigManager.get("password");
        
        Allure.step("Click login button");
        DashBoardPage dashBoardPage = loginPage.loginToVWOValidCreds(username, password);
        
        Allure.step("Verify user is logged in");
        String loggedInUser = dashBoardPage.loggedInUserName();
        assertThat(loggedInUser).isNotNull().isNotEmpty();
        
        Allure.addAttachment("Logged In User", loggedInUser);
    }

    @Test(priority = 2)
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test login with invalid credentials and verify error message")
    @Owner("Prasad")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        Allure.step("Navigate to VWO login page");
        loginPage.openVWOUrl();
        
        Allure.step("Enter invalid credentials");
        String errorMsg = loginPage.loginToVWOLoginInvalidCreds("invalid@test.com", "wrongpass");
        
        Allure.step("Verify error message is displayed");
        assertThat(errorMsg).contains("Your email, password, IP address or location did not match");
        
        Allure.addAttachment("Error Message", errorMsg);
    }
}
