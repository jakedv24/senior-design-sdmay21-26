package edu.iastate.ece.sd.sdmay2126.runner.gui.driver;

/**
 * Specifies web-driver configuration for Selenium automation.
 */
public class SeleniumDriverConfiguration {
    private SeleniumDrivers driverType;
    private String driverLocation;
    private boolean headless;

    public SeleniumDriverConfiguration(SeleniumDrivers driverType, String driverLocation, boolean headless) {
        this.driverType = driverType;
        this.driverLocation = driverLocation;
        this.headless = headless;
    }

    public SeleniumDrivers getDriverType() {
        return driverType;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public boolean getHeadless() {
        return headless;
    }
}
