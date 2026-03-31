package com.prasad_v.tests.pom.vwo;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.listeners.RetryAnalyzer;
import com.prasad_v.listeners.ScreenshotListener;
import com.prasad_v.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;

import static com.prasad_v.driver.DriverManagerTL.getDriver;

@Listeners(ScreenshotListener.class)
@Test(retryAnalyzer = RetryAnalyzer.class)
public class TestVWOLogin_Retry extends CommonToAllTest {

    private static final Logger logger = LogManager.getLogger(TestVWOLogin_Retry.class);

    @Owner("PRAMOD")
    @Description("Verify retry mechanism on a failing test")
    @Test
    public void testFail() {
        getDriver().get(ConfigManager.get("url"));
        logger.info("Testing retry mechanism - expected to fail");
        Assert.assertTrue(false);
    }

    @Owner("PRAMOD")
    @Description("Verify retry mechanism on a passing test")
    @Test
    public void testPass() {
        getDriver().get(ConfigManager.get("url"));
        logger.info("Testing retry mechanism - expected to pass");
        Assert.assertTrue(true);
    }
}