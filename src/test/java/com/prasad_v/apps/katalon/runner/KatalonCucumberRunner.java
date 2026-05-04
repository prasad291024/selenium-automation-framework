package com.prasad_v.apps.katalon.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/katalon",
        glue = "com.prasad_v.apps.katalon.definitions",
        tags = "not @Ignore",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/katalon/cucumber.html",
                "json:target/cucumber-reports/katalon/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class KatalonCucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
