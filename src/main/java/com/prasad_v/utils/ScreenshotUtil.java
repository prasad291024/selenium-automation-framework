package com.prasad_v.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "screenshots/";

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            Files.write(Paths.get(filePath), screenshot);
            
            // Attach to Allure Report
            Allure.addAttachment(testName, new ByteArrayInputStream(screenshot));
            
            LoggerUtil.info("Screenshot captured: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            LoggerUtil.error("Failed to capture screenshot", e);
            return null;
        }
    }
}
