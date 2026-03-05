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
    // Initialize WebDriver
    // ======================
    // This method reads the browser type from properties file and launches the appropriate browser
    public static void init() {
        // Read the 'browser' key from properties file
        String browser = PropertiesReader.readKey("browser");

        // Convert browser name to lowercase to make comparison case-insensitive
        browser = browser.toLowerCase();

        // Select browser based on value read from config
        switch (browser) {

            // If browser = edge
            case "edge":
                System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\edgedriver_win64\\msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--guest");
                driver = new EdgeDriver(edgeOptions);
                break;

            // If browser = chrome
            case "chrome":
                // Create ChromeOptions to configure browser settings
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized"); // open in maximized window

                // ============ ADD THESE LINES FOR CI/CD SUPPORT ============
                // Check if running in CI/CD environment (GitHub Actions)
                // When GITHUB_ACTIONS env variable is set, use headless mode
                if (System.getenv("GITHUB_ACTIONS") != null) {
                    // Headless mode - run without GUI (for CI/CD)
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                // ============ END CI/CD SUPPORT CODE ============

                driver = new ChromeDriver(chromeOptions);        // launch Chrome with options
                break;

            // If browser = firefox
            case "firefox":
                // Create FirefoxOptions to configure browser settings
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized"); // open in maximized window

                // ============ ADD THESE LINES FOR CI/CD SUPPORT ============
                // Check if running in CI/CD environment (GitHub Actions)
                if (System.getenv("GITHUB_ACTIONS") != null) {
                    // Headless mode for Firefox
                    firefoxOptions.addArguments("--headless");
                }
                // ============ END CI/CD SUPPORT CODE ============

                driver = new FirefoxDriver(firefoxOptions);       // launch Firefox with options
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
            driver.quit(); // Close all browser windows and end session
            driver = null; // Prevents stale driver access
        }
    }
}