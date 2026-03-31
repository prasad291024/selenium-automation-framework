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






















/*
This program is a base class containing common reusable methods for all page classes in a Selenium Page Object Model (POM) structure.
It is designed to promote reusability, reduce code duplication, and simplify the code in individual page classes.

The purpose of this program is to:
Centralize all common actions used across different page objects like clicking, entering input, retrieving text, and opening specific URLs.
Provide a foundation for all page classes so that they can inherit and use these shared utilities.
Improve maintainability and scalability of the test automation framework.


The CommonToAllPage class contains the following methods:
openVWOUrl(): Opens the VWO website URL specified in the properties file.
openOrangeHRMUrl(): Opens the OrangeHRM website URL specified in the properties file.
clickElement(By by): Clicks on a web element identified by the given locator.
clickElement(WebElement by): Clicks on a web element directly provided.
enterInput(By by, String key): Enters the provided key into a web element identified by the given locator.
enterInput(WebElement by, String key): Enters the provided key into a web element directly provided.
getText(By by): Retrieves the text content of a web element identified by the given locator.
getText(WebElement by): Retrieves the text content of a web element directly provided.

The CommonToAllPage constructor is currently empty, but it can be used to perform any initialization tasks that are common across all page objects.


Conditions
The program assumes that the WebDriver is already initialized and the browser is running (getDriver() must return a valid instance).
The locators provided must match elements present in the DOM.
The PropertiesReader.readKey() must be correctly implemented to fetch property values from a configuration file.


Expected Output
URLs open correctly based on configuration.
Elements are clicked or populated with input correctly.
Text is retrieved correctly from the element.



 */