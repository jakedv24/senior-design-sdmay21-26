package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumDriverConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Provides management and initialization for Firefox web-drivers.
 */
public class WebDriverFirefox implements WebDriverManager {
    @Override
    public WebDriver initializeDriver(SeleniumDriverConfiguration driverConfiguration) {
        System.setProperty("webdriver.gecko.driver", driverConfiguration.getDriverLocation());
        return new FirefoxDriver(
                // TODO: Expose through configuration; make "debug" option
                new FirefoxOptions().setHeadless(false)
        );
    }
}
