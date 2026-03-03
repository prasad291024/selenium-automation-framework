package com.prasad_v.tests.pageFactory;

import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.pages.pageFactory.appvwo.LoginPage_PF;
import com.prasad_v.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.prasad_v.driver.DriverManager.getDriver;

public class TestVWOLogin_PF extends CommonToAllTest {

    private static final Logger logger = LogManager.getLogger(TestVWOLogin_PF.class);

    @Test
    public void testLoginNegativeVWO_PF() {
        logger.info("Starting the Testcases Page Factory");
        getDriver().get(ConfigManager.get("url"));
        LoginPage_PF loginPage_PF = new LoginPage_PF(getDriver());
        String error_msg = loginPage_PF.loginToVWOInvalidCreds();
        logger.info("Error msg I got "+ error_msg);
        logger.info("Finished the Testcases Page Factory");
        logger.debug("Finished the Testcases Page Factory");
        logger.error("Finished the Testcases Page Factory");
        logger.fatal("Fatal error - code is dead!");
        Assert.assertEquals(error_msg, ConfigManager.get("error_message"));
    }
}