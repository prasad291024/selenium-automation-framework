package com.prasad_v.tests.pom.vwo;

import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.DashBoardPage;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.LoginPage;
import com.prasad_v.utils.PropertiesReader;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestVWOLogin_04_PropertyReader_DriverManager_POM_CommonToAll extends CommonToAllTest {

    @Description("TC#1- Verify that with invalid email, pass, error message is shown on the app.vwo.com")
    @Test
    public void test_negative_vwo_login() {
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());
        String error_msg = loginPage.loginToVWOLoginInvalidCreds(
            PropertiesReader.readKey("invalid_username"),
            PropertiesReader.readKey("invalid_password"));

        assertThat(error_msg).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(error_msg, PropertiesReader.readKey("error_message"));
    }

    @Owner("PRASAD")
    @Description("TC#2-Verify that valid creds dashboard page is loaded")
    @Test
    public void testLoginPositiveVWO() {
        LoginPage loginPage_VWO = new LoginPage(DriverManagerTL.getDriver());
        loginPage_VWO.loginToVWOLoginValidCreds(
            PropertiesReader.readKey("username"),
            PropertiesReader.readKey("password"));

        DashBoardPage dashBoardPage = new DashBoardPage(DriverManagerTL.getDriver());
        String usernameLoggedIn = dashBoardPage.loggedInUserName();

        assertThat(usernameLoggedIn).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(usernameLoggedIn, PropertiesReader.readKey("expected_username"));
    }
}