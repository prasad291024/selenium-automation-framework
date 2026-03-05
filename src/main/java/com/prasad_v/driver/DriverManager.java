// Package declaration: This class resides under 'driver' package
package com.prasad_v.driver;

// Import custom utility to read values from config.properties file
import com.prasad_v.utils.PropertiesReader;

// Selenium WebDriver base class
import org.openqa.selenium.WebDriver;

// Browser-specific driver and options classes from Selenium
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

// Class to manage WebDriver instance lifecycle (init, get, quit)
// WARNING: This uses static WebDriver - NOT thread-safe for parallel execution
// For parallel tests, use DriverManagerTL instead
public class DriverManager {
    private DriverManager() {
        /* This utility class should not be instantiated */
    }


    // Static WebDriver instance accessible globally within the framework
    // NOTE: Static driver is NOT thread-safe - use DriverManagerTL for parallel execution
    public static WebDriver driver;

    // Getter method for the WebDriver
    // This is how you access the browser instance from any class
    public static WebDriver getDriver() {
        return driver;
    }

    // Setter method for the WebDriver
    // This can be used if you want to assign a WebDriver manually (e.g., remote or local)
    public static void setDriver(WebDriver driver) {
        DriverManager.driver = driver;
    }

    // ======================
    // Helper Method: Check if running in CI/CD
    // ======================
    // Returns true if running in CI/CD environment (GitHub Actions, Jenkins, etc.)
    private static boolean isRunningInCI() {
        // Check multiple CI/CD environment variables for broad compatibility
        String gitHubActions = System.getenv("GITHUB_ACTIONS");
        String ciEnv = System.getenv("CI");
        String jenkinsHome = System.getenv("JENKINS_HOME");

        // If any CI/CD indicator is found, we're in CI/CD
        boolean isCI = gitHubActions != null || ciEnv != null || jenkinsHome != null;

        if (isCI) {
            System.out.println("[INFO] CI/CD environment detected - enabling headless mode");
        }

        return isCI;
    }

    // ======================
    // Initialize WebDriver
    // ======================
    // This method reads the browser type from properties file and launches the appropriate browser
    public static void init() {
        // Read the 'browser' key from properties file
        String browser = PropertiesReader.readKey("browser");

        // Convert browser name to lowercase to make comparison case-insensitive
        browser = browser.toLowerCase();

        // Detect if running in CI/CD environment
        boolean isCIEnvironment = isRunningInCI();

        // Select browser based on value read from config
        switch (browser) {

            // If browser = edge
            case "edge":
                System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\edgedriver_win64\\msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--guest");

                // Add headless mode if in CI/CD
                if (isCIEnvironment) {
                    edgeOptions.addArguments("--headless");
                }

                driver = new EdgeDriver(edgeOptions);
                break;

            // If browser = chrome
            case "chrome":
                // Create ChromeOptions to configure browser settings
                ChromeOptions chromeOptions = new ChromeOptions();

                // Always add these arguments
                chromeOptions.addArguments("--start-maximized");

                // ============ CI/CD HEADLESS MODE ============
                // If running in CI/CD environment, add headless arguments
                if (isCIEnvironment) {
                    System.out.println("[INFO] Configuring Chrome for headless execution");

                    // Essential headless arguments
                    chromeOptions.addArguments("--headless=new");           // New headless mode (more stable)
                    chromeOptions.addArguments("--no-sandbox");             // Required for Docker/CI environments
                    chromeOptions.addArguments("--disable-dev-shm-usage");  // Prevent memory issues in CI/CD
                    chromeOptions.addArguments("--disable-gpu");            // Disable GPU (not available in CI/CD)
                    chromeOptions.addArguments("--window-size=1920,1080");  // Set window size
                    chromeOptions.addArguments("--disable-extensions");     // Disable extensions
                    chromeOptions.addArguments("--disable-plugins");        // Disable plugins
                    chromeOptions.addArguments("--disable-infobars");       // Remove infobars
                    chromeOptions.addArguments("--disable-notifications");  // Disable notifications
                    chromeOptions.addArguments("--disable-default-apps");   // Disable default apps
                    chromeOptions.addArguments("--single-process");         // Single process (for CI/CD)
                }
                // ============ END CI/CD HEADLESS MODE ============

                // Launch Chrome with configured options
                driver = new ChromeDriver(chromeOptions);

                if (isCIEnvironment) {
                    System.out.println("[INFO] Chrome launched in headless mode");
                }
                break;

            // If browser = firefox
            case "firefox":
                // Create FirefoxOptions to configure browser settings
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");

                // Add headless mode if in CI/CD
                if (isCIEnvironment) {
                    System.out.println("[INFO] Configuring Firefox for headless execution");
                    firefoxOptions.addArguments("--headless");
                }

                driver = new FirefoxDriver(firefoxOptions);

                if (isCIEnvironment) {
                    System.out.println("[INFO] Firefox launched in headless mode");
                }
                break;

            // If browser value is not supported or invalid
            default:
                System.out.println("Not browser Supported!!!");
                // You can also throw an exception here to fail fast
                // throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    // ======================
    // Quit WebDriver
    // ======================
    // This method safely quits the browser and sets the WebDriver instance to null
    public static void down() {
        // Check if driver is already initialized
        if (getDriver() != null) {
            try {
                driver.quit(); // Close all browser windows and end session
                driver = null; // Prevents stale driver access
                System.out.println("[INFO] WebDriver closed successfully");
            } catch (Exception e) {
                System.out.println("[WARNING] Error closing WebDriver: " + e.getMessage());
                driver = null;
            }
        }
    }
}