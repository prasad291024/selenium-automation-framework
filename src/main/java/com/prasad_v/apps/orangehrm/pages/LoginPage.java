package com.prasad_v.apps.orangehrm.pages;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.prasad_v.driver.DriverManagerTL.getDriver;

public class LoginPage extends CommonToAllPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private By username     = By.xpath("//input[@placeholder='Username']");
    private By password     = By.xpath("//input[@placeholder='Password']");
    private By submitBtn    = By.xpath("//button[normalize-space()='Login']");
    private By errorMessage = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

    public void loginWithCreds(String user, String pwd) {
        LoggerUtil.info("OrangeHRM login - user: " + LoggerUtil.redacted());
        openAppUrl();
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(submitBtn);
        LoggerUtil.info("OrangeHRM login submitted");
    }

    public String loginWithInvalidCreds(String user, String pwd) {
        LoggerUtil.info("OrangeHRM invalid login - user: " + LoggerUtil.redacted());
        openAppUrl();
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(submitBtn);
        WaitHelpers.checkVisibility(getDriver(), errorMessage);
        String error = getText(errorMessage);
        LoggerUtil.info("Error message: " + error);
        return error;
    }
}
