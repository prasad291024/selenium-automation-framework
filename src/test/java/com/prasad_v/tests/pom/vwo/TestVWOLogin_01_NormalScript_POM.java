package com.prasad_v.tests.pom.vwo;

import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.pages.pageObjectModel.appvwo.normal_POM.DashboardPage;
import com.prasad_v.pages.pageObjectModel.appvwo.normal_POM.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

@Listeners({com.prasad_v.listeners.ScreenshotListener.class})
public class TestVWOLogin_01_NormalScript_POM {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        DriverManagerTL.init();
        driver = DriverManagerTL.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverManagerTL.quit();
    }

    @Owner("PRASAD")
    @Description("Verify that with invalid email, pass, error message is shown on the app.vwo.com")
    @Test
    public void test_negative_vwo_login() {
        LoginPage loginPage = new LoginPage(driver);
        String error_msg = loginPage.loginToVWOLoginInvalidCreds("admin@gmail.com", "1234");

        assertThat(error_msg).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(error_msg, "Your email, password, IP address or location did not match");
    }

    @Owner("PRASAD")
    @Description("Verify that valid creds dashboard page is loaded")
    @Test
    public void test_positive_vwo_login() {
        LoginPage loginPage_VWO = new LoginPage(driver);
        loginPage_VWO.loginToVWOLoginValidCreds("contact+aug@thetestingacademy.com", "TtxkgQ!s$rJBk85");
        
        DashboardPage dashBoardPage = new DashboardPage(driver);
        String usernameLoggedIn = dashBoardPage.loggedInUserName();

        assertThat(usernameLoggedIn).isNotBlank().isNotNull().isNotEmpty();
        Assert.assertEquals(usernameLoggedIn, "Aman");
    }
}