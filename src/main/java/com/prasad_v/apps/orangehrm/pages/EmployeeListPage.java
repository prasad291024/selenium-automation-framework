package com.prasad_v.apps.orangehrm.pages;

import com.prasad_v.base.CommonToAllPage;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.WaitHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmployeeListPage extends CommonToAllPage {

    WebDriver driver;

    public EmployeeListPage(WebDriver driver) {
        this.driver = driver;
    }

    private By menuHeader = By.xpath("//h6[contains(@class,'oxd-topbar-header-breadcrumb-module')]");

    public String getMenuHeader() {
        LoggerUtil.info("Fetching menu header from OrangeHRM dashboard");
        WaitHelpers.checkVisibility(driver, menuHeader);
        String header = getText(menuHeader);
        LoggerUtil.info("Menu header: " + header);
        return header;
    }
}
