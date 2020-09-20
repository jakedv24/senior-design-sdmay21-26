package edu.iastate.ece.sd.sdmay2126.runner.selenium;

public class SeleniumConfiguration {
    private String webDriverLocation;
    private String globusUsername;
    private String globusPassword;

    public SeleniumConfiguration(String webDriverLocation, String globusUsername, String globusPassword) {
        this.webDriverLocation = webDriverLocation;
        this.globusUsername = globusUsername;
        this.globusPassword = globusPassword;
    }

    public String getWebDriverLocation() {
        return webDriverLocation;
    }

    public String getGlobusUsername() {
        return globusUsername;
    }

    public String getGlobusPassword() {
        return globusPassword;
    }
}
