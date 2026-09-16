package com.prasad_v.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Unit tests for ConfigManager.
 * Focuses on environment variable placeholder resolution, fallback logic,
 * and configuration lookups without external browser or network dependencies.
 */
public class ConfigManagerTest {

    @Test
    public void testResolveEnvPlaceholderNullAndPlain() {
        Assert.assertNull(ConfigManager.resolveEnvPlaceholder(null));
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder(""), "");
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder("plain_text"), "plain_text");
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder("https://app.vwo.com"), "https://app.vwo.com");
    }

    @Test
    public void testResolveEnvPlaceholderIncompleteBrackets() {
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder("${INCOMPLETE"), "${INCOMPLETE");
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder("INCOMPLETE}"), "INCOMPLETE}");
        Assert.assertEquals(ConfigManager.resolveEnvPlaceholder("$NOT_WRAPPED"), "$NOT_WRAPPED");
    }

    @Test
    public void testResolveEnvPlaceholderExistingSystemEnv() {
        // Find any existing non-blank system environment variable (e.g. PATH or JAVA_HOME)
        Map<String, String> env = System.getenv();
        String candidateKey = null;
        for (Map.Entry<String, String> entry : env.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isBlank()) {
                candidateKey = entry.getKey();
                break;
            }
        }

        if (candidateKey != null) {
            String resolved = ConfigManager.resolveEnvPlaceholder("${" + candidateKey + "}");
            Assert.assertEquals(resolved, System.getenv(candidateKey));
        }
    }

    @Test
    public void testResolveEnvPlaceholderDefaultCredentialFallback() {
        String testKey = "TEST_CONFIG_MGR_UNIT_VAR";
        ConfigManager.DEFAULT_CREDENTIAL_VALUES.put(testKey, "FallbackUnitValue");
        try {
            String resolved = ConfigManager.resolveEnvPlaceholder("${" + testKey + "}");
            Assert.assertEquals(resolved, "FallbackUnitValue");
        } finally {
            ConfigManager.DEFAULT_CREDENTIAL_VALUES.remove(testKey);
        }
    }

    @Test
    public void testResolveEnvPlaceholderUnsetWithoutDefault() {
        String nonExistentKey = "DEFINITELY_NON_EXISTENT_VAR_12345_XYZ";
        String resolved = ConfigManager.resolveEnvPlaceholder("${" + nonExistentKey + "}");
        Assert.assertEquals(resolved, "");
    }

    @Test
    public void testSystemPropertyOverride() {
        String testProp = "config.manager.unit.test.key";
        String expectedVal = "overriddenValue123";
        System.setProperty(testProp, expectedVal);
        try {
            String actualVal = ConfigManager.get(testProp);
            Assert.assertEquals(actualVal, expectedVal);
            String requiredVal = ConfigManager.getRequired(testProp);
            Assert.assertEquals(requiredVal, expectedVal);
        } finally {
            System.clearProperty(testProp);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetRequiredThrowsOnMissingKey() {
        ConfigManager.getRequired("DEFINITELY_MISSING_KEY_XYZ_98765");
    }

    @Test
    public void testGetEnvironmentAndApp() {
        Assert.assertNotNull(ConfigManager.getEnvironment());
        Assert.assertNotNull(ConfigManager.getApp());
    }
}
