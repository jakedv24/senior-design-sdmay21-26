package edu.iastate.ece.sd.sdmay2126.runner.gui;

import edu.iastate.ece.sd.sdmay2126.runner.gui.authentication.SeleniumAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.gui.authentication.globus.GlobusAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.gui.driver.SeleniumDriverConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.gui.driver.SeleniumDrivers;

/**
 * Specifies some Selenium configuration, including driver and authentication information.
 */
public class SeleniumConfiguration {
    private SeleniumDriverConfiguration driverConfiguration;
    private SeleniumAuthenticationConfiguration authenticationConfiguration;
    private String narrativeIdentifier;

    // TODO: This little "helper" should probably be removed/refactored to decouple from Globus
    public SeleniumConfiguration(
            String globusUsername, String globusPassword, SeleniumDrivers driverType,
            String driverLocation, String narrativeIdentifier, boolean headless) {
        this(
                new SeleniumDriverConfiguration(driverType, driverLocation, headless),
                new GlobusAuthenticationConfiguration(globusUsername, globusPassword),
                narrativeIdentifier
        );
    }

    /**
     * @param narrativeIdentifier The final URL path segment which identifies the narrative to load.
     */
    public SeleniumConfiguration(
            SeleniumDriverConfiguration driverConfiguration,
            SeleniumAuthenticationConfiguration authenticationConfiguration,
            String narrativeIdentifier) {
        this.driverConfiguration = driverConfiguration;
        this.authenticationConfiguration = authenticationConfiguration;
        this.narrativeIdentifier = narrativeIdentifier;
    }

    public SeleniumDriverConfiguration getDriverConfiguration() {
        return driverConfiguration;
    }

    public SeleniumAuthenticationConfiguration getAuthenticationConfiguration() {
        return authenticationConfiguration;
    }

    public String getNarrativeIdentifier() {
        return narrativeIdentifier;
    }
}
