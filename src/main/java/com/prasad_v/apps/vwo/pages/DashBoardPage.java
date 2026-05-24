package com.prasad_v.apps.vwo.pages;

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

    private By loggedInUser = By.cssSelector("[data-qa=\"lufexuloga\"]");

    public String loggedInUserName() {
        LoggerUtil.info("Fetching logged in username from dashboard");
        WaitHelpers.visibilityOfElement(loggedInUser);
        String username = getText(loggedInUser);
        LoggerUtil.info("Logged in user resolved: " + LoggerUtil.redacted());
        return username;
    }
}
