package com.prasad_v.driver;

import com.prasad_v.utils.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DriverManagerCloud {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void initBrowserStack() throws MalformedURLException {
        String username = ConfigManager.get("browserstack.username");
        String accessKey = ConfigManager.get("browserstack.accesskey");

        HashMap<String, Object> bsOptions = new HashMap<>();
        bsOptions.put("os", "Windows");
        bsOptions.put("osVersion", "11");
        bsOptions.put("projectName", "ATB10x Selenium Framework");
        bsOptions.put("buildName", "Build-" + System.currentTimeMillis());
        bsOptions.put("sessionName", "Test Session");

        ChromeOptions options = new ChromeOptions();
        options.setCapability("bstack:options", bsOptions);

        String hubUrl = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
        driver.set(new RemoteWebDriver(new URL(hubUrl), options));
    }

    public static void initLambdaTest() throws MalformedURLException {
        String username = ConfigManager.get("lambdatest.username");
        String accessKey = ConfigManager.get("lambdatest.accesskey");

        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", "Windows 11");
        ltOptions.put("browserName", "Chrome");
        ltOptions.put("browserVersion", "latest");
        ltOptions.put("project", "ATB10x Selenium Framework");
        ltOptions.put("build", "Build-" + System.currentTimeMillis());
        ltOptions.put("name", "Test Session");
        ltOptions.put("w3c", true);

        ChromeOptions options = new ChromeOptions();
        options.setCapability("LT:Options", ltOptions);

        String hubUrl = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";
        driver.set(new RemoteWebDriver(new URL(hubUrl), options));
    }

    public static void quit() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}
