package edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication;

/**
 * Specifies authentication configuration related to a particular flow.
 */
public class SeleniumAuthenticationConfiguration {
    private SeleniumAuthenticationFlows flowType;

    public SeleniumAuthenticationConfiguration(SeleniumAuthenticationFlows flowType) {
        this.flowType = flowType;
    }

    public SeleniumAuthenticationFlows getFlowType() {
        return flowType;
    }
}
