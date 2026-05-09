package com.prasad_v.base;

import com.prasad_v.driver.DriverManagerTL;
import com.prasad_v.listeners.ScreenshotListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({ ScreenshotListener.class })
public class CommonToAllTest {

    protected org.openqa.selenium.WebDriver driver;

    @BeforeMethod
    public void setUp() {
        DriverManagerTL.init();
        driver = DriverManagerTL.getDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverManagerTL.quit();
    }
}
