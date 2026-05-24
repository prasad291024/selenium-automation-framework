package com.prasad_v.apps.katalon.pages;

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

    private final By username = By.id("txt-username");
    private final By password = By.id("txt-password");
    private final By loginBtn = By.id("btn-login");
    private final By errorMessage = By.cssSelector("p.lead.text-danger");

    public void loginWithCreds(String user, String pwd) {
        LoggerUtil.info("Katalon login - user: " + LoggerUtil.redacted());
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(loginBtn);
    }

    public String loginWithInvalidCreds(String user, String pwd) {
        LoggerUtil.info("Katalon invalid login - user: " + LoggerUtil.redacted());
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(loginBtn);
        WaitHelpers.checkVisibility(getDriver(), errorMessage);
        return getText(errorMessage);
    }
}
