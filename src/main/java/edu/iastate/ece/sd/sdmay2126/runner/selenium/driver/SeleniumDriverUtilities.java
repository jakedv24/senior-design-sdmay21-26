package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

/**
 * General utilities for interaction with Selenium drivers.
 */
public class SeleniumDriverUtilities {
    /**
     * Identifies a Selenium driver enum value from its string.
     */
    public static SeleniumDrivers getDriverFromString(String driverName) throws InvalidSeleniumDriverException {
        switch (driverName.toLowerCase()) {
            case "chrome":
                return SeleniumDrivers.CHROME;
            case "firefox":
                return SeleniumDrivers.FIREFOX;
            case "chrome_remote":
                return SeleniumDrivers.CHROME_REMOTE;
            default:
                throw new InvalidSeleniumDriverException(driverName);
        }
    }
}
