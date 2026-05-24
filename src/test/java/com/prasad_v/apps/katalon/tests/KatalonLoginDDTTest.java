package com.prasad_v.apps.katalon.tests;

import com.prasad_v.apps.katalon.pages.HomePage;
import com.prasad_v.apps.katalon.pages.LoginPage;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ExcelUtil;
import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Katalon Application")
@Feature("Login - Data Driven")
public class KatalonLoginDDTTest extends CommonToAllTest {

    private static final String DATA_FILE = "src/test/resources/testdata/KatalonTestData.xlsx";

    @DataProvider(name = "invalidLoginData", parallel = true)
    public Object[][] invalidLoginData() {
        return ExcelUtil.getTestData(DATA_FILE, "InvalidLogin");
    }

    @Test(dataProvider = "invalidLoginData")
    @Story("Invalid Login - Data Driven")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message for multiple invalid credential combinations from Excel")
    public void testInvalidLoginDDT(String username, String password, String expectedError) {
        LoggerUtil.info("DDT - Invalid login test | user: " + LoggerUtil.redacted());
        HomePage homePage = new HomePage(DriverManagerTL.getDriver());
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());

        homePage.goToLogin();
        String actualError = loginPage.loginWithInvalidCreds(username, password);

        assertThat(actualError)
                .as("Error message should match for supplied user")
                .contains(expectedError);

        Allure.addAttachment("Test Data", "User: " + LoggerUtil.redacted() + " | Expected: " + expectedError);
    }
}
