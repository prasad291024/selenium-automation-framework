package com.prasad_v.apps.orangehrm.pages;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends CommonToAllPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private By username  = By.xpath("//input[@placeholder='Username']");
    private By password  = By.xpath("//input[@placeholder='Password']");
    private By submitBtn = By.xpath("//button[normalize-space()='Login']");

    public void loginWithCreds(String user, String pwd) {
        LoggerUtil.info("OrangeHRM login - user: " + user.replaceAll("[\r\n]", "_"));
        openAppUrl();
        enterInput(username, user);
        enterInput(password, pwd);
        clickElement(submitBtn);
        LoggerUtil.info("OrangeHRM login submitted");
    }
}
