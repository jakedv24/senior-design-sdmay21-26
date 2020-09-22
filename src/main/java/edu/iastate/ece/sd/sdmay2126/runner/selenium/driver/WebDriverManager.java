package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

import org.openqa.selenium.WebDriver;

/**
 * Provides configuration and initializtion capabilites for Selenium web-drivers.
 */
public interface WebDriverManager {
    /**
     * Should initialize a web-driver with the specified driver configuration.
     */
    WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration);

    // TODO: Add routine for downloading or locating drivers on the user's device
}
