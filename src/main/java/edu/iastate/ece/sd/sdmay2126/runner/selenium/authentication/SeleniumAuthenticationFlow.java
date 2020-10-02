package edu.iastate.ece.sd.sdmay2126.runner.selenium.authentication;

import edu.iastate.ece.sd.sdmay2126.runner.selenium.SeleniumIdentificationException;

/**
 * Perform an authentication flow, such as Globus OAuth, to some session.
 */
public interface SeleniumAuthenticationFlow {
    /**
     * Should perform the authentication flow.
     */
    void authenticateSession() throws SeleniumIdentificationException, InterruptedException;
}
