package com.prasad_v.apps.vwo.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/vwo",
        glue = "com.prasad_v.apps.vwo.definitions",
        tags = "not @Ignore",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/vwo/cucumber.html",
                "json:target/cucumber-reports/vwo/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class VWOCucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
