package edu.iastate.ece.sd.sdmay2126.runner.gui.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Provides management and initialization for Chromium web-drivers.
 */
public class SeleniumDriverChrome implements SeleniumDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        System.setProperty("webdriver.chrome.driver", driverConfiguration.getDriverLocation());
        return new ChromeDriver(
                new ChromeOptions().setHeadless(driverConfiguration.getHeadless())
        );
    }
}
