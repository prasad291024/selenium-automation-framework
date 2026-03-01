package com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashBoardPage extends CommonToAllPage {

    WebDriver driver;

    public DashBoardPage(WebDriver driver) {
        this.driver = driver;
    }

    By userNameOnDashboard = By.cssSelector("[data-qa=\"lufexuloga\"]");

    public String loggedInUserName() {
        LoggerUtil.info("Fetching logged in username from dashboard");
        WaitHelpers.visibilityOfElement(userNameOnDashboard);
        String username = getText(userNameOnDashboard);
        LoggerUtil.info("Logged in user: " + username);
        return username;
    }
}

















/*

This class represents the Dashboard Page in the improved Page Object Model (POM) structure of the VWO application. It encapsulates the UI elements and actions related to the Dashboard page.

This class:
Extends a common base class (CommonToAllPage) to reuse common Selenium actions (like getText, clickElement, etc.).
Implements Dashboard-specific actions like fetching the logged-in username from the UI.

🧩 Purpose / Intention of the Program
To encapsulate dashboard-specific behavior and elements into one page class.
Enables cleaner, reusable test scripts that interact with the VWO Dashboard Page.
Uses By locator strategy, instead of @FindBy (i.e., not using PageFactory).


 */