package com.prasad_v.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EnhancedWaitUtil {

    private static final int DEFAULT_TIMEOUT = 20;
    private static final int DEFAULT_POLLING = 500;

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT), Duration.ofMillis(DEFAULT_POLLING));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForInvisibility(WebDriver driver, By locator) {
        getWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForUrlContains(WebDriver driver, String urlFragment) {
        getWait(driver).until(ExpectedConditions.urlContains(urlFragment));
    }

    public static void waitForTitleContains(WebDriver driver, String title) {
        getWait(driver).until(ExpectedConditions.titleContains(title));
    }

    public static void waitForPageLoad(WebDriver driver) {
        getWait(driver).until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
}
