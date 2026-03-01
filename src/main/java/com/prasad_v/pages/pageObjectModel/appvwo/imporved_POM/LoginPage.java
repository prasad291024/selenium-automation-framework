// Declares the package location of this class
package com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.prasad_v.driver.DriverManager.getDriver;

public class LoginPage extends CommonToAllPage {

    WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    private By username = By.id("login-username");
    private By password = By.id("login-password");
    private By signButton = By.id("js-login-btn");
    private By error_message = By.id("js-notification-box-msg");

    public String loginToVWOLoginInvalidCreds(String user, String pwd) {
        LoggerUtil.info("Starting login with invalid credentials");
        openVWOUrl();
        
        LoggerUtil.info("Entering username: " + user);
        enterInput(username, user);
        enterInput(password, pwd);
        
        LoggerUtil.info("Clicking login button");
        clickElement(signButton);
        
        WaitHelpers.checkVisibility(getDriver(), error_message);
        String errorMsg = getText(error_message);
        LoggerUtil.info("Error message received: " + errorMsg);
        
        return errorMsg;
    }

    public void loginToVWOLoginValidCreds(String user, String pwd) {
        LoggerUtil.info("Starting login with valid credentials");
        openVWOUrl();
        
        LoggerUtil.info("Entering username: " + user);
        enterInput(username, user);
        enterInput(password, pwd);
        
        LoggerUtil.info("Clicking login button");
        clickElement(signButton);
        LoggerUtil.info("Login completed");
    }
}











/*
This class represents the Login Page of the VWO web application using the Page Object Model (POM) approach (without PageFactory).
It provides locators and actions needed to perform login operations (both valid and invalid) and handles login-related elements like username, password, sign-in button, and error messages.

🧩 Purpose / Intention of the Program
Encapsulates all elements and login actions for the VWO login page.
Follows POM principles to keep tests clean and maintainable.
Uses custom utility classes (CommonToAllPage, WaitHelpers, DriverManager) for cleaner Selenium code.


 */