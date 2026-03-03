package com.prasad_v.tests.examples;

import com.prasad_v.utils.ConfigManager;
import com.prasad_v.utils.LoggerUtil;
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
    public void testConfigManager() {
        String browser = ConfigManager.get("browser");
        assertThat(browser).isNotNull();
        System.out.println("✅ ConfigManager working - Browser: " + browser);
        
        String env = ConfigManager.getEnvironment();
        System.out.println("✅ Environment: " + env);
        assertThat(env).isNotNull();
    }
}
