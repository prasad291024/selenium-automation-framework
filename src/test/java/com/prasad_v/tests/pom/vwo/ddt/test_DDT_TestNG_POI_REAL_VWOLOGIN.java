package com.prasad_v.tests.pom.vwo.ddt;
import com.prasad_v.UtilsExcel.UtilExcel;
import com.prasad_v.base.CommonToAllTest;
import com.prasad_v.pages.pageObjectModel.appvwo.imporved_POM.LoginPage;
import com.prasad_v.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.prasad_v.driver.DriverManager.getDriver;

public class test_DDT_TestNG_POI_REAL_VWOLOGIN extends CommonToAllTest {

    @Test(dataProvider = "getData")
    public void test_vwo_login(String email, String password) {
        System.out.println(email + " - "+ password);
        getDriver().navigate().to(ConfigManager.get("url"));
        Assert.assertEquals(getDriver().getTitle(), ConfigManager.get("current_title"));
        Assert.assertEquals(getDriver().getCurrentUrl(), ConfigManager.get("current_url"));

        LoginPage loginPage = new LoginPage(getDriver());
        String error_message = loginPage.loginToVWOLoginInvalidCreds(email, password);
        Assert.assertEquals(error_message, ConfigManager.get("error_message"));
    }

    @DataProvider(parallel = true)
    public Object[][] getData(){
        return UtilExcel.getTestDataFromExcel("sheet1");
    }
}