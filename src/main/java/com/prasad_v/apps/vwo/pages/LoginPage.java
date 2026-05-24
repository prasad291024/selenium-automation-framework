package com.prasad_v.apps.vwo.pages;

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

    private By username    = By.id("login-username");
    private By password    = By.id("login-password");
    private By signButton  = By.id("js-login-btn");
    private By errorMessage = By.id("js-notification-box-msg");

    public String loginWithInvalidCreds(String user, String pwd) {
        LoggerUtil.info("Login attempt with invalid credentials - user: " + LoggerUtil.redacted());
        openAppUrl();
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(signButton);
        WaitHelpers.checkVisibility(getDriver(), errorMessage);
        String error = getText(errorMessage);
        LoggerUtil.info("Error message: " + error);
        return error;
    }

    public void loginWithValidCreds(String user, String pwd) {
        LoggerUtil.info("Login attempt with valid credentials - user: " + LoggerUtil.redacted());
        openAppUrl();
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(signButton);
        LoggerUtil.info("Login submitted");
    }
}
