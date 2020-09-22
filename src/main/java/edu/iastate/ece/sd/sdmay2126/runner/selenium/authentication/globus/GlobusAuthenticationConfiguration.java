package edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.globus;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationConfiguration;
import edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication.SeleniumAuthenticationFlows;

public class GlobusAuthenticationConfiguration extends SeleniumAuthenticationConfiguration {
    private String username;
    private String password;

    public GlobusAuthenticationConfiguration(SeleniumAuthenticationFlows flowType, String username, String password) {
        super(flowType);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
