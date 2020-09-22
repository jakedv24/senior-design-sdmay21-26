package edu.iastate.ece.sd.sdmay2126.runner.selenium.driver;

/**
 * Specifies web-driver configuration for Selenium automation.
 */
public class SeleniumDriverConfiguration {
    private SeleniumDrivers driverType;
    private String driverLocation;

    public SeleniumDriverConfiguration(SeleniumDrivers driverType, String driverLocation) {
        this.driverType = driverType;
        this.driverLocation = driverLocation;
    }

    public SeleniumDrivers getDriverType() {
        return driverType;
    }

    public String getDriverLocation() {
        return driverLocation;
    }
}
