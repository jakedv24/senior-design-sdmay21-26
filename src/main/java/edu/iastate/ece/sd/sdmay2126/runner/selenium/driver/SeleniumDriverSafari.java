package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Provides management and initialization for Safari web-drivers.
 */
public class SeleniumDriverSafari implements SeleniumDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        return new SafariDriver();
    }
}
