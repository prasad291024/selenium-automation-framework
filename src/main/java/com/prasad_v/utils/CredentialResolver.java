package com.prasad_v.utils;

/**
 * Utility class for resolving environment variable placeholders in credential values.
 * This extracts the duplicated resolveCredentials logic from Cucumber step definitions.
 */
public class CredentialResolver {

    /**
     * Resolves environment variable placeholders in the format "${ENV_VAR_NAME}".
     * If the value is a placeholder, it will be resolved against system environment variables.
     * If the environment variable is not set, returns the original value.
     *
     * @param value The value potentially containing a placeholder
     * @return The resolved value or original value if not a placeholder or env var not set
     */
    public static String resolveCredentials(String value) {
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVarName = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVarName);
            return envValue != null ? envValue : value;
        }
        return value;
    }
}