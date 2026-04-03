package com.prasad_v.apps.orangehrm.tests;

import com.prasad_v.apps.orangehrm.pages.EmployeeListPage;
import com.prasad_v.apps.orangehrm.pages.LoginPage;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.utils.ConfigManager;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("OrangeHRM Application")
@Feature("Login")
public class OrangeHRMLoginTest extends CommonToAllTest {

    @Test
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify valid credentials redirect to PIM dashboard")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(DriverManagerTL.getDriver());
        EmployeeListPage employeeListPage = new EmployeeListPage(DriverManagerTL.getDriver());

        loginPage.loginWithCreds(
                ConfigManager.get("username"),
                ConfigManager.get("password"));

        String menuHeader = employeeListPage.getMenuHeader();
        assertThat(menuHeader)
                .as("Menu header should match expected")
                .isEqualTo(ConfigManager.get("expected_username"));

        Allure.addAttachment("Menu Header", menuHeader);
    }
}
