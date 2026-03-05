package com.prasad_v.driver;

import com.prasad_v.utils.PropertiesReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManagerTL {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    // Returns true if running in a CI/CD environment
    private static boolean isCI() {
        return System.getenv("GITHUB_ACTIONS") != null
                || System.getenv("CI") != null
                || System.getenv("JENKINS_HOME") != null;
    }

    public static void init() {
        String browser = PropertiesReader.readKey("browser").toLowerCase();
        boolean ci = isCI();

        if (ci) {
            System.out.println("[INFO] CI/CD environment detected - enabling headless mode for: " + browser);
        }

        WebDriver driverInstance = switch (browser) {

            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (ci) {
                    options.addArguments("--headless=new");          // Stable headless mode
                    options.addArguments("--no-sandbox");             // Required in Docker/CI
                    options.addArguments("--disable-dev-shm-usage");  // Prevent memory crashes
                    options.addArguments("--disable-gpu");            // No GPU in CI
                    options.addArguments("--window-size=1920,1080"); // Fixed size (replaces --start-maximized)
                    options.addArguments("--disable-extensions");
                    options.addArguments("--disable-notifications");
                } else {
                    options.addArguments("--start-maximized");
                    options.addArguments("--disable-notifications");
                }
                yield new ChromeDriver(options);
            }

            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (ci) {
                    options.addArguments("--headless");
                    options.addArguments("--width=1920");
                    options.addArguments("--height=1080");
                } else {
                    options.addArguments("--start-maximized");
                }
                yield new FirefoxDriver(options);
            }

            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (ci) {
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--window-size=1920,1080");
                } else {
                    options.addArguments("--start-maximized");
                    options.addArguments("--guest");
                }
                yield new EdgeDriver(options);
            }

            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        setDriver(driverInstance);
    }

    public static void quit() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }
}