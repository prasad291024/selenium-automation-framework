package com.prasad_v.apps.katalon.pages;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.prasad_v.driver.DriverManagerTL.getDriver;

public class AppointmentPage extends CommonToAllPage {

    WebDriver driver;

    public AppointmentPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By header = By.xpath("//h2[normalize-space()='Make Appointment']");

    public String getHeader() {
        WaitHelpers.checkVisibility(getDriver(), header);
        String value = getText(header);
        LoggerUtil.info("Katalon appointment header: " + value);
        return value;
    }
}
