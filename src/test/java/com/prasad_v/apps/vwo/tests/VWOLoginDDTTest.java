package com.prasad_v.apps.vwo.tests;

import com.prasad_v.apps.vwo.pages.LoginPage;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ExcelUtil;
import com.prasad_v.utils.LoggerUtil;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("VWO Application")
@Feature("Login - Data Driven")
public class VWOLoginDDTTest extends CommonToAllTest {

    private static final String DATA_FILE = "src/test/resources/testdata/VWOTestData.xlsx";

    @DataProvider(name = "invalidLoginData", parallel = true)
    public Object[][] invalidLoginData() {
        return ExcelUtil.getTestData(DATA_FILE, "InvalidLogin");
    }

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return ExcelUtil.getTestData(DATA_FILE, "ValidLogin");
    }

    @Test(dataProvider = "invalidLoginData")
    @Story("Invalid Login - Data Driven")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify error message for multiple invalid credential combinations from Excel")
    public void testInvalidLoginDDT(String username, String password, String expectedError) {
        LoggerUtil.info("DDT - Invalid login test | user: " + LoggerUtil.redacted());
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());

        String actualError = loginPage.loginWithInvalidCreds(username, password);

        assertThat(actualError)
                .as("Error message should match for supplied user")
                .contains(expectedError);

        Allure.addAttachment("Test Data", "text/plain", "User: " + LoggerUtil.redacted() + " | Expected: " + expectedError);
    }

    // SKIPPED: Requires a paid VWO account. Prior test credentials expired
    // and no replacement account is currently available. Re-enable once
    // valid VWO_USERNAME/VWO_PASSWORD are available.
    @Test(dataProvider = "validLoginData", enabled = false)
    @Story("Valid Login - Data Driven")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify successful login for valid credentials from Excel")
    public void testValidLoginDDT(String username, String password) {
        LoggerUtil.info("DDT - Valid login test | user: " + LoggerUtil.redacted());
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());

        String resolvedUsername = resolveCredentials(username);
        String resolvedPassword = resolveCredentials(password);

        loginPage.loginWithValidCreds(resolvedUsername, resolvedPassword);

        String pageTitle = DriverManagerTL.getDriver().getTitle();
        assertThat(pageTitle).containsIgnoringCase("dashboard");

        Allure.addAttachment("Test Data", "User: " + LoggerUtil.redacted());
    }

    private String resolveCredentials(String value) {
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVarName = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVarName);
            return envValue != null ? envValue : value;
        }
        return value;
    }
}
