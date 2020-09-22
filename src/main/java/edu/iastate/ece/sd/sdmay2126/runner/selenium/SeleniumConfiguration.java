package edu.iastate.ece.sd.sdmay2126.runner.selenium;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus.GlobusAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDriverConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.driver.SeleniumDrivers;

/**
 * Specifies some Selenium configuration, including driver and authentication information.
 */
public class SeleniumConfiguration {
    private SeleniumDriverConfiguration driverConfiguration;
    private SeleniumAuthenticationConfiguration authenticationConfiguration;

    // TODO: This little "helper" should probably be removed/refactored to decouple from Globus
    public SeleniumConfiguration(String globusUsername, String globusPassword, SeleniumDrivers driverType, String driverLocation) {
        this(
                new SeleniumDriverConfiguration(driverType, driverLocation),
                new GlobusAuthenticationConfiguration(globusUsername, globusPassword)
        );
    }

    public SeleniumConfiguration(
            SeleniumDriverConfiguration driverConfiguration,
            SeleniumAuthenticationConfiguration authenticationConfiguration) {
        this.driverConfiguration = driverConfiguration;
    }

    public SeleniumDriverConfiguration getDriverConfiguration() {
        return driverConfiguration;
    }

    public SeleniumAuthenticationConfiguration getAuthenticationConfiguration() {
        return authenticationConfiguration;
    }
}
