package edu.iastate.ece.sd.sdmay2126.runner.gui.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Provides management and initialization for Firefox web-drivers.
 */
public class SeleniumDriverFirefox implements SeleniumDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        System.setProperty("webdriver.gecko.driver", driverConfiguration.getDriverLocation());
        return new FirefoxDriver(
                new FirefoxOptions().setHeadless(driverConfiguration.getHeadless())
        );
    }
}
