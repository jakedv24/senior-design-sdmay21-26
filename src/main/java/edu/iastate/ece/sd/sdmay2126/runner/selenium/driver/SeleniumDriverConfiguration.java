package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

/**
 * Specifies web-driver configuration for Selenium automation.
 */
public class SeleniumDriverConfiguration {
    private SeleniumWebDrivers driverType;
    private String driverLocation;

    public SeleniumDriverConfiguration(SeleniumWebDrivers driverType, String driverLocation) {
        this.driverType = driverType;
        this.driverLocation = driverLocation;
    }

    public SeleniumWebDrivers getDriverType() {
        return driverType;
    }

    public String getDriverLocation() {
        return driverLocation;
    }
}
