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

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManagerTL.quit();
    }

    protected void verifySiteReachable(String targetUrl) {
        try {
            java.net.URI uri = java.net.URI.create(targetUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);
            int code = conn.getResponseCode();
            if (code >= 500) {
                String warnMsg = "Site returned HTTP " + code + " at " + targetUrl + " - skipping test";
                com.prasad_v.utils.LoggerUtil.warn(warnMsg);
                throw new org.testng.SkipException(warnMsg);
            }
        } catch (org.testng.SkipException se) {
            throw se;
        } catch (Exception e) {
            String warnMsg = "Site unreachable at " + targetUrl + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ") - skipping test";
            com.prasad_v.utils.LoggerUtil.warn(warnMsg);
            throw new org.testng.SkipException(warnMsg, e);
        }
    }
}
