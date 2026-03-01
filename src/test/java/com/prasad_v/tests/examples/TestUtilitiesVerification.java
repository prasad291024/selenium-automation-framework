package com.prasad_v.tests.examples;

import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.LoggerUtil;
import com.prasad_v.utils.PropertiesReader;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class TestUtilitiesVerification {

    @Test
    public void testLoggerUtil() {
        LoggerUtil.info("✅ LoggerUtil is working!");
        LoggerUtil.debug("Debug message");
        LoggerUtil.warn("Warning message");
        System.out.println("✅ LoggerUtil test passed");
    }

    @Test
    public void testPropertiesReader() {
        String browser = PropertiesReader.readKey("browser");
        assertThat(browser).isNotNull();
        System.out.println("✅ PropertiesReader working - Browser: " + browser);
    }

    @Test
    public void testConfigManager() {
        String env = ConfigManager.getEnvironment();
        System.out.println("✅ ConfigManager working - Environment: " + env);
        assertThat(env).isNotNull();
    }
}
