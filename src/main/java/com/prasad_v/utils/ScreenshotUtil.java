package com.prasad_v.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots/";
    private static final String PAGE_SOURCE_DIR = "page_source/";

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            Files.createDirectories(Paths.get(PAGE_SOURCE_DIR));

            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            Files.write(Paths.get(filePath), screenshot);

            // Attach to Allure Report
            Allure.addAttachment(testName, "image/png", new ByteArrayInputStream(screenshot), "png");

            // Also capture and attach page source
            String pageSource = driver.getPageSource();
            String pageSourceFileName = testName + "_" + timestamp + ".html";
            String pageSourceFilePath = PAGE_SOURCE_DIR + pageSourceFileName;
            Files.write(Paths.get(pageSourceFilePath), pageSource.getBytes());
            Allure.addAttachment("Page Source - " + testName, "text/html", new ByteArrayInputStream(pageSource.getBytes(StandardCharsets.UTF_8)), "html");

            LoggerUtil.info("Screenshot and page source captured: " + filePath);
            return filePath;

        } catch (IOException e) {
            LoggerUtil.error("Failed to capture screenshot or page source", e);
            return null;
        }
    }
}
