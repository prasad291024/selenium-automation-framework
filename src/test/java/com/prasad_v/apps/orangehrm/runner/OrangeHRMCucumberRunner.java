package com.prasad_v.apps.orangehrm.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/orangehrm",
        glue = "com.prasad_v.apps.orangehrm.definitions",
        tags = "not @Ignore",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/orangehrm/cucumber.html",
                "json:target/cucumber-reports/orangehrm/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class OrangeHRMCucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
