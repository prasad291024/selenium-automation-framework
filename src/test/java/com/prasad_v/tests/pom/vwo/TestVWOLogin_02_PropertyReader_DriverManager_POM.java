package com.prasad_v.tests.pom.vwo;

import com.prasad_v.driver.DriverManager;
import com.prasad_v.pages.pageObjectModel.appvwo.normal_POM.DashboardPage;
import com.prasad_v.pages.pageObjectModel.appvwo.normal_POM.LoginPage;
import com.prasad_v.utils.ConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestVWOLogin_02_PropertyReader_DriverManager_POM {

    @Description("TC#1- Verify that with invalid email, pass, error message is shown on the app.vwo.com")
    @Test
    public void test_negative_vwo_login() throws Exception {



        // Driver Manager Code - 1
        DriverManager.init();

        // Page Class Code (POM Code) - 2
        LoginPage loginPage  = new LoginPage(DriverManager.getDriver());
        String error_msg = loginPage.loginToVWOLoginInvalidCreds(ConfigManager.get("invalid_username"),ConfigManager.get("invalid_password"));

        // Assertions - 3
        assertThat(error_msg).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(error_msg,ConfigManager.get("error_message"));

        DriverManager.getDriver().quit();

    }

    @Owner("PRASAD")
    @Description("TC#2-Verify that valid creds dashboard page is loaded")
    @Test
    public void testLoginPositiveVWO() {

        LoginPage loginPage_VWO = new LoginPage(DriverManager.getDriver());
        loginPage_VWO.loginToVWOLoginValidCreds(ConfigManager.get("username"),ConfigManager.get("password"));

        DashboardPage dashBoardPage  = new DashboardPage(DriverManager.getDriver());
        String usernameLoggedIn = dashBoardPage.loggedInUserName();


        assertThat(usernameLoggedIn).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(usernameLoggedIn,ConfigManager.get("expected_username"));



    }

}
