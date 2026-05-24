package com.prasad_v.base;

import com.prasad_v.utils.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.prasad_v.driver.DriverManagerTL.getDriver;

public class CommonToAllPage {

    public void openUrl(String url) {
        getDriver().get(url);
    }

    public void openAppUrl() {
        getDriver().get(ConfigManager.get("url"));
    }

    public void clickElement(By by) {
        getDriver().findElement(by).click();
    }

    public void clickElement(WebElement by) {
        by.click();
    }

    public void enterInput(By by, String key) {
        getDriver().findElement(by).sendKeys(key);
    }

    public void enterInput(WebElement by, String key) {
        by.sendKeys(key);
    }

    public String getText(By by) {
        return getDriver().findElement(by).getText();
    }

    public String getText(WebElement by) {
        return by.getText();
    }
}
