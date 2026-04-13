package com.prasad_v.apps.katalon.pages;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends CommonToAllPage {

    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    private final By makeAppointmentBtn = By.id("btn-make-appointment");

    public void goToLogin() {
        LoggerUtil.info("Opening Katalon app and navigating to login");
        openAppUrl();
        clickElement(makeAppointmentBtn);
    }
}
