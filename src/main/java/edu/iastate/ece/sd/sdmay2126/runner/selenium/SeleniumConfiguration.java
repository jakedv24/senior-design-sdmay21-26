package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDrivers;

/**
 * Specifies some Selenium configuration, including driver and authentication information.
 */
public class SeleniumConfiguration {
    private String globusUsername;
    private String globusPassword;
    private SeleniumDriverConfiguration driverConfiguration;

    public SeleniumConfiguration(String globusUsername, String globusPassword, SeleniumDrivers driverType, String driverLocation) {
        this(
                globusUsername,
                globusUsername,
                new SeleniumDriverConfiguration(driverType, driverLocation)
        );
    }

    public SeleniumConfiguration(String globusUsername, String globusPassword, SeleniumDriverConfiguration driverConfiguration) {
        this.globusUsername = globusUsername;
        this.globusPassword = globusPassword;
        this.driverConfiguration = driverConfiguration;
    }

    public String getGlobusUsername() {
        return globusUsername;
    }

    public String getGlobusPassword() {
        return globusPassword;
    }

    public SeleniumDriverConfiguration getDriverConfiguration() {
        return driverConfiguration;
    }
}
