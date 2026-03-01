package com.prasad_v.base;

import com.prasad_v.driver.DriverManagerTL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class CommonToAllTest {

    @BeforeMethod
    public void setUp(){
        DriverManagerTL.init();
    }

    @AfterMethod
    public void tearDown(){
        DriverManagerTL.quit();
    }
}
