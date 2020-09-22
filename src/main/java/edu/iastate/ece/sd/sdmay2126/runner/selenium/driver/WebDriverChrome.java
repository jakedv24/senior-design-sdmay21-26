package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumDriverConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Provides management and initialization for Chromium web-drivers.
 */
public class WebDriverChrome implements WebDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        System.setProperty("webdriver.chrome.driver", driverConfiguration.getDriverLocation());
        return new ChromeDriver(
                // TODO: Expose through configuration; make "debug" option
                new ChromeOptions().setHeadless(false)
        );
    }
}
